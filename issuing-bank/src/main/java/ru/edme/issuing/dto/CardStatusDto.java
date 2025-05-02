package ru.edme.issuing.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardStatusDto {

    private long id;

    @Size(max = 255)
    private String cardStatusName;
}
