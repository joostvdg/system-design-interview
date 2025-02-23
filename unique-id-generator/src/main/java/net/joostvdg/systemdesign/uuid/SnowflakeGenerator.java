package net.joostvdg.systemdesign.uuid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SnowflakeGenerator  {

    private static final Logger logger = LoggerFactory.getLogger(SnowflakeGenerator.class);

    private short sequence = 0;
    private boolean alive = true;
    private Future<?> sequenceService;
    private ExecutorService service;

    public SnowflakeGenerator() {
        service = Executors.newSingleThreadExecutor();
    }


    public void start() {
        logger.info("Starting SnowflakeGenerator - sequence service");
        sequenceService = service.submit(() -> {
            var now = Instant.now();
            while (alive) {
                sequence++;
                if (sequence > 4095) {
                    sequence = 0;
                }
                if (Instant.now().isAfter(now.plus(1, ChronoUnit.MILLIS))) {
                    logger.info("Sequence: {}", sequence);
                    now = Instant.now();
                }
                if (Thread.interrupted()) {
                    logger.info("Thread interrupted, shutting down sequence service");
                    alive = false;
                }
            }
        });
    }

    public void shutdown() {
        logger.info("Shutting down SnowflakeGenerator");
        alive = false;
        sequenceService.cancel(true);
        service.shutdown();
        service.shutdownNow();
    }


    public TSID generate(int machineId, int dataCenterId) {
        logger.info("Generating new TSID with machineId: {} and dataCenterId: {}", machineId, dataCenterId);
        SnowflakeId snowflakeId = new SnowflakeId();

        snowflakeId.setSequence(sequence);
        snowflakeId.setMachineId(machineId);
        snowflakeId.setDataCenterId(dataCenterId);
        snowflakeId.setMillisecondSequence();

        long id = snowflakeId.getId();
        String binaryStringOfId = snowflakeId.toBinaryString();
        System.out.println("Generated ID: " + binaryStringOfId);
        System.out.println("Generated ID: " + id);
        return new TSID(id);
    }

}
