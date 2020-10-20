package com.fairandsmart.consent.manager.model;

import com.fairandsmart.consent.manager.entity.ModelData;

import java.util.List;
import java.util.Objects;

public class Preference extends ModelData {

    public static final String TYPE = "preference";

    private String title;
    private String body;
    private List<String> contentTypeOptions;
    private List<String> contentThemeOptions;
    private List<String> channelOptions;
    private List<String> formatOptions;
    private List<String> frequencyOptions;
    private List<String> localeOptions;

    public Preference() {
        this.setType(TYPE);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getContentTypeOptions() {
        return contentTypeOptions;
    }

    public void setContentTypeOptions(List<String> contentTypeOptions) {
        this.contentTypeOptions = contentTypeOptions;
    }

    public List<String> getContentThemeOptions() {
        return contentThemeOptions;
    }

    public void setContentThemeOptions(List<String> contentThemeOptions) {
        this.contentThemeOptions = contentThemeOptions;
    }

    public List<String> getChannelOptions() {
        return channelOptions;
    }

    public void setChannelOptions(List<String> channelOptions) {
        this.channelOptions = channelOptions;
    }

    public List<String> getFormatOptions() {
        return formatOptions;
    }

    public void setFormatOptions(List<String> formatOptions) {
        this.formatOptions = formatOptions;
    }

    public List<String> getFrequencyOptions() {
        return frequencyOptions;
    }

    public void setFrequencyOptions(List<String> frequencyOptions) {
        this.frequencyOptions = frequencyOptions;
    }

    public List<String> getLocaleOptions() {
        return localeOptions;
    }

    public void setLocaleOptions(List<String> localeOptions) {
        this.localeOptions = localeOptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Preference that = (Preference) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(body, that.body) &&
                Objects.equals(contentTypeOptions, that.contentTypeOptions) &&
                Objects.equals(contentThemeOptions, that.contentThemeOptions) &&
                Objects.equals(channelOptions, that.channelOptions) &&
                Objects.equals(formatOptions, that.formatOptions) &&
                Objects.equals(frequencyOptions, that.frequencyOptions) &&
                Objects.equals(localeOptions, that.localeOptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, body, contentTypeOptions, contentThemeOptions, channelOptions, formatOptions, frequencyOptions, localeOptions);
    }

    @Override
    public String toString() {
        return "Preference{" +
                "title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", contentTypeOptions=" + contentTypeOptions +
                ", contentThemeOptions=" + contentThemeOptions +
                ", channelOptions=" + channelOptions +
                ", formatOptions=" + formatOptions +
                ", frequencyOptions=" + frequencyOptions +
                ", localeOptions=" + localeOptions +
                '}';
    }
}
