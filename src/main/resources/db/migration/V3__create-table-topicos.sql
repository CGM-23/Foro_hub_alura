CREATE TABLE IF NOT EXISTS topicos (
            id INT AUTO_INCREMENT PRIMARY KEY,
            titulo VARCHAR(100) NOT NULL,
            mensaje  VARCHAR(350) NOT NULL,
            fecha_creacion DATE NOT NULL,
            estado VARCHAR(30) NOT NULL,
            autor_id INT NOT NULL,
            curso_id INT NOT NULL,
            INDEX idx_topicos_autor (autor_id),
            INDEX idx_topicos_curso (curso_id),
            CONSTRAINT fk_topicos_autor
            FOREIGN KEY (autor_id) REFERENCES usuarios(id) ON DELETE CASCADE,
            CONSTRAINT fk_topicos_curso
            FOREIGN KEY (curso_id) REFERENCES cursos(id) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;