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

import com.fairandsmart.consent.manager.ConsentReceipt;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "receipt")
@XmlAccessorType(XmlAccessType.FIELD)
public class RenderableReceipt extends ConsentReceipt {

    private RenderingLayout layout;

    public RenderableReceipt() {
    }

    public RenderableReceipt(ConsentReceipt receipt, RenderingLayout layout) {
        super(receipt);
        this.layout = layout;
    }

    public RenderingLayout getLayout() {
        return layout;
    }

    public void setLayout(RenderingLayout layout) {
        this.layout = layout;
    }

}
