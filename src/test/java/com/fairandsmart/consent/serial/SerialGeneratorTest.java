package com.fairandsmart.consent.serial;

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
