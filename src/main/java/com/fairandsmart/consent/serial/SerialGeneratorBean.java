package com.fairandsmart.consent.serial;

/*-
 * #%L
 * Right Consents Community Edition
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

import com.fairandsmart.consent.common.config.SerialConfig;
import com.fairandsmart.consent.common.util.Base58;
import com.fairandsmart.consent.common.util.Lock;
import com.fairandsmart.consent.common.util.LockType;
import com.fairandsmart.consent.serial.entity.Sequence;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class SerialGeneratorBean implements SerialGenerator {

    private static final Logger LOGGER = Logger.getLogger(SerialGenerator.class.getName());
    private static final Random random = new Random();

    @Inject
    protected SerialConfig config;

    private static final Map<String, Sequence> pools = new HashMap<>();

    @Override
    @Lock(LockType.WRITE)
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public String next(String name) throws SerialGeneratorException {
        LOGGER.log(Level.FINE,"Generating next serial for name: " + name);
        long value = generate(name);
        return config.prefix() + valueToSerial(value);
    }

    @Override
    public long extract(String serial) {
        LOGGER.log(Level.FINE,"Retrieving value for serial: " + serial);
        if ( !isValid(serial) ) {
            throw new IllegalStateException("Serial is invalid");
        }
        byte[] bserial = Base58.decode(serial.substring(config.prefix().length()));
        if ( bserial.length == 4 ) {
            return ByteBuffer.wrap(bserial, 1, bserial.length - 2).getShort();
        }
        if ( bserial.length == 6 ) {
            return ByteBuffer.wrap(bserial, 1, bserial.length - 2).getInt();
        }
        return ByteBuffer.wrap(bserial, 1, bserial.length - 2).getLong();
    }

    @Override
    public boolean isValid(String serial) {
        LOGGER.log(Level.FINE,"Checking if serial is valid: " + serial);
        if ( config.prefix() != null && !serial.startsWith(config.prefix()) ) {
            return false;
        }
        byte[] bserial;
        if ( config.prefix() != null ) {
            bserial = Base58.decode(serial.substring(config.prefix().length()));
        } else {
            bserial = Base58.decode(serial);
        }
        if (bserial.length != 4 && bserial.length != 6 && bserial.length != 10) {
            return false;
        }
        return checksum(bserial) == bserial[bserial.length - 1];
    }

    private static String valueToSerial(long value) {
        if ( value < 0 ) {
            throw new IllegalStateException("Id can't be negative");
        }
        byte[] rnd = new byte[1];
        random.nextBytes(rnd);
        ByteBuffer buffer;
        if ( value <= Short.MAX_VALUE ) {
            buffer = ByteBuffer.allocate(4).put(rnd).putShort((short)value);
        } else if ( value <= Integer.MAX_VALUE ) {
            buffer = ByteBuffer.allocate(6).put(rnd).putInt((int)value);
        } else {
            buffer = ByteBuffer.allocate(10).put(rnd).putLong(value);
        }
        buffer.put(buffer.limit()-1, checksum(buffer.array()));
        return Base58.encode(buffer.array());
    }

    private static byte checksum(byte[] serial) {
        byte checksum = (byte) 0;
        for ( int i=0; i< serial.length-1; i++ ) {
            checksum = (byte) (checksum & serial[i]);
        }
        return checksum;
    }

    private long generate(String name) throws SerialGeneratorException {
        try {
            Sequence pool = pools.get(name);
            if (pool == null) {
                pool = Sequence.findById(name, LockModeType.PESSIMISTIC_WRITE);
                if (pool == null) {
                    LOGGER.log(Level.INFO, "Sequence not found, initialising with value: " + config.slotInitialValue());
                    pool = new Sequence();
                    pool.name = name;
                    pool.next = config.slotInitialValue();
                } else {
                    pool.next = pool.value;
                }
                pool.value = pool.next + config.slotCapacity();
                pool.persistAndFlush();
                pools.put(pool.name, pool);
            }
            if (pool.next == pool.value) {
                LOGGER.log(Level.INFO, "Pool empty, renewing...");
                pool = Sequence.findById(name, LockModeType.PESSIMISTIC_WRITE);
                pool.next = pool.value;
                pool.value = pool.next + config.slotCapacity();
                pool.persistAndFlush();
                pools.put(pool.name, pool);
            }
            pool.next++;
            return pool.next;
        } catch ( RuntimeException e ) {
            throw new SerialGeneratorException("Unable to generate new serial", e);
        }
    }

}
