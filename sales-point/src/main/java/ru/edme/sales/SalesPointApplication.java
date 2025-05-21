package ru.edme.sales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import ru.edme.security.SecurityAutoConfiguration;

@EnableFeignClients
@EnableMethodSecurity
@EnableDiscoveryClient
@SpringBootApplication
@Import(SecurityAutoConfiguration.class)
public class SalesPointApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalesPointApplication.class, args);
    }
}
