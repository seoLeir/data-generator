package org.bitpioneers;

import lombok.SneakyThrows;
import org.bitpioneers.types.PersonType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@ConfigurationPropertiesScan
@SpringBootApplication
public class ApplicationRunner {
    @SneakyThrows
    public static void main(String[] args) {
        SpringApplication.run(ApplicationRunner.class, args);
    }
}