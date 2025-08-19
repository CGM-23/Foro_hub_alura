package com.example.Foro_hub_alura.controller;

import com.example.Foro_hub_alura.dto.usuario.*;
import com.example.Foro_hub_alura.modelos.Usuario;
import com.example.Foro_hub_alura.repository.UsuarioRepository;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioRepository usuarioRepository,
                             PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> registrar(@RequestBody @Valid DatosRegistroUsuario datosRegistro,
                                       UriComponentsBuilder uri) {

        String email = datosRegistro.email().trim().toLowerCase();

        if (usuarioRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("email_in_use", "El email ya está registrado"));
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(datosRegistro.nombre());
        usuario.setEmail(email);
        usuario.setContrasena(passwordEncoder.encode(datosRegistro.contrasena()));
        // tipo queda "USER" por defecto en la entidad

        usuario = usuarioRepository.save(usuario);

        URI url = uri.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(url).body(new DatosRespuestaUsuario(usuario));
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoUsuario>> listar(@PageableDefault(size = 10) Pageable paginacion) {
        Page<DatosListadoUsuario> page = usuarioRepository.findAll(paginacion)
                .map(DatosListadoUsuario::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> retornaDatos(@PathVariable Integer id) {
        return usuarioRepository.findById(id)
                .<ResponseEntity<?>>map(u -> ResponseEntity.ok(new DatosRespuestaUsuario(u)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("not_found", "Usuario no encontrado")));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<?> actualizar(@RequestBody @Valid DatosActualizarUsuario datosActualizar) {
        if (datosActualizar.id() == null) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("invalid_request", "El id es obligatorio"));
        }

        var opt = usuarioRepository.findById(datosActualizar.id());
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("not_found", "Usuario no encontrado"));
        }

        Usuario usuario = opt.get();

        if (datosActualizar.nombre() != null) {
            usuario.setNombre(datosActualizar.nombre());
        }

        if (datosActualizar.email() != null) {
            String nuevoEmail = datosActualizar.email().trim().toLowerCase();
            if (!nuevoEmail.equals(usuario.getEmail())
                    && usuarioRepository.findByEmail(nuevoEmail).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ErrorResponse("email_in_use", "El email ya está registrado"));
            }
            usuario.setEmail(nuevoEmail);
        }

        if (datosActualizar.contrasena() != null) {
            usuario.setContrasena(passwordEncoder.encode(datosActualizar.contrasena()));
        }

        return ResponseEntity.ok(new DatosRespuestaUsuario(usuario));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("not_found", "Usuario no encontrado"));
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public record ErrorResponse(String error, String message) {}
}
