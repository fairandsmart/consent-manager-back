package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.manager.entity.ModelVersion;

import java.util.ArrayList;
import java.util.List;

public class ConsentForm {

    private ModelVersion header;
    private List<ModelVersion> elements;
    private ModelVersion footer;

    public ConsentForm() {
        elements = new ArrayList<>();
    }

    public ModelVersion getHeader() {
        return header;
    }

    public void setHeader(ModelVersion header) {
        this.header = header;
    }

    public List<ModelVersion> getElements() {
        return elements;
    }

    public void setElements(List<ModelVersion> elements) {
        this.elements = elements;
    }

    public void addElement(ModelVersion element) {
        this.elements.add(element);
    }

    public ModelVersion getFooter() {
        return footer;
    }

    public void setFooter(ModelVersion footer) {
        this.footer = footer;
    }

    @Override
    public String toString() {
        return "ConsentForm{" +
                "header=" + header +
                ", elements=" + elements +
                ", footer=" + footer +
                '}';
    }
}
