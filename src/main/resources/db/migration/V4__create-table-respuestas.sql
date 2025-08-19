CREATE TABLE IF NOT EXISTS respuestas (
                id INT AUTO_INCREMENT PRIMARY KEY,
                mensaje VARCHAR(350) NOT NULL,
                topico_id  INT NOT NULL,
                fecha_creacion DATE NOT NULL,
                autor_id INT NOT NULL,
                solucion BOOLEAN NOT NULL,
                INDEX idx_respuestas_topico (topico_id),
                INDEX idx_respuestas_autor (autor_id),
                CONSTRAINT fk_respuestas_topico
                FOREIGN KEY (topico_id) REFERENCES topicos(id) ON DELETE CASCADE,
                CONSTRAINT fk_respuestas_autor
                FOREIGN KEY (autor_id) REFERENCES usuarios(id) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;