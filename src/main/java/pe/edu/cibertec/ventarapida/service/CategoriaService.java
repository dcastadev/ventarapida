package pe.edu.cibertec.ventarapida.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.ventarapida.entity.Categoria;
import pe.edu.cibertec.ventarapida.repository.CategoriaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    // Listar todas activas
    public List<Categoria> listarActivas() {
        return categoriaRepository.findByEstadoOrderByNombreAsc("1");
    }

    // Listar todas (para tabla admin)
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    // Buscar por ID
    public Optional<Categoria> buscarPorId(String id) {
        return categoriaRepository.findById(id);
    }

    // Guardar nueva categoria
    @Transactional
    public Categoria guardar(Categoria categoria, String usuarioActual) {
        categoria.setIdCategoria(generarId());
        categoria.setEstado("1");
        categoria.setCreateUser(usuarioActual);
        categoria.setCreateDate(LocalDateTime.now());
        return categoriaRepository.save(categoria);
    }

    // Actualizar categoria
    @Transactional
    public Categoria actualizar(Categoria categoria, String usuarioActual) {
        categoria.setUpdatedUser(usuarioActual);
        categoria.setUpdatedDate(LocalDateTime.now());
        return categoriaRepository.save(categoria);
    }

    // Eliminar logico (estado = 2)
    @Transactional
    public void eliminar(String id, String usuarioActual) {
        categoriaRepository.findById(id).ifPresent(cat -> {
            cat.setEstado("2");
            cat.setDeletedUser(usuarioActual);
            cat.setDeletedDate(LocalDateTime.now());
            categoriaRepository.save(cat);
        });
    }

    // Generar ID tipo "CAT00001"
    private String generarId() {
        long count = categoriaRepository.count() + 1;
        return String.format("CAT%05d", count);
    }
}
