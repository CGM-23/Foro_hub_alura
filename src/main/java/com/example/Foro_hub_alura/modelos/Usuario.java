package com.example.Foro_hub_alura.modelos;

import com.example.Foro_hub_alura.dto.usuario.DatosActualizarUsuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Table(name = "usuarios")
@Entity(name = "Usuario")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // DB: INT

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @JsonIgnore
    @Column(nullable = false, length = 100)
    private String contrasena;

    @Column(nullable = false, length = 30)
    private String tipo = "USER";

    // ---- Actualización parcial desde DTO ----
    public void actualizarDatos(DatosActualizarUsuario d) {
        if (d.nombre() != null) this.nombre = d.nombre();
        if (d.email() != null) this.email = d.email();
        if (d.contrasena() != null) this.contrasena = d.contrasena();
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    // ---- Spring Security (sin roles) ----
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return contrasena;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
