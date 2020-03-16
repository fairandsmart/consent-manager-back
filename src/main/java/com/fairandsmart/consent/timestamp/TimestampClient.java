package com.fairandsmart.consent.timestamp;

import com.fairandsmart.consent.timestamp.dto.Timestamp;

public interface TimestampClient {

    byte[] generateFile(String hashAlgo, String hash) throws TimestampClientException;

    Timestamp generate(String hashAlgo, String hash) throws TimestampClientException;

}
