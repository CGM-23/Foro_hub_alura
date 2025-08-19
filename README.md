# Foro Hub Alura – Backend (Spring Boot)

## Índice
1. **[Descripción del proyecto](#1-descripción-del-proyecto)**
2. **[Tecnologías y arquitectura](#2-tecnologías-y-arquitectura)**
3. **[Configuración y ejecución](#3-configuración-y-ejecución)**
4. **[Uso con Postman (endpoints principales)](#4-uso-con-postman-endpoints-principales)**

---

## 1) Descripción del proyecto
API REST de un **foro** (solo backend) con **Spring Boot 3 / Java 21**.  
Incluye autenticación **JWT**, CRUD de **usuarios** y **tópicos** (respuestas más adelante). Se consume desde **Postman**.

---

## 2) Tecnologías y arquitectura
- **Java 21**, **Spring Boot 3.5.x**
- **Spring Web**, **Spring Data JPA**
- **Spring Security 6** + JWT (**Auth0 java-jwt**)
- **Jakarta Validation** (`@Valid`), **Lombok**
- **Flyway** (solo DDL), **MySQL** (o **H2** en memoria para pruebas)
---

## 3) Configuración y ejecución

### `application.yml` (MySQL ejemplo)
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/forodb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: none        # DDL por Flyway
    show-sql: true
    properties:
      hibernate.format_sql: true

  flyway:
    enabled: true
    locations: classpath:db/migration

server:
  port: 8080
```
## 4) Uso con Postman (endpoints principales)
### 4.1 Registrar usuario (para poder loguear)

```bash
POST http://localhost:8080/usuarios
Content-Type: application/json

{
  "nombre": "nombre",
  "email": "nombre@test.com",
  "contrasena": "12345678"
}
```
4.2 Login (obtener JWT)
```bash
POST http://localhost:8080/login
Content-Type: application/json

{
  "email": "test@test.com",
  "contrasena": "12345678"
}
```
```makefile
Authorization: Bearer <access_token>
```
4.3 Tópicos (CRUD)
Crear
```bash
POST http://localhost:8080/topicos
Authorization: Bearer <token>
Content-Type: application/json

{
  "titulo": "Error al levantar Spring Boot",
  "mensaje": "Failed to start bean ...",
  "autorId": 1,
  "cursoId": 2
}

```
Listar (paginado + filtros + orden)
```bash
GET  http://localhost:8080/topicos?page=0&size=10&sort=fechaCreacion,asc
GET  http://localhost:8080/topicos?curso=Spring Boot
GET  http://localhost:8080/topicos?anio=2025
GET  http://localhost:8080/topicos?curso=Spring Boot&anio=2025
(agrega Authorization: Bearer <token>)
```
Detalles
```bash
GET http://localhost:8080/topicos/3
Authorization: Bearer <token>
```
Actualizar
```bash
PUT http://localhost:8080/topicos/3
Authorization: Bearer <token>
Content-Type: application/json

{
  "titulo": "Título actualizado",
  "estado": "CLOSED"
}
```

Eliminar
```bash
DELETE http://localhost:8080/topicos/3
Authorization: Bearer <token>
```
