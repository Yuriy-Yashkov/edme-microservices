package ru.edme.sales.config;

import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Конфигурационный компонент Spring, настраивающий экспорт трассировок (спанов)
 * в систему сбора телеметрии (например, Jaeger) через OTLP по gRPC.
 * <p>
 * Используется библиотека OpenTelemetry для экспорта данных о распределённых трассировках.
 */
@Component
public class JaegerConfig {

    /**
     * Создаёт бин {@link OtlpGrpcSpanExporter}, который используется OpenTelemetry
     * из spring-actuator.
     * для экспорта спанов в OTLP-совместимый backend (например, Jaeger Collector).
     * <p>
     * Конечная точка (endpoint) задаётся через конфигурационный параметр `tracing.url`
     * в `application.yml` или `application.properties`.
     *
     * @param url URL-адрес OTLP gRPC-сервера (например, http://localhost:4317).
     * @return сконфигурированный {@link OtlpGrpcSpanExporter} для экспорта спанов.
     */
    @Bean // авто-настройка перехвата запросов
    public OtlpGrpcSpanExporter otlpGrpcSpanExporter(@Value("${tracing.url}") String url) {
        return OtlpGrpcSpanExporter.builder().setEndpoint(url).build();
    }
}
