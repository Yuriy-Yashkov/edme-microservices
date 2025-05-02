package ru.edme.sales.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edme.sales.model.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String login;
    private String fullName;
    private String password;
    private Role role;
}

