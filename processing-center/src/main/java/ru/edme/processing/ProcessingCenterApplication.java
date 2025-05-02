package ru.edme.processing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ProcessingCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProcessingCenterApplication.class, args);
    }

}
