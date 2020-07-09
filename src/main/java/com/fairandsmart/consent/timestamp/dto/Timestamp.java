package com.fairandsmart.consent.timestamp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.text.SimpleDateFormat;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Timestamp {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    private String algo;
    private Long time;
    private String response;

    public Timestamp() {}

    public Timestamp(String algo, Long time, String response) {
        this.algo = algo;
        this.time = time;
        this.response = response;
    }

    public String getAlgo() {
        return algo;
    }

    public Long getTime() {
        return time;
    }

    public String getResponse() {
        return response;
    }

    public String getDate() {
        return simpleDateFormat.format(time);
    }

    @Override
    public String toString() {
        return "{" + algo + "}{" + response + "}{" + simpleDateFormat.format(time) + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Timestamp timestamp = (Timestamp) o;

        if (!Objects.equals(time, timestamp.time)) return false;
        return Objects.equals(response, timestamp.response);
    }

    @Override
    public int hashCode() {
        int result = time != null ? time.hashCode() : 0;
        result = 31 * result + (response != null ? response.hashCode() : 0);
        return result;
    }
}
