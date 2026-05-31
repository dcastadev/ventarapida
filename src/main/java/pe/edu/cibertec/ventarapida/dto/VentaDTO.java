package pe.edu.cibertec.ventarapida.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// DTO para el formulario de registro de venta
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaDTO {

    private String idCliente;
    private String tipoCompro;  // BOLETA / FACTURA
    private String observacion;
    private List<DetalleVentaDTO> detalles = new ArrayList<>();

    // Totales calculados en el frontend / confirmados aqui
    private BigDecimal subtotal;
    private BigDecimal igv;
    private BigDecimal total;
}
