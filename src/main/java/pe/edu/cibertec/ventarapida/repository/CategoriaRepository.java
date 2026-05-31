// ============================================================
// CategoriaRepository.java
// ============================================================
package pe.edu.cibertec.ventarapida.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.ventarapida.entity.Categoria;
import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, String> {

    // Solo categorias activas
    List<Categoria> findByEstadoOrderByNombreAsc(String estado);

    // Verificar nombre duplicado
    boolean existsByNombreAndEstado(String nombre, String estado);
}
