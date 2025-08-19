package com.example.Foro_hub_alura.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity @Table(name = "topicos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topico {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(nullable = false, length = 350)
    private String mensaje;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;

    @Column(nullable = false, length = 30)
    private String estado = "OPEN";

    @ManyToOne(optional = false)
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;
}