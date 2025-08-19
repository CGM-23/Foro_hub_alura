package com.example.Foro_hub_alura.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "cursos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Curso {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String categoria;
}