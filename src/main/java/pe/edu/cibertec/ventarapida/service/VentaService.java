package pe.edu.cibertec.ventarapida.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.ventarapida.dto.DetalleVentaDTO;
import pe.edu.cibertec.ventarapida.dto.VentaDTO;
import pe.edu.cibertec.ventarapida.entity.*;
import pe.edu.cibertec.ventarapida.repository.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository        ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final ClienteRepository      clienteRepository;
    private final ProductoRepository     productoRepository;
    private final UsuarioRepository      usuarioRepository;

    // ── Transaccional principal ──────────────────────────────────────────────
    // Si cualquier paso falla → rollback automatico de TODO
    @Transactional
    public Venta registrarVenta(VentaDTO dto, String username) {

        // 1. Obtener cliente
        Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // 2. Obtener usuario actual
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 3. Determinar serie segun tipo de comprobante
        String serie = dto.getTipoCompro().equals("BOLETA") ? "B001" : "F001";
        int correlativo = ventaRepository.findMaxCorrelativoBySerie(serie) + 1;

        // 4. Crear cabecera de venta
        Venta venta = new Venta();
        venta.setIdVenta(generarIdVenta());
        venta.setCliente(cliente);
        venta.setUsuario(usuario);
        venta.setFechaVenta(LocalDateTime.now());
        venta.setTipoCompro(dto.getTipoCompro());
        venta.setSerie(serie);
        venta.setCorrelativo(correlativo);
        venta.setObservacion(dto.getObservacion());
        venta.setEstado("1");
        venta.setCreateUser(username);
        venta.setCreateDate(LocalDateTime.now());

        // 5. Calcular totales procesando cada linea del detalle
        BigDecimal subtotalTotal = BigDecimal.ZERO;

        // Guardar la venta primero (la necesitamos para el detalle)
        venta.setSubtotal(BigDecimal.ZERO);
        venta.setIgv(BigDecimal.ZERO);
        venta.setTotal(BigDecimal.ZERO);
        Venta ventaGuardada = ventaRepository.save(venta);

        // 6. Guardar cada linea de detalle + descontar stock
        for (DetalleVentaDTO lineaDTO : dto.getDetalles()) {

            Producto producto = productoRepository.findById(lineaDTO.getIdProducto())
                    .orElseThrow(() -> new RuntimeException(
                            "Producto no encontrado: " + lineaDTO.getIdProducto()));

            // Validar stock disponible
            if (producto.getStock() < lineaDTO.getCantidad()) {
                throw new RuntimeException(
                        "Stock insuficiente para: " + producto.getNombre() +
                        " (disponible: " + producto.getStock() + ")");
            }

            // Calcular subtotal de la linea
            BigDecimal descuento = lineaDTO.getDescuento() != null
                    ? lineaDTO.getDescuento() : BigDecimal.ZERO;
            BigDecimal precioConDesc = lineaDTO.getPrecioUnit().subtract(descuento);
            BigDecimal subtotalLinea = precioConDesc.multiply(
                    BigDecimal.valueOf(lineaDTO.getCantidad()))
                    .setScale(2, RoundingMode.HALF_UP);

            // Guardar detalle
            DetalleVenta detalle = new DetalleVenta();
            detalle.setIdDetalle(generarIdDetalle());
            detalle.setVenta(ventaGuardada);
            detalle.setProducto(producto);
            detalle.setCantidad(lineaDTO.getCantidad());
            detalle.setPrecioUnit(lineaDTO.getPrecioUnit());
            detalle.setDescuento(descuento);
            detalle.setSubtotal(subtotalLinea);
            detalle.setEstado("1");
            detalleVentaRepository.save(detalle);

            // Descontar stock del producto
            producto.setStock(producto.getStock() - lineaDTO.getCantidad());
            productoRepository.save(producto);

            subtotalTotal = subtotalTotal.add(subtotalLinea);
        }

        // 7. Calcular IGV (18%) y total y actualizar la venta
        BigDecimal igv   = subtotalTotal.multiply(new BigDecimal("0.18"))
                                        .setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotalTotal.add(igv).setScale(2, RoundingMode.HALF_UP);

        ventaGuardada.setSubtotal(subtotalTotal);
        ventaGuardada.setIgv(igv);
        ventaGuardada.setTotal(total);
        ventaRepository.save(ventaGuardada);

        return ventaGuardada;
    }

    // ── Consultas ────────────────────────────────────────────────────────────

    public Optional<Venta> buscarPorId(String id) {
        return ventaRepository.findById(id);
    }

    public List<Venta> consultarPorCliente(String idCliente) {
        return ventaRepository
                .findByClienteIdClienteAndEstadoOrderByFechaVentaDesc(idCliente, "1");
    }

    public List<Venta> consultarPorFechas(LocalDateTime inicio, LocalDateTime fin) {
        return ventaRepository.findByRangoFechas(inicio, fin);
    }

    public List<Venta> ventasDelDia(LocalDateTime fecha) {
        return ventaRepository.findByFecha(fecha);
    }

    public List<DetalleVenta> detallesDeVenta(String idVenta) {
        return detalleVentaRepository.findByVentaIdVenta(idVenta);
    }

    public List<Object[]> productosMasVendidos(LocalDateTime inicio, LocalDateTime fin) {
        return detalleVentaRepository.findProductosMasVendidos(inicio, fin);
    }

    // ── Generadores de ID ────────────────────────────────────────────────────

    private String generarIdVenta() {
        long count = ventaRepository.count() + 1;
        return String.format("VTA%05d", count);
    }

    private String generarIdDetalle() {
        long count = detalleVentaRepository.count() + 1;
        return String.format("DET%05d", count);
    }
}
