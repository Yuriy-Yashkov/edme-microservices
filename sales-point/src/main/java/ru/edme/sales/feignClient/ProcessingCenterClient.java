package ru.edme.sales.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.edme.dto.CardTransferDto;

@FeignClient(name = "processing-center")
public interface ProcessingCenterClient {

    @PostMapping("/v1/processing-center/cards/transfer")
    void transferToProcessingCenter(@RequestBody CardTransferDto cardTransferDto);
}
