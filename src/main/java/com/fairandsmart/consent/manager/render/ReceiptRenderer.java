package com.fairandsmart.consent.manager.render;

/*-
 * #%L
 * Right Consents, a Universal Consents & Preferences Management Platform.
 * %%
 * Copyright (C) 2020 - 2021 Fair And Smart
 * %%
 * This file is part of Right Consents Community Edition.
 * 
 * Right Consents Community Edition is published by FAIR AND SMART under the
 * GNU GENERAL PUBLIC LICENCE Version 3 (GPLv3) and a set of additional terms.
 * 
 * For more information, please see the “LICENSE” and “LICENSE.FAIRANDSMART”
 * files, or see https://www.fairandsmart.com/opensource/.
 * #L%
 */

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

public abstract class ReceiptRenderer {

    protected static JAXBContext jaxbContext;

    static {
        try {
            jaxbContext = JAXBContext.newInstance(RenderableReceipt.class);
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
    }

    abstract public String format();

    abstract public byte[] render(RenderableReceipt receipt) throws RenderingException;

}
