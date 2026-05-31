package pe.edu.cibertec.ventarapida.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.ventarapida.entity.DetalleVenta;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, String> {

    List<DetalleVenta> findByVentaIdVenta(String idVenta);

    // Reporte productos mas vendidos (para JasperReports)
    @Query("SELECT d.producto.nombre, d.producto.marca, " +
           "SUM(d.cantidad) AS totalUnidades, " +
           "SUM(d.subtotal) AS totalIngresos " +
           "FROM DetalleVenta d " +
           "WHERE d.venta.estado = '1' " +
           "AND d.venta.fechaVenta BETWEEN :inicio AND :fin " +
           "GROUP BY d.producto.nombre, d.producto.marca " +
           "ORDER BY totalUnidades DESC")
    List<Object[]> findProductosMasVendidos(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin")    LocalDateTime fin);
}
