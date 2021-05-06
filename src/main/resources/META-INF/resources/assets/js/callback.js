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
var iFrameEventsTargetOrigin = null;
var callbackUrl = null;

window.iFrameResizer = {
    readyCallback: function () {
        if ('parentIFrame' in window) {
            window.parentIFrame.sendMessage('sent', '*');
            window.parentIFrame.close();
        }
    }
}

function init(eventsTargetOrigin, callback) {
    iFrameEventsTargetOrigin = eventsTargetOrigin;
    callbackUrl = callback;
    if (callbackUrl) {
        setTimeout(() => doCallback(), 4000);
    }
}

function notify(message) {
    if (iFrameEventsTargetOrigin) {
        if ('parentIFrame' in window) {
            window.parentIFrame.sendMessage(message, iFrameEventsTargetOrigin);
        } else if (window.top && window.top !== window) {
            window.top.postMessage(message, iFrameEventsTargetOrigin);
        }
    }
}

function doCallback() {
    if (window.top === window || !iFrameEventsTargetOrigin) {
        window.location.assign(callbackUrl);
    } else {
        // If asked to notify parent do not redirect automatically, notify to allow top frame redirection
        notify('consent-callback/' + callbackUrl);
    }
}
