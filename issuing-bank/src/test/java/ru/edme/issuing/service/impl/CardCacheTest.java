package ru.edme.issuing.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import ru.edme.issuing.dto.CardDto;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CardCacheTest {

    @Test
    void testRedisSerializationOfCardDtoList() throws IOException {
        ObjectMapper mapper = new ObjectMapper().deactivateDefaultTyping();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        GenericJackson2JsonRedisSerializer serializer =
                new GenericJackson2JsonRedisSerializer(mapper);

        // given
        List<CardDto> originalList = List.of(
                CardDto.builder().id(1L).cardNumber("1234").expirationDate(LocalDate.now()).build(),
                CardDto.builder().id(2L).cardNumber("5678").expirationDate(LocalDate.now().plusYears(1)).build()
        );

        // when
        byte[] serialized = serializer.serialize(originalList);
        List<CardDto> result = mapper.readValue(serialized, new TypeReference<List<CardDto>>() {
        });

        // then
        assertNotNull(result);
        assertEquals(originalList.size(), result.size());
        assertEquals(originalList.get(0).getCardNumber(), result.get(0).getCardNumber());
    }

    @Test
    void testRedisSerializationOfSingleCardDto() throws Exception {
        ObjectMapper mapper = new ObjectMapper().deactivateDefaultTyping();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // given
        CardDto card = CardDto.builder().id(1L).cardNumber("1234").expirationDate(LocalDate.now()).build();

        GenericJackson2JsonRedisSerializer serializer =
                new GenericJackson2JsonRedisSerializer(mapper);

        // when
        byte[] serialized = serializer.serialize(card);
        CardDto deserialized = mapper.readValue(serialized, CardDto.class);

        // then
        assertThat(deserialized).isEqualTo(card);
    }
}
