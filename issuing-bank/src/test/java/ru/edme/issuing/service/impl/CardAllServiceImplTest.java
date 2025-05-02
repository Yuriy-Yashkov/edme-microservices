package ru.edme.issuing.service.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import ru.edme.issuing.dto.CardDto;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CardAllServiceImplTest {

    @Test
    void save() {
    }

    @Test
    void findById() {
    }

    @Test
    void findAll() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void testRedisSerialization() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("ru.edme.issuing.dto")
                .allowIfSubType("java.util")
                .allowIfSubType("java.time")
                .allowIfSubType("java.math")
                .build();

        mapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(mapper);

        CardDto original = CardDto.builder()
                .id(1L)
                .cardNumber("1234")
                .expirationDate(LocalDate.now())
                .build();

        byte[] bytes = serializer.serialize(original);
        Object deserialized = serializer.deserialize(bytes);

        assertEquals(original.getId(), ((CardDto) deserialized).getId());
    }

    @Test
    void testRedisSerializationOfCardDtoList() throws JsonProcessingException {
        // Настраиваем ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Полиморфная сериализация с @class
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("ru.edme.issuing.dto")
                .allowIfSubType("java.util")
                .allowIfSubType("java.time")
                .allowIfSubType("java.math")
                .build();
        mapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        // Создаём сериализатор
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(mapper);

        // Создаём список DTO
        List<CardDto> originalList = List.of(
                CardDto.builder().id(1L).cardNumber("1234").expirationDate(LocalDate.now()).build(),
                CardDto.builder().id(2L).cardNumber("5678").expirationDate(LocalDate.now().plusYears(1)).build()
        );

        // Сериализация в JSON-строку
        String json = mapper.writeValueAsString(originalList);

        // Десериализация
        List<CardDto> result = mapper.readValue(json, new TypeReference<List<CardDto>>() {
        });

        // Проверки
        assertNotNull(result);
        assertEquals(originalList.size(), result.size());
        assertEquals(originalList.get(0).getCardNumber(), result.get(0).getCardNumber());
    }

}