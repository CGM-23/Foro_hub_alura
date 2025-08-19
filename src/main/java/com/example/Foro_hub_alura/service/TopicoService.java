package com.example.Foro_hub_alura.service;

import com.example.Foro_hub_alura.dto.topico.DatosActualizarTopico;
import com.example.Foro_hub_alura.dto.topico.DatosRegistroTopico;
import com.example.Foro_hub_alura.dto.topico.DatosTopicoView;
import com.example.Foro_hub_alura.modelos.Curso;
import com.example.Foro_hub_alura.modelos.Topico;
import com.example.Foro_hub_alura.modelos.Usuario;
import com.example.Foro_hub_alura.repository.CursoRepository;
import com.example.Foro_hub_alura.repository.TopicoRepository;
import com.example.Foro_hub_alura.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

@Service
public class TopicoService {

    private static final Set<String> ESTADOS_VALIDOS = Set.of("OPEN","CLOSED","ARCHIVED");

    private final TopicoRepository topicoRepo;
    private final CursoRepository cursoRepo;
    private final UsuarioRepository usuarioRepo; // <--- NUEVO

    public TopicoService(TopicoRepository topicoRepo,
                         CursoRepository cursoRepo,
                         UsuarioRepository usuarioRepo) {     // <--- NUEVO
        this.topicoRepo = topicoRepo;
        this.cursoRepo = cursoRepo;
        this.usuarioRepo = usuarioRepo;                       // <--- NUEVO
    }

    /* === CREAR === */
    @Transactional
    public DatosTopicoView crear(DatosRegistroTopico dto) {
        // No duplicados (título + mensaje)
        if (topicoRepo.existsByTituloIgnoreCaseAndMensajeIgnoreCase(dto.titulo(), dto.mensaje())) {
            throw new IllegalStateException("topico_duplicado");
        }

        Usuario autor = usuarioRepo.findById(dto.autorId())
                .orElseThrow(() -> new IllegalArgumentException("autor_no_encontrado"));

        Curso curso = cursoRepo.findById(dto.cursoId())
                .orElseThrow(() -> new IllegalArgumentException("curso_no_encontrado"));

        Topico t = new Topico();
        t.setTitulo(dto.titulo());
        t.setMensaje(dto.mensaje());
        t.setFechaCreacion(LocalDate.now()); // DB usa DATE
        t.setEstado("OPEN");
        t.setAutor(autor);
        t.setCurso(curso);

        t = topicoRepo.save(t);
        return toView(t); // <- Devuelve DTO con id(), así compila resp.id()
    }


    @Transactional(readOnly = true)
    public DatosTopicoView detalle(Integer id) {
        var t = topicoRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("topico_no_encontrado"));
        return toView(t);
    }

    @Transactional
    public DatosTopicoView actualizar(Integer id, DatosActualizarTopico dto) {
        var t = topicoRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("topico_no_encontrado"));


        String nuevoTitulo  = dto.titulo()  != null ? dto.titulo()  : t.getTitulo();
        String nuevoMensaje = dto.mensaje() != null ? dto.mensaje() : t.getMensaje();

        if (topicoRepo.existsByTituloIgnoreCaseAndMensajeIgnoreCaseAndIdNot(nuevoTitulo, nuevoMensaje, t.getId())) {
            throw new IllegalStateException("topico_duplicado");
        }

        if (dto.titulo()  != null) t.setTitulo(dto.titulo());
        if (dto.mensaje() != null) t.setMensaje(dto.mensaje());

        if (dto.estado() != null) {
            String e = dto.estado().toUpperCase();
            if (!ESTADOS_VALIDOS.contains(e)) throw new IllegalArgumentException("estado_invalido");
            t.setEstado(e);
        }

        if (dto.cursoId() != null) {
            Curso curso = cursoRepo.findById(dto.cursoId())
                    .orElseThrow(() -> new IllegalArgumentException("curso_no_encontrado"));
            t.setCurso(curso);
        }
        return toView(t);
    }
// imports necesarios:
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import java.time.LocalDate;
// import java.time.Month;

    @Transactional(readOnly = true)
    public Page<DatosTopicoView> listar(String curso, Integer anio, Pageable pageable) {
        Page<Topico> page;

        if (curso != null && !curso.isBlank() && anio != null) {
            LocalDate start = LocalDate.of(anio, Month.JANUARY, 1);
            LocalDate end   = LocalDate.of(anio, Month.DECEMBER, 31);
            page = topicoRepo.findByCurso_NombreIgnoreCaseAndFechaCreacionBetween(
                    curso.trim(), start, end, pageable);
        } else if (curso != null && !curso.isBlank()) {
            page = topicoRepo.findByCurso_NombreIgnoreCase(curso.trim(), pageable);
        } else if (anio != null) {
            LocalDate start = LocalDate.of(anio, Month.JANUARY, 1);
            LocalDate end   = LocalDate.of(anio, Month.DECEMBER, 31);
            page = topicoRepo.findByFechaCreacionBetween(start, end, pageable);
        } else {
            page = topicoRepo.findAll(pageable);
        }

        return page.map(this::toView);
    }

    private DatosTopicoView toView(Topico t) {
        return new DatosTopicoView(
                t.getId(),
                t.getTitulo(),
                t.getMensaje(),
                t.getFechaCreacion(),
                t.getEstado(),
                t.getAutor().getId(),
                t.getAutor().getNombre(),
                t.getCurso().getId(),
                t.getCurso().getNombre()
        );
    }
    @Transactional
    public void eliminar(Integer id) {
        if (!topicoRepo.existsById(id)) {
            throw new IllegalArgumentException("topico_no_encontrado");
        }
        topicoRepo.deleteById(id);
    }
}
