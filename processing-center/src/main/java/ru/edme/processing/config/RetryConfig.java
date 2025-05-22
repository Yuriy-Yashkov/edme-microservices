package ru.edme.processing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import ru.edme.processing.exception.EmptyResponseException;
import ru.edme.processing.exception.ServerErrorException;

@Component
@EnableRetry
public class RetryConfig {

    @Bean
    public RetryTemplate retryTemplate() {
        return RetryTemplate.builder()
                .maxAttempts(3) // количество повторов
                .fixedBackoff(1000) // задержка между повторами (мс)
                .retryOn(ServerErrorException.class)
                .retryOn(EmptyResponseException.class)
                .build();
    }
}
