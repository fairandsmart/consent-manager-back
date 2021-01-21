package com.fairandsmart.consent.serial;

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

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@QuarkusTest
public class SerialGeneratorTest {

    @Inject
    SerialGenerator generator;

    @Test
    public void testSimpleGeneration() throws SerialGeneratorException {
        String serial = generator.next("dadaduc");

        Assertions.assertNotNull(serial);
        Assertions.assertTrue(generator.isValid(serial));
        Assertions.assertEquals(1, generator.extract(serial));

        for (int i = 2; i < 200; i++) {
            serial = generator.next("dadaduc");
            Assertions.assertEquals(i, generator.extract(serial));
        }
    }

    @Test
    public void testMultiThreadGeneration() throws InterruptedException {
        List<Thread> workers = new ArrayList<>();
        List<String> serials = new ArrayList<>();
        try {
            for (int i = 0; i < 10; i++) {
                Thread thread = new Thread(new SerialConsumer(generator, serials));
                workers.add(thread);
            }
            for (Thread worker : workers) {
                worker.start();
            }
            boolean finished = false;
            while (!finished) {
                Thread.sleep(500);
                if (workers.stream().noneMatch(w -> w.isAlive())) {
                    finished = true;
                }
            }
            List<Long> generated = new ArrayList<>();
            for (String serial : serials) {
                Long value = generator.extract(serial);
                if (generated.contains(value)) {
                    Assertions.fail("duplicate serial value found: " + value);
                }
                generated.add(value);
            }
            Assertions.assertEquals(1000, generated.size());

        } catch (RuntimeException e) {
            e.printStackTrace();
            Assertions.fail(e.getMessage());
        }
    }

    static class SerialConsumer implements Runnable {

        private final SerialGenerator generator;
        private final Collection<String> serials;

        public SerialConsumer(SerialGenerator generator, Collection<String> serials) {
            this.generator = generator;
            this.serials = serials;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 100; i++) {
                    serials.add(generator.next("SerialTest"));
                }
            } catch (SerialGeneratorException e) {
                throw new RuntimeException("Unable to generate next serial");
            }
        }
    }

}
