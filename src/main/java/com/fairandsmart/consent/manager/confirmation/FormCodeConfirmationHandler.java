package com.fairandsmart.consent.manager.confirmation;

import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.entity.Transaction;
import com.fairandsmart.consent.manager.exception.ConfirmationException;

import javax.enterprise.context.Dependent;
import javax.transaction.Transactional;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

@Dependent
public class FormCodeConfirmationHandler implements ConfirmationHandler {

    public static final String PARAM_CONFIRM_CODE_VALUE="ConfirmCodeValue";

    private static final Random rnd = new Random();

    @Override
    public boolean canHandle(ConsentContext.Confirmation confirmation) {
        return confirmation.equals(ConsentContext.Confirmation.FORM_CODE);
    }

    @Override
    @Transactional
    public Map<String, String> prepare(Transaction tx) {
        String code = this.generateCode();
        tx.params.put(PARAM_CONFIRM_CODE_VALUE, code);
        tx.persist();
        return Collections.singletonMap(PARAM_CONFIRM_CODE_VALUE, code);
    }

    @Override
    public void validate(Transaction tx, ConsentContext ctx, MultivaluedMap<String, String> values) throws ConfirmationException {
        if (!values.containsKey(PARAM_CONFIRM_CODE_VALUE)
                || values.get(PARAM_CONFIRM_CODE_VALUE).isEmpty()
                || !values.get(PARAM_CONFIRM_CODE_VALUE).get(0).equals(tx.params.get(PARAM_CONFIRM_CODE_VALUE))) {
            throw new ConfirmationException(ctx, "Unable to validate confirmation, unexisting or bad confirmation values");
        }
    }

    private String generateCode() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i=0; i<6; i++) {
            stringBuffer.append(rnd.nextInt(9));
        }
        return stringBuffer.toString();
    }


}
