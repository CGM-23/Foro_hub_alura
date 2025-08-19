package com.example.Foro_hub_alura.dto.usuario;

import jakarta.validation.constraints.*;

public record DatosActualizarUsuario(
        @NotNull
        Integer id,
        String nombre,
        @Email
        String email,
        String contrasena   ) {
}