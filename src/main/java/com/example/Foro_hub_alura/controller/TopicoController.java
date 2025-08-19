package com.example.Foro_hub_alura.controller;

import com.example.Foro_hub_alura.dto.topico.DatosActualizarTopico;
import com.example.Foro_hub_alura.dto.topico.DatosTopicoView;
import com.example.Foro_hub_alura.dto.topico.DatosRegistroTopico;
import com.example.Foro_hub_alura.service.TopicoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping({"/topicos"})
public class TopicoController {

    private final TopicoService service;

    public TopicoController(TopicoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody @Valid DatosRegistroTopico dto,
                                       UriComponentsBuilder uri) {
        try {
            var resp = service.crear(dto);
            URI location = uri.path("/topicos/{id}").buildAndExpand(resp.id()).toUri();
            return ResponseEntity.created(location).body(resp);
        } catch (IllegalStateException e) {
            if ("topico_duplicado".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ErrorResponse("duplicate", "Ya existe un tópico con el mismo título y mensaje"));
            }
            return ResponseEntity.badRequest().body(new ErrorResponse("invalid_state", e.getMessage()));
        } catch (IllegalArgumentException e) {
            String msg = e.getMessage();
            if ("autor_no_encontrado".equals(msg)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("author_not_found", "Autor no encontrado"));
            } else if ("curso_no_encontrado".equals(msg)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("course_not_found", "Curso no encontrado"));
            }
            return ResponseEntity.badRequest().body(new ErrorResponse("invalid_argument", msg));
        }
    }

    @GetMapping
    public ResponseEntity<Page<DatosTopicoView>> listar(
            @RequestParam(required = false) String curso,
            @RequestParam(required = false, name = "anio") Integer anio,
            @PageableDefault(size = 10) Pageable pageable) {

        var page = service.listar(curso, anio, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(service.detalle(id));
        } catch (IllegalArgumentException e) {
            if ("topico_no_encontrado".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("not_found", "Tópico no encontrado"));
            }
            return ResponseEntity.badRequest().body(new ErrorResponse("invalid_argument", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id,
                                        @RequestBody @Valid DatosActualizarTopico dto) {
        try {
            var resp = service.actualizar(id, dto);
            return ResponseEntity.ok(resp);
        } catch (IllegalArgumentException e) {
            String msg = e.getMessage();
            if ("topico_no_encontrado".equals(msg)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("not_found", "Tópico no encontrado"));
            } else if ("curso_no_encontrado".equals(msg)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("course_not_found", "Curso no encontrado"));
            } else if ("estado_invalido".equals(msg)) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("invalid_state", "Estado inválido. Usa: OPEN, CLOSED o ARCHIVED"));
            }
            return ResponseEntity.badRequest().body(new ErrorResponse("invalid_argument", msg));
        } catch (IllegalStateException e) {
            if ("topico_duplicado".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ErrorResponse("duplicate", "Ya existe un tópico con el mismo título y mensaje"));
            }
            return ResponseEntity.badRequest().body(new ErrorResponse("invalid_state", e.getMessage()));
        }
    }

    public record ErrorResponse(String error, String message) {}
}
