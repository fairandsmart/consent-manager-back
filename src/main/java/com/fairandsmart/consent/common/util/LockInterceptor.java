package com.fairandsmart.consent.common.util;

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

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

@Lock
@Interceptor
public class LockInterceptor {

    private static final Logger LOGGER = Logger.getLogger(LockInterceptor.class.getName());
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    @AroundInvoke
    public Object concurrencyControl(InvocationContext ctx) throws Exception {
        Lock lockAnnotation = ctx.getMethod().getAnnotation(Lock.class);

        if (lockAnnotation == null) {
            lockAnnotation = ctx.getTarget().getClass().getAnnotation(Lock.class);
        }

        Object returnValue = null;
        switch (lockAnnotation.value()) {
            case WRITE:
                ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
                try {
                    LOGGER.log(Level.FINEST, "Acquiring WRITE lock");
                    writeLock.lock();
                    returnValue = ctx.proceed();
                } finally {
                    writeLock.unlock();
                    LOGGER.log(Level.FINEST, "Lock released");
                }
                break;
            case READ:
                ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
                try {
                    LOGGER.log(Level.FINEST, "Acquiring READ lock");
                    readLock.lock();
                    returnValue = ctx.proceed();
                } finally {
                    readLock.unlock();
                    LOGGER.log(Level.FINEST, "Lock released");
                }
                break;
        }
        return returnValue;
    }
}
