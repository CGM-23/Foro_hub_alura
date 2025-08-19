package com.example.Foro_hub_alura.dto.usuario;

import com.example.Foro_hub_alura.modelos.Usuario;

public record DatosRespuestaUsuario(
        Integer id,
        String nombre,
        String email
) {
    public DatosRespuestaUsuario(Usuario u) {
        this(u.getId(), u.getNombre(), u.getEmail());
    }
}
