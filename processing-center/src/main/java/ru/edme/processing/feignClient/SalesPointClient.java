package ru.edme.processing.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import ru.edme.dto.CardTransferDto;

@FeignClient(name = "sales-point")
public interface SalesPointClient {

    @PostMapping("/v1/sales-point/cards/transfer")
    void transferToSalesPoint(CardTransferDto cardTransferDto);
}
