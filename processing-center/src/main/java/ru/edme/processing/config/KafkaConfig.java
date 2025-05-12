package ru.edme.processing.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.TopicBuilder;

import java.util.Map;

//@Component todo Отключил, что бы создать Топик из 1-го сервиса
public class KafkaConfig {

    @Value("${spring.kafka.topics.card-transfer-to-issuing}")
    private String topic;

    //    @Bean
    NewTopic createTopic() {
        return TopicBuilder
                .name(topic)
                .partitions(1)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }
}
