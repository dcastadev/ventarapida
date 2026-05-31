package pe.edu.cibertec.ventarapida.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.ventarapida.entity.Venta;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, String> {

    // Consulta 1: ventas por cliente
    List<Venta> findByClienteIdClienteAndEstadoOrderByFechaVentaDesc(
            String idCliente, String estado);

    // Consulta 2: ventas por rango de fechas
    @Query("SELECT v FROM Venta v WHERE v.estado = '1' " +
           "AND v.fechaVenta BETWEEN :inicio AND :fin " +
           "ORDER BY v.fechaVenta DESC")
    List<Venta> findByRangoFechas(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin")    LocalDateTime fin);

    // Reporte cierre de caja: ventas del dia
    @Query("SELECT v FROM Venta v WHERE v.estado = '1' " +
           "AND CAST(v.fechaVenta AS date) = CAST(:fecha AS date) " +
           "ORDER BY v.fechaVenta ASC")
    List<Venta> findByFecha(@Param("fecha") LocalDateTime fecha);

    // Ultimo correlativo para una serie
    @Query("SELECT COALESCE(MAX(v.correlativo), 0) FROM Venta v WHERE v.serie = :serie")
    Integer findMaxCorrelativoBySerie(@Param("serie") String serie);
}
