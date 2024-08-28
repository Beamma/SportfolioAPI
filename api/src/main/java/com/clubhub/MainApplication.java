package com.clubhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class MainApplication {

    /**
     * Main entry point, runs the Spring application
     * @param args command line arguments
     */

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

}
