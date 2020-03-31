package com.fairandsmart.consent.manager.entity;

import javax.persistence.Embeddable;
import java.util.Set;

@Embeddable
public class Content {

    public String title;
    public String body;
    public String footer;
    public Set<String> attachments;

}
