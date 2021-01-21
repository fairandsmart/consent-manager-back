package com.fairandsmart.consent.common.util;

/*-
 * #%L
 * Right Consents / A Consent Manager Platform
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * You accept that the Program was not created with a view to satisfy Your
 * individual requirements. Therefore, you must ensure that the Program
 * comply with Your needs, requirements and constraints. FAIR AND SMART
 * represents and warrants that it holds, without any restriction or
 * reservation, all the legal titles, authorizations and intellectual
 * property rights granted in the context of the GPLv3 License. See the
 * Additional Terms for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <https://www.gnu.org/licenses/>.
 * 
 * You should have received a copy of the Additional Terms along with this
 * program. If not, see <https://www.fairandsmart.com/opensource/>.
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
