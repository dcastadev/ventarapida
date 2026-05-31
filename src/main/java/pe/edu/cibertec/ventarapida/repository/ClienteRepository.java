package pe.edu.cibertec.ventarapida.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.ventarapida.entity.Cliente;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {

    List<Cliente> findByEstadoOrderByApellidosAsc(String estado);

    Optional<Cliente> findByNroDocAndEstado(String nroDoc, String estado);

    boolean existsByNroDoc(String nroDoc);

    // Buscar por nombre, apellido o doc
    @Query("SELECT c FROM Cliente c WHERE c.estado = '1' AND " +
           "(LOWER(c.apellidos) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           " LOWER(c.nombres)   LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           " c.nroDoc           LIKE CONCAT('%', :termino, '%'))")
    List<Cliente> buscarActivos(@Param("termino") String termino);
}
