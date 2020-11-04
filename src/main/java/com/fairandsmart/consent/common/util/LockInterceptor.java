package com.fairandsmart.consent.common.util;

/*-
 * #%L
 * Right Consent / A Consent Manager Platform
 * 
 * Authors:
 * 
 * Xavier Lefevre <xavier.lefevre@fairandsmart.com> / FairAndSmart
 * Nicolas Rueff <nicolas.rueff@fairandsmart.com> / FairAndSmart
 * Jérôme Blanchard <jerome.blanchard@fairandsmart.com> / FairAndSmart
 * Alan Balbo <alan.balbo@fairandsmart.com> / FairAndSmart
 * Frederic Pierre <frederic.pierre@fairansmart.com> / FairAndSmart
 * Victor Guillaume <victor.guillaume@fairandsmart.com> / FairAndSmart
 * Manon Stremplewski <manon.stremplewski@fairandsmart.com> / FairAndSmart
 * Pauline Kullmann <pauline.kullmmann@fairandsmart.com> / FairAndSmart
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
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
