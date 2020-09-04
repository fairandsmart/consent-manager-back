package com.fairandsmart.consent.common.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.ZonedDateTime;

public class ZonedDateTimeAdapter extends XmlAdapter<String, ZonedDateTime> {

    @Override
    public String marshal(ZonedDateTime date) {
        return date.toString();
    }

    @Override
    public ZonedDateTime unmarshal(String date) {
        return ZonedDateTime.parse(date);
    }

}
