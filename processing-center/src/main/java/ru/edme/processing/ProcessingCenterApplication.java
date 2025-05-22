package ru.edme.processing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import ru.edme.security.SecurityAutoConfiguration;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@Import(SecurityAutoConfiguration.class)
public class ProcessingCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProcessingCenterApplication.class, args);
    }
}
