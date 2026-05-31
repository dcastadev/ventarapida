package pe.edu.cibertec.ventarapida.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.cibertec.ventarapida.dto.VentaDTO;
import pe.edu.cibertec.ventarapida.entity.Venta;
import pe.edu.cibertec.ventarapida.service.ClienteService;
import pe.edu.cibertec.ventarapida.service.ProductoService;
import pe.edu.cibertec.ventarapida.service.VentaService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class VentaController {

    private final VentaService    ventaService;
    private final ClienteService  clienteService;
    private final ProductoService productoService;

    // ── Formulario de nueva venta ────────────────────────────────────────────
    @GetMapping("/ventas/nueva")
    public String formularioVenta(Model model) {
        model.addAttribute("ventaDTO", new VentaDTO());
        model.addAttribute("clientes", clienteService.listarActivos());
        model.addAttribute("productos", productoService.listarActivos());
        return "ventas/form";
    }

    // ── Registrar venta (@Transactional dentro del service) ──────────────────
    @PostMapping("/ventas/registrar")
    public String registrarVenta(@ModelAttribute VentaDTO ventaDTO,
                                  Authentication auth,
                                  RedirectAttributes flash) {
        try {
            Venta venta = ventaService.registrarVenta(ventaDTO, auth.getName());
            flash.addFlashAttribute("mensajeExito",
                    "Venta registrada: " + venta.getSerie() + "-" +
                    String.format("%08d", venta.getCorrelativo()));
            return "redirect:/ventas/detalle/" + venta.getIdVenta();
        } catch (RuntimeException e) {
            flash.addFlashAttribute("mensajeError", "Error al registrar venta: " + e.getMessage());
            return "redirect:/ventas/nueva";
        }
    }

    // ── Ver detalle de una venta ─────────────────────────────────────────────
    @GetMapping("/ventas/detalle/{id}")
    public String verDetalle(@PathVariable String id, Model model,
                              RedirectAttributes flash) {
        return ventaService.buscarPorId(id).map(v -> {
            model.addAttribute("venta",    v);
            model.addAttribute("detalles", ventaService.detallesDeVenta(id));
            return "ventas/detalle";
        }).orElseGet(() -> {
            flash.addFlashAttribute("mensajeError", "Venta no encontrada");
            return "redirect:/dashboard";
        });
    }

    // ── Consulta 1: Ventas por cliente ───────────────────────────────────────
    @GetMapping("/consultas/por-cliente")
    public String consultaPorCliente(
            @RequestParam(required = false) String idCliente,
            Model model) {
        model.addAttribute("clientes",  clienteService.listarActivos());
        model.addAttribute("idCliente", idCliente);
        if (idCliente != null && !idCliente.isEmpty()) {
            List<Venta> ventas = ventaService.consultarPorCliente(idCliente);
            model.addAttribute("ventas", ventas);
            clienteService.buscarPorId(idCliente)
                    .ifPresent(c -> model.addAttribute("clienteSeleccionado", c));
        }
        return "ventas/consulta-cliente";
    }

    // ── Consulta 2: Ventas por rango de fechas ───────────────────────────────
    @GetMapping("/consultas/por-fecha")
    public String consultaPorFecha(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            Model model) {
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin",    fechaFin);
        if (fechaInicio != null && fechaFin != null) {
            LocalDateTime inicio = fechaInicio.atStartOfDay();
            LocalDateTime fin    = fechaFin.atTime(LocalTime.MAX);
            model.addAttribute("ventas", ventaService.consultarPorFechas(inicio, fin));
        }
        return "ventas/consulta-fecha";
    }
}
