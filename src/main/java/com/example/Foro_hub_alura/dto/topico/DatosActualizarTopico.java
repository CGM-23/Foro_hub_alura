package com.example.Foro_hub_alura.dto.topico;

import jakarta.validation.constraints.Size;

public record DatosActualizarTopico(
        String titulo,
        String mensaje,
        String estado,
        Integer cursoId
) {}