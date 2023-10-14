package org.bitpioneers;

import lombok.SneakyThrows;
import org.bitpioneers.types.PersonType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.http.HttpRequest;
import java.util.Random;

@EnableScheduling
@ConfigurationPropertiesScan
@SpringBootApplication
public class ApplicationRunner {
    @SneakyThrows
    public static void main(String[] args) {
        SpringApplication.run(ApplicationRunner.class, args);
//        Random random = new Random(60);
//        System.out.println(PersonType.INDIVIDUAL.ordinal());
//        while (true){
//            Thread.sleep(1000);
//            System.out.println(random.nextInt(1, 60));
//        }
    }
}