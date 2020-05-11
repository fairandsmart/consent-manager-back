package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.data.ConsentElementData;
import com.fairandsmart.consent.manager.entity.ConsentRecord;
import com.fairandsmart.consent.manager.entity.ConsentElementContent;
import com.fairandsmart.consent.manager.entity.ConsentElementEntry;
import com.fairandsmart.consent.manager.entity.ConsentElementVersion;
import com.fairandsmart.consent.manager.filter.ModelEntryFilter;
import com.fairandsmart.consent.manager.receipt.ConsentReceipt;
import com.fairandsmart.consent.security.AuthenticationService;
import com.fairandsmart.consent.serial.SerialGenerator;
import com.fairandsmart.consent.serial.SerialGeneratorException;
import com.fairandsmart.consent.token.InvalidTokenException;
import com.fairandsmart.consent.token.TokenExpiredException;
import com.fairandsmart.consent.token.TokenService;
import com.fairandsmart.consent.token.TokenServiceException;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.Instant;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.fairandsmart.consent.manager.entity.ConsentElementEntry.DEFAULT_BRANCHE;

@ApplicationScoped
public class ConsentServiceBean implements ConsentService {

    private static final Logger LOGGER = Logger.getLogger(ConsentService.class.getName());

    @Inject
    AuthenticationService authentication;

    @Inject
    SerialGenerator generator;

    @Inject
    TokenService tokenService;

    /* MODELS MANAGEMENT */

    @Override
    public CollectionPage<ConsentElementEntry> listModelEntries(ModelEntryFilter filter) throws AccessDeniedException {
        LOGGER.log(Level.FINE, "Listing models entries");
        String connectedIdentifier = authentication.getConnectedIdentifier();
        //TODO Handle cases where an admin is able to list any model entries
        // For now, authenticated user is enforced as owner of th emodels :
        filter.setOwner(connectedIdentifier);
        if ( !connectedIdentifier.equals(filter.getOwner()) ) {
            //This exception is not raised yet, only here to not forgot usage later
            throw new AccessDeniedException("Owner must be the connected user");
        }
        PanacheQuery<ConsentElementEntry> query = ConsentElementEntry.find("owner = ?1 and type = ?2", filter.getOwner(), filter.getType());
        CollectionPage<ConsentElementEntry> result = new CollectionPage<>();
        result.setValues(query.page(Page.of(filter.getPage()-1, filter.getSize())).list());
        result.setPageSize(filter.getSize());
        result.setPage(filter.getPage());
        result.setTotalPages(query.pageCount());
        result.setTotalCount(query.count());
        return result;
    }

    @Override
    @Transactional
    public String createModelEntry(String key, String name, String description, String locale, ConsentElementData data) throws ConsentManagerException, EntityAlreadyExistsException {
        LOGGER.log(Level.FINE, "Creating new model entry");
        String connectedIdentifier = authentication.getConnectedIdentifier();
        if ( ConsentElementEntry.isKeyAlreadyExistsForOwner(connectedIdentifier, key)) {
            throw new EntityAlreadyExistsException("A model entry already exists with key: " + key);
        }
        try {
            ConsentElementEntry entry = new ConsentElementEntry();
            entry.type = data.getType();
            entry.key = key;
            entry.name = name;
            entry.description = description;
            entry.branches = DEFAULT_BRANCHE;
            entry.owner = connectedIdentifier;
            entry.persist();

            ConsentElementVersion version = new ConsentElementVersion();
            version.entry = entry;
            version.author = connectedIdentifier;
            version.owner = connectedIdentifier;
            version.branches = DEFAULT_BRANCHE;
            version.creationDate = System.currentTimeMillis();
            version.modificationDate = version.creationDate;
            version.invalidation = ConsentElementVersion.Invalidation.INVALIDATE;
            version.status = ConsentElementVersion.Status.DRAFT;
            version.serial = generator.next(version.getClass().getName());
            version.defaultLocale = locale;
            version.availableLocales = locale;
            version.content.put(locale, new ConsentElementContent().withModelData(data));
            version.persist();

            return entry.id.toString();
        } catch ( SerialGeneratorException ex ) {
            throw new ConsentManagerException("unable to generate serial number for information model");
        } catch ( ModelDataSerializationException ex ) {
            throw new ConsentManagerException("unable to serialise model data");
        }
    }

    @Override
    public ConsentElementEntry getModelEntry(String id) throws EntityNotFoundException {
        LOGGER.log(Level.FINE, "Getting model entry for id: " + id);
        String connectedIdentifier = authentication.getConnectedIdentifier();
        ConsentElementEntry entry = ConsentElementEntry.findById(UUID.fromString(id));
        if ( entry == null || !entry.owner.equals(connectedIdentifier) ) {
            throw new EntityNotFoundException("Unable to find an entry for id: " + id);
        }
        return entry;
    }

    @Override
    public ConsentElementEntry findModelEntryByKey(String key) throws EntityNotFoundException {
        LOGGER.log(Level.FINE, "Finding model entry for key: " + key);
        String connectedIdentifier = authentication.getConnectedIdentifier();
        List<ConsentElementEntry> entries = ConsentElementEntry.find("owner = ?1 and key = ?2", connectedIdentifier, key).list();
        if ( entries.isEmpty() ) {
            throw new EntityNotFoundException("Unable to find an entry for key: " + key);
        }
        return entries.get(0);
    }

    @Override
    public ConsentElementVersion findActiveModelVersionForKey(String key) throws EntityNotFoundException {
        LOGGER.log(Level.FINE, "Finding active model version for key: " + key);
        String connectedIdentifier = authentication.getConnectedIdentifier();
        //TODO for now we only works with DRAFT
        List<ConsentElementVersion> versions = ConsentElementVersion.find("owner = ?1 and entry.key = ?2 and status = ?3", connectedIdentifier, key, ConsentElementVersion.Status.DRAFT).list();
        if ( versions.isEmpty() ) {
            throw new EntityNotFoundException("Unable to find an entry for key: " + key);
        }
        if ( versions.size() > 1 ) {
            LOGGER.log(Level.WARNING, "Found more than one active version, this is an incoherency");
        }
        return versions.get(0);
    }

    /* CONSENT MANAGEMENT */

    @Override
    public String buildToken(ConsentContext ctx) {
        //TODO Handle cases where an admin is generating a token for another owner
        // For now, authenticated user is enforced as owner of the token :
        LOGGER.log(Level.FINE, "Building generate form token for context: " + ctx);
        ctx.setOwner(authentication.getConnectedIdentifier());
        return tokenService.generateToken(ctx);
    }

    @Override
    public ConsentForm generateForm(String token) throws EntityNotFoundException, TokenServiceException, TokenExpiredException, InvalidTokenException {
        //TODO :
        // 1. Load existing records for elements of this context (applying models invalidation strategy)
        //    Adapt ConsentForm to include existing values for each elements
        // 2. According to the ConsentContext requisite adopt the correct behaviour for display or not the form or parts of the form
        // 3. If form has to be displayed, load all models to populate
        // 4. Generate a new submission token and populate the form
        LOGGER.log(Level.FINE, "Generating consent form");
        ConsentContext ctx = (ConsentContext) tokenService.readToken(token);

        ConsentForm form = new ConsentForm();
        ConsentElementVersion header = this.findActiveModelVersionForKey(ctx.getHeader());
        form.setHeader(header);
        ctx.setHeader(header.getIdentifier().serialize());

        List<String> elementsIdentifiers = new ArrayList<>();
        for (String key : ctx.getElements()) {
            ConsentElementVersion element = this.findActiveModelVersionForKey(key);
            form.addElement(element);
            elementsIdentifiers.add(element.getIdentifier().serialize());
        }
        ctx.setElements(elementsIdentifiers);

        ConsentElementVersion footer = this.findActiveModelVersionForKey(ctx.getFooter());
        form.setFooter(footer);
        ctx.setFooter(footer.getIdentifier().serialize());

        form.setToken(tokenService.generateToken(ctx));
        return form;
    }

    @Override
    @Transactional
    public ConsentReceipt submitConsent(String token, Map<String, String> values) throws InvalidConsentException, TokenServiceException, TokenExpiredException, InvalidTokenException {

        //TODO :
        // 0. Generate transaction id
        // 1. Check values coherency and ids according to context
        // 2. Build existing records according to the submitted values
        // 3. Includes relevant records in the receipt
        // 4. According to the context, commit or not the transaction, apply to records
        LOGGER.log(Level.FINE, "Submitting consent");
        ConsentContext ctx = (ConsentContext) tokenService.readToken(token);

        String transaction = UUID.randomUUID().toString();
        Instant now = Instant.now();
        this.checkValuesCoherency(ctx, values);

        List<ConsentRecord> records = new ArrayList<>();
        for ( Map.Entry<String, String> value : values.entrySet() ) {
            try {
                ConsentElementIdentifier headId = ConsentElementIdentifier.deserialize(ctx.getHeader());
                ConsentElementIdentifier bodyId = ConsentElementIdentifier.deserialize(value.getKey());
                ConsentElementIdentifier footId = ConsentElementIdentifier.deserialize(ctx.getFooter());
                ConsentRecord record = new ConsentRecord();
                record.transaction = transaction;
                record.subject = ctx.getSubject();
                record.owner = ctx.getOwner();
                record.head = headId.getSerial();
                record.type = bodyId.getType();
                record.body = bodyId.getSerial();
                record.foot = footId.getSerial();
                record.serial = headId.getSerial() + "." + bodyId.getSerial() + "." + footId.getSerial();
                record.value = value.getValue();
                record.creationTimestamp = now.toEpochMilli();
                record.expirationTimestamp = now.plus(Period.ofWeeks(52)).toEpochMilli();
                record.status = ConsentRecord.Status.PENDING;
                record.persist();
                records.add(record);
            } catch (IllegalIdentifierException e) {
                //
            }
        }
        LOGGER.log(Level.INFO, "records: " + records);

        ConsentReceipt receipt = new ConsentReceipt();
        receipt.setLocale(ctx.getLocale());
        receipt.setTransaction(transaction);
        receipt.setSubject(ctx.getSubject());
        receipt.setTimestamp(now.toEpochMilli());
        //receipt.records = records;
        return receipt;
    }

    private void checkValuesCoherency(ConsentContext ctx, Map<String, String> values) throws InvalidConsentException {
        if ( ctx.getHeader() == null && values.containsKey("header") ) {
            throw new InvalidConsentException("submitted header incoherency, expected: null got: " + values.get("header"));
        }
        if ( ctx.getHeader() != null && !ctx.getHeader().isEmpty() && (!values.containsKey("header") || !values.get("header").equals(ctx.getHeader())) ) {
            throw new InvalidConsentException("submitted header incoherency, expected: " + ctx.getHeader() + " got: " + values.get("header"));
        }
        if ( ctx.getFooter() == null && values.containsKey("footer") ) {
            throw new InvalidConsentException("submitted footer incoherency, expected: null got: " + values.get("footer"));
        }
        if ( ctx.getFooter() != null && !ctx.getFooter().isEmpty() && (!values.containsKey("footer") || !values.get("footer").equals(ctx.getFooter())) ) {
            throw new InvalidConsentException("submitted footer incoherency, expected: " + ctx.getFooter() + " got: " + values.get("footer"));
        }
        Map<String, String> submittedElementValues = values.entrySet().stream().filter(e -> e.getKey().startsWith("element")).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        if ( !new HashSet<>(ctx.getElements()).equals(submittedElementValues.keySet()) ) {
            throw new InvalidConsentException("submitted elements incoherency");
        }
    }
}
