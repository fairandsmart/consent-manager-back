package com.fairandsmart.consent.serial;

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

        for ( int i=2; i<200; i++ ) {
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
            for ( Thread worker: workers ) {
                worker.start();
            }
            for ( Thread worker: workers ) {
                worker.join();
            }
            List<Long> generated = new ArrayList<>();
            for ( String serial : serials ) {
                Long value = generator.extract(serial);
                if ( generated.contains(value) ) {
                    Assertions.fail("duplicate serial value found: " + value);
                }
                generated.add(value);
            }
            Assertions.assertEquals(10000, generated.size());

        } catch (RuntimeException e ) {
            e.printStackTrace();
            Assertions.fail(e.getMessage());
        }
    }

    class SerialConsumer implements Runnable {

        private SerialGenerator generator;
        private Collection<String> serials;

        public SerialConsumer(SerialGenerator generator, Collection<String> serials) {
            this.generator = generator;
            this.serials = serials;
        }

        @Override
        public void run() {
            try {
                for ( int i=0; i<1000; i++ ){
                    serials.add(generator.next("SerialTest"));
                }
            } catch (SerialGeneratorException e) {
                throw new RuntimeException("Unable to generate next serial");
            }
        }
    }



}
