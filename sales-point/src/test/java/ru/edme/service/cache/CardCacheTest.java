package ru.edme.service.cache;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import ru.edme.dto.requestDTO.CardRequestDTO;
import ru.edme.mapper.CardMapper;
import ru.edme.model.Card;
import ru.edme.repository.CardRepository;
import ru.edme.service.CardAllService;
import ru.edme.util.TestData;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@SpringBootTest
@ActiveProfiles("test")
@WithMockUser(username = "yriy", password = "123", authorities = "ADMIN")
public class CardCacheTest {

    @Autowired
    private CardAllService cardAllService;

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

        cardAllService.findById(card.getId());

        // 2-й вызов — должен взять из кэша, findById НЕ должен вызываться
        cardAllService.findById(card.getId());

        Mockito.verify(cardRepository, times(1)).findById(card.getId());
    }

    @Test
    void testSaveShouldUpdateCache() {
        CardRequestDTO requestDTO = data.cardRequestDTOId;
        requestDTO.setId(requestDTO.getId() + 1);
        Card card = cardMapper.toCard(requestDTO);

        Mockito.when(cardRepository.save(Mockito.any()))
                .thenReturn(card);

        cardAllService.save(requestDTO);

        // Теперь должен быть кэширован
        cardAllService.findById(requestDTO.getId());
        Mockito.verify(cardRepository, never()).findById(requestDTO.getId());
    }

    @Test
    void testDeleteShouldEvictCache() {
        Card card = data.card;
        card.setId(card.getId() + 2);

        Mockito.when(cardRepository.findById(card.getId()))
                .thenReturn(Optional.of(card));

        boolean deleted = cardAllService.delete(card.getId());
        assertTrue(deleted);

        // После удаления вызов findById должен снова обратиться в репозиторий
        cardAllService.findById(card.getId());
        Mockito.verify(cardRepository, times(2)).findById(card.getId());
    }
}
