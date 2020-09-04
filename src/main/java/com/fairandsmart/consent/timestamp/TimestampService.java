package com.fairandsmart.consent.timestamp;

import org.w3c.dom.Document;

public interface TimestampService {

    void timestamp(Document xml) throws TimestampServiceException;
}
