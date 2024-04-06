package com.leo.parkapi.web.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioCreateDto {
    @NotBlank
    @Email(regexp = "^[a-z0-9.-]+@[a-z0-9.-]+\\.[a-z]{2,}$", message = "E-mail inválido.")
    private String username;

    @NotBlank
    @Size(min = 6, max = 6, message = "A senha deve ter 6 caracteres.")
    private String password;
}
