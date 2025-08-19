package com.example.Foro_hub_alura.dto.topico;

import java.time.LocalDate;


public record DatosRespuestaTopico(
        Integer id,
        String titulo,
        String mensaje,
        LocalDate fechaCreacion,
        String estado,
        Integer autorId,
        Integer cursoId
) {}