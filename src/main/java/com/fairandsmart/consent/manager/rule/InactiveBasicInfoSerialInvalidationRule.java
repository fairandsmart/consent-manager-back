package com.fairandsmart.consent.manager.rule;

import com.fairandsmart.consent.common.config.MainConfig;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.entity.Record;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
public class InactiveBasicInfoSerialInvalidationRule extends RecordStatusFilterRule {

    private static final Logger LOGGER = Logger.getLogger(InactiveBasicInfoSerialInvalidationRule.class.getName());

    private Map<String, List<String>> activeSerialsCache = new HashMap<>();

    @Inject
    MainConfig config;

    @Override
    public void apply(List<Record> records) {
        LOGGER.log(Level.FINE, "searching records with invalid basic info serial");
        records.stream().filter(record ->
            record.status.equals(Record.Status.COMMITTED) && record.infoKey != null
                && !record.infoKey.isEmpty() && getActiveSerial(record.infoKey).contains(record.infoSerial)
        ).forEach(record -> {
            LOGGER.log(Level.FINE, "marking record as irrelevant, " + record.id);
            record.status = Record.Status.IRRELEVANT;
            record.statusExplanation = "basic info serial no more active";
        });
        this.applyNext(records);
    }

    private List<String> getActiveSerial(String key) {
        if ( !activeSerialsCache.containsKey(key) ) {
            activeSerialsCache.put(key, ModelVersion.SystemHelper.findActiveSerialsForKey(config.owner(), key));
        }
        return activeSerialsCache.get(key);
    }
}