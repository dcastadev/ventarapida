package pe.edu.cibertec.ventarapida.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVentaDTO {

    private String idProducto;
    private String nombreProducto;
    private Integer cantidad;
    private BigDecimal precioUnit;
    private BigDecimal descuento;
    private BigDecimal subtotal;
}
