package ru.edme.processing.feignClient;

import feign.FeignException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Flux;
import ru.edme.dto.CardTransferDto;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Import(SalesPointClientIntegrationTest.TestLoadBalancerConfig.class)//Импортирует в Spring-контекст конфигурацию, которая подменяет LoadBalancer (чтобы sales-point указывал на mockWebServer).
class SalesPointClientIntegrationTest {

    @Autowired
    private SalesPointClient salesPointClient;//Это мой @FeignClient(name = "sales-point") — прокси-интерфейс, который будет делать HTTP-запросы к sales-point.

    private static MockWebServer mockWebServer;//Это встроенный HTTP-сервер для тестов. Он будет принимать и проверять запросы вместо настоящего sales-point.

    @BeforeAll
    static void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(); //  Запускает HTTP-сервер на случайном порту.
    }

    @AfterAll
    static void shutdown() throws IOException {
        mockWebServer.shutdown(); // Завершает работу тестового HTTP-сервера после всех тестов.
    }

    @Test
    void shouldSendCard_whenResponseIs200() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200)); //Говорим mockWebServer: "когда к тебе придёт HTTP-запрос, ответь 200 OK без тела".

        CardTransferDto dto = new CardTransferDto(
                "4123450000000019",
                LocalDate.MIN,
                "IVAN I.IVANOV",
                1L,
                1L,
                1L,
                LocalDateTime.MIN,
                LocalDateTime.MIN
        );

        /**
         * Вызываем метод FeignClient. Он выполнит POST запрос на http://localhost:<порт>/v1/sales-point/cards/transfer.
         * через подмену LoadBalancer: @Import(...TestLoadBalancerConfig.class)
         */
        salesPointClient.transferToSalesPoint(dto);

        RecordedRequest request = mockWebServer.takeRequest(1, TimeUnit.SECONDS);//Ждём до 1 секунды, пока mockWebServer получит запрос.
        assertNotNull(request);
        assertEquals("POST", request.getMethod());
        assertEquals("/v1/sales-point/cards/transfer", request.getPath());
        assertTrue(request.getBody().readUtf8().contains("4123450000000019")); // ожидаемое значение
    }

    @Test
    void shouldFailWithoutRetry_onClientError_4xx() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse().setResponseCode(400)); // client error

        CardTransferDto dto = new CardTransferDto(
                "4123450000000019", LocalDate.MIN, "IVAN I.IVANOV",
                1L, 1L, 1L, LocalDateTime.MIN, LocalDateTime.MIN
        );

        // ожидаем исключение
        assertThrows(FeignException.BadRequest.class, () -> {
            salesPointClient.transferToSalesPoint(dto);
        });

        // проверим, что был только 1 запрос (без ретраев)
        assertEquals(1, mockWebServer.getRequestCount());
    }

    @Test
    void shouldRetry_onServerError_5xx() {
        // 2 ошибки 500 подряд — симулируем падение сервиса
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        CardTransferDto dto = new CardTransferDto(
                "4123450000000019", LocalDate.MIN, "IVAN I.IVANOV",
                1L, 1L, 1L, LocalDateTime.MIN, LocalDateTime.MIN
        );

        assertThrows(FeignException.InternalServerError.class, () -> {
            salesPointClient.transferToSalesPoint(dto);
        });

        // ожидаем 2 запроса (1 основной + 1 retry)
        assertEquals(2, mockWebServer.getRequestCount());
    }

    @Test
    void shouldTimeout_whenServerDoesNotRespondInTime() {
        // Задержка ответа — симулируем зависший сервис
        mockWebServer.enqueue(new MockResponse()
                .setBody("OK")
                .setBodyDelay(5, TimeUnit.SECONDS) // задержка
                .setResponseCode(200)
        );

        CardTransferDto dto = new CardTransferDto(
                "4123450000000019", LocalDate.MIN, "IVAN I.IVANOV",
                1L, 1L, 1L, LocalDateTime.MIN, LocalDateTime.MIN
        );

        // Обычно нужен кастомный timeout в application-test.yml или конфигурации Feign
        assertThrows(FeignException.class, () -> {
            salesPointClient.transferToSalesPoint(dto);
        });

        // 1 запрос был, но он завис
        assertEquals(3, mockWebServer.getRequestCount());
    }


    @TestConfiguration
    static class TestLoadBalancerConfig {

        @Bean
        public ServiceInstanceListSupplier serviceInstanceListSupplier() {
            return new ServiceInstanceListSupplier() {
                @Override
                public String getServiceId() {
                    return "sales-point";
                }

                @Override //Мы подменяем поведение Eureka: говорим, что "sales-point" — это localhost:порт, где работает mockWebServer.
                public Flux<List<ServiceInstance>> get() {
                    return Flux.just(List.of(new DefaultServiceInstance(
                            "sales-point-1", getServiceId(), "localhost", mockWebServer.getPort(), false
                    )));
                }
            };
        }
    }
}
