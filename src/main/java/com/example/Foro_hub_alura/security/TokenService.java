package com.example.Foro_hub_alura.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.Foro_hub_alura.modelos.Usuario;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {

    private static final String SECRET = "123456";
    private static final String ISSUER = "foro-alura";
    private final Algorithm algorithm = Algorithm.HMAC256(SECRET);

    public String generarToken(Usuario usuario) {
        try {
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(usuario.getEmail())
                    .withClaim("id", usuario.getId())
                    .withExpiresAt(expiraEnHoras(24))
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new IllegalStateException("No se pudo crear el token JWT", e);
        }
    }

    public String getSubject(String token) {
        try {
            DecodedJWT decoded = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token);
            return decoded.getSubject();
        } catch (JWTVerificationException e) {
            return null; // inválido/expirado
        }
    }

    private Instant expiraEnHoras(int horas) {
        return Instant.now().plusSeconds(horas * 3600L);
    }
}
