package ru.edme.processing.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KafkaConfig {

    @Value("${spring.kafka.topics.card-transfer-to-issuing}")
    private String topic;

    @Bean
    NewTopic createTopic() {
        return TopicBuilder
                .name(topic)
                .partitions(1)
                .replicas(1)
                .configs(Map.of("min.insync.replicas", "1"))
                .build();
    }
}
