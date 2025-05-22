package ru.edme.issuing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import ru.edme.security.SecurityAutoConfiguration;

@EnableDiscoveryClient
@SpringBootApplication
@Import(SecurityAutoConfiguration.class)
public class IssuingBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(IssuingBankApplication.class, args);
    }
}