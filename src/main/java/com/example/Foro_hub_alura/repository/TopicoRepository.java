package com.example.Foro_hub_alura.repository;

import com.example.Foro_hub_alura.modelos.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface TopicoRepository extends JpaRepository<Topico, Integer> {
    boolean existsByTituloIgnoreCaseAndMensajeIgnoreCase(String titulo, String mensaje);

    boolean existsByTituloIgnoreCaseAndMensajeIgnoreCaseAndIdNot(String titulo, String mensaje, Integer id);

    Page<Topico> findByCurso_NombreIgnoreCase(String nombre, Pageable pageable);

    Page<Topico> findByFechaCreacionBetween(LocalDate start, LocalDate end, Pageable pageable);

    Page<Topico> findByCurso_NombreIgnoreCaseAndFechaCreacionBetween(String nombre, LocalDate start, LocalDate end, Pageable pageable);
}