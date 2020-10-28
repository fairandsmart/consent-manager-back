package com.fairandsmart.consent.manager.render;

import com.fairandsmart.consent.manager.model.Receipt;

public interface ReceiptRenderer {

    String format();

    byte[] render(Receipt receipt) throws RenderingException;

}
