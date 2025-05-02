package ru.edme.sales.service.cache;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import ru.edme.sales.dto.requestDto.CardRequestDto;
import ru.edme.sales.mapper.CardMapper;
import ru.edme.sales.model.Card;
import ru.edme.sales.repository.CardRepository;
import ru.edme.sales.service.CardService;
import ru.edme.sales.util.TestData;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@SpringBootTest
@ActiveProfiles("test")
@WithMockUser(username = "yriy", password = "123", authorities = "ADMIN")
public class CardCacheTest {

    @Autowired
    private CardService cardService;

    @MockBean
    private CardRepository cardRepository;
    private static TestData data;

    @Autowired
    private CardMapper cardMapper;

    @BeforeAll
    static void setUpBeforeAll() {
        data = new TestData();
    }

    @Test
    void testFindByIdShouldUseCache() {
        Card card = data.cardId;

        // 1-й вызов — должен пойти в репозиторий
        Mockito.when(cardRepository.findById(card.getId()))
                .thenReturn(Optional.of(card));

        cardService.findById(card.getId());

        // 2-й вызов — должен взять из кэша, findById НЕ должен вызываться
        cardService.findById(card.getId());

        Mockito.verify(cardRepository, times(1)).findById(card.getId());
    }

    @Test
    void testSaveShouldUpdateCache() {
        CardRequestDto requestDTO = data.cardRequestDtoId;
        requestDTO.setId(requestDTO.getId() + 1);
        Card card = cardMapper.toCard(requestDTO);

        Mockito.when(cardRepository.save(Mockito.any()))
                .thenReturn(card);

        cardService.save(requestDTO);

        // Теперь должен быть кэширован
        cardService.findById(requestDTO.getId());
        Mockito.verify(cardRepository, never()).findById(requestDTO.getId());
    }

    @Test
    void testDeleteShouldEvictCache() {
        Card card = data.card;
        card.setId(card.getId() + 2);

        Mockito.when(cardRepository.findById(card.getId()))
                .thenReturn(Optional.of(card));

        boolean deleted = cardService.delete(card.getId());
        assertTrue(deleted);

        // После удаления вызов findById должен снова обратиться в репозиторий
        cardService.findById(card.getId());
        Mockito.verify(cardRepository, times(2)).findById(card.getId());
    }
}
