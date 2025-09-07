package ru.edme.issuing.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.topics.card-transfer-to-processing}")
    private String topicToProcessing;

    @Value("${spring.kafka.topics.card-transfer-to-issuing}")
    private String topicToIssuing;

    @Bean
    NewTopic createTopicToProcessing() {
        return TopicBuilder
                .name(topicToProcessing)
                .partitions(1)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }

    @Bean
    NewTopic createTopicToIssuing() {
        return TopicBuilder
                .name(topicToIssuing)
                .partitions(1)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }
}
