package ru.edme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edme.util.RegexPatterns;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {

    private long id;

    @NotBlank
    @Size(max = 100)
    private String lastName;

    @NotBlank
    @Size(max = 100)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    private String middleName;
    private LocalDate birthDate;

    @Size(max = 255)
    private String document;

    @Size(max = 255)
    private String address;

    @Size(max = 20, message = "Номер не должен превышать 20 символов")
    private String phone;

    @Size(max = 255)
    @Pattern(regexp = RegexPatterns.SIMPLE_EMAIL)
    private String email;
}
