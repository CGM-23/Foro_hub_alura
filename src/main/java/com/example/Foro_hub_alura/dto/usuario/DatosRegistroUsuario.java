package com.example.Foro_hub_alura.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DatosRegistroUsuario(
        @NotBlank
        String nombre,
        @NotBlank @Email
        String email,
        @NotBlank
        String contrasena
) {}
