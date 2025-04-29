package ru.edme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SalesPointApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalesPointApplication.class, args);
    }

}
