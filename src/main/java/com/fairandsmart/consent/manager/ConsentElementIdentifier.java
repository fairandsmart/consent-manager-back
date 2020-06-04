package com.fairandsmart.consent.manager;

public class ConsentElementIdentifier {

    public static final String prefix = "element";
    public static final Character separator = '/';

    private String type;
    private String key;
    private String serial;

    public ConsentElementIdentifier(String type, String key, String serial) {
        this.type = type;
        this.key = key;
        this.serial = serial;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public static ConsentElementIdentifier deserialize(String serializedIdentifier) throws IllegalIdentifierException {
        String[] parts = serializedIdentifier.split(String.valueOf(separator));
        if (parts.length != 4 || !parts[0].equals(prefix) ) {
            throw new IllegalIdentifierException(serializedIdentifier);
        }
        return new ConsentElementIdentifier(parts[1], parts[2], parts[3]);
    }

    public String serialize() {
        return prefix + separator + type + separator + key + separator + serial;
    }

    @Override
    public String toString() {
        return serialize();
    }
}
