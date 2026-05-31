package pe.edu.cibertec.ventarapida.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.ventarapida.entity.Producto;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, String> {

    // Productos activos ordenados por nombre
    List<Producto> findByEstadoOrderByNombreAsc(String estado);

    // Buscar por nombre o marca (para el buscador del carrito)
    @Query("SELECT p FROM Producto p WHERE p.estado = '1' AND " +
           "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           " LOWER(p.marca)  LIKE LOWER(CONCAT('%', :termino, '%')))")
    List<Producto> buscarActivos(@Param("termino") String termino);

    // Productos con stock bajo el minimo
    @Query("SELECT p FROM Producto p WHERE p.estado = '1' AND p.stock <= p.stockMinimo")
    List<Producto> findStockBajoMinimo();
}
