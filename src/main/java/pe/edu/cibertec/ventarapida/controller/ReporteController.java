package pe.edu.cibertec.ventarapida.controller;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.ventarapida.entity.Venta;
import pe.edu.cibertec.ventarapida.service.VentaService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Controller
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final VentaService ventaService;

    // ── Pagina de seleccion de reportes ──────────────────────────────────────
    @GetMapping
    public String paginaReportes(Model model) {
        model.addAttribute("hoy", LocalDate.now());
        return "reportes/index";
    }

    // ── Reporte 1: Cierre de caja del dia (PDF) ──────────────────────────────
    @GetMapping("/cierre-caja")
    public ResponseEntity<byte[]> reporteCierreCaja(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha) {
        try {
            LocalDateTime fechaDT = fecha.atStartOfDay();
            List<Venta> ventas = ventaService.ventasDelDia(fechaDT);

            // Preparar datos para Jasper
            List<Map<String, Object>> datos = new ArrayList<>();
            double totalDia = 0;
            for (Venta v : ventas) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("serie",        v.getSerie());
                fila.put("correlativo",  String.format("%08d", v.getCorrelativo()));
                fila.put("tipoCompro",   v.getTipoCompro());
                fila.put("cliente",      v.getCliente().getNombreCompleto());
                fila.put("total",        v.getTotal().doubleValue());
                fila.put("hora",         v.getFechaVenta().toLocalTime().toString());
                datos.add(fila);
                totalDia += v.getTotal().doubleValue();
            }

            Map<String, Object> params = new HashMap<>();
            params.put("FECHA",      fecha.toString());
            params.put("TOTAL_DIA",  totalDia);
            params.put("CANT_VENTAS", ventas.size());

            byte[] pdf = generarPdf("reports/cierre_caja.jrxml", params, datos);
            return buildPdfResponse(pdf, "cierre_caja_" + fecha + ".pdf");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // ── Reporte 2: Productos mas vendidos (PDF) ──────────────────────────────
    @GetMapping("/productos-vendidos")
    public ResponseEntity<byte[]> reporteProductosVendidos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaFin) {
        try {
            LocalDateTime inicio = fechaInicio.atStartOfDay();
            LocalDateTime fin    = fechaFin.atTime(LocalTime.MAX);

            List<Object[]> resultados = ventaService.productosMasVendidos(inicio, fin);

            List<Map<String, Object>> datos = new ArrayList<>();
            int rank = 1;
            for (Object[] row : resultados) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("ranking",       rank++);
                fila.put("nombre",        row[0].toString());
                fila.put("marca",         row[1].toString());
                fila.put("totalUnidades", ((Number) row[2]).intValue());
                fila.put("totalIngresos", ((Number) row[3]).doubleValue());
                datos.add(fila);
            }

            Map<String, Object> params = new HashMap<>();
            params.put("PERIODO", fechaInicio + " al " + fechaFin);

            byte[] pdf = generarPdf("reports/productos_vendidos.jrxml", params, datos);
            return buildPdfResponse(pdf, "productos_vendidos.pdf");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // ── Metodo auxiliar: compilar y llenar el reporte Jasper ─────────────────
    private byte[] generarPdf(String jrxmlPath,
                               Map<String, Object> params,
                               List<Map<String, Object>> datos) throws Exception {

        // Cargar y compilar el template .jrxml
        var resource   = new ClassPathResource(jrxmlPath);
        var compilado  = JasperCompileManager.compileReport(resource.getInputStream());

        // Crear datasource con la lista de mapas
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(datos);

        // Llenar el reporte
        JasperPrint print = JasperFillManager.fillReport(compilado, params, ds);

        // Exportar a bytes PDF
        return JasperExportManager.exportReportToPdf(print);
    }

    // ── Metodo auxiliar: construir respuesta HTTP con el PDF ──────────────────
    private ResponseEntity<byte[]> buildPdfResponse(byte[] pdf, String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.inline().filename(filename).build());
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}
