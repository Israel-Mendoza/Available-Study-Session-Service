package dev.artisra.availablesessions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AvailableSessionsApplication implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(AvailableSessionsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AvailableSessionsApplication.class, args);
    }

    @Override
    public void run() {
        logger.warn("AvailableSessionsApplication started successfully.");
    }
}
