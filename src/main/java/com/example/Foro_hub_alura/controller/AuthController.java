package com.example.Foro_hub_alura.controller;

import com.example.Foro_hub_alura.dto.usuario.DatosLoginUsuario;
import com.example.Foro_hub_alura.modelos.Usuario;
import com.example.Foro_hub_alura.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthenticationManager authManager;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authManager, TokenService tokenService) {
        this.authManager = authManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid DatosLoginUsuario req) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(req.email(), req.contrasena());
            var authentication = authManager.authenticate(authToken);
            var usuario = (Usuario) authentication.getPrincipal();
            var jwt = tokenService.generarToken(usuario);
            return ResponseEntity.ok(new TokenResponse(jwt, "Bearer"));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("invalid_credentials", "Email o contraseña incorrectos"));
        }
    }

    public record TokenResponse(String access_token, String token_type) {}
    public record ErrorResponse(String error, String message) {}
}