package com.fairandsmart.consent.common.hash;

import org.apache.commons.codec.digest.DigestUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HashGenerator {

    private static final Logger LOGGER = Logger.getLogger(HashGenerator.class.getName());

    public static String hash(Object obj) throws IllegalAccessException {
        String serialized = extractHashString(obj);
        LOGGER.log(Level.FINE, "Serialized before hash: " + serialized);
        return DigestUtils.sha256Hex(serialized);
    }

    private static String extractHashString(Object obj) throws IllegalAccessException {
        if (obj.getClass().isAnnotationPresent(HashPart.class)) {
            StringBuffer serialized = new StringBuffer();
            serialized.append(obj.getClass().getSimpleName());
            serialized.append("{");
            boolean first = true;
            //Fields are ordered to ensure uniqueness of serialization using natural order
            Field[] fields = obj.getClass().getDeclaredFields();
            Arrays.sort(fields, new Comparator<Field>() {
                @Override
                public int compare(Field o1, Field o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            for (Field field : fields) {
                if (field.isAnnotationPresent(HashPart.class)) {
                    if ( !first ) {
                        serialized.append(", ");
                    } else {
                        first = false;
                    }
                    field.setAccessible(true);
                    serialized.append(field.getName());
                    serialized.append("='");
                    serialized.append(extractHashString(field.get(obj)));
                    serialized.append("'");
                }
            }
            serialized.append("}");
            return serialized.toString();
        } else {
            return obj.toString();
        }
    }
}
