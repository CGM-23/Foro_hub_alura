package com.example.Foro_hub_alura.dto.usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
public record DatosLoginUsuario(
        @NotBlank
        @Email
        String email,
        @NotBlank
        String contrasena
) {}