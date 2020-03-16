package com.fairandsmart.consent.common.util;

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
