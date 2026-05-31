package pe.edu.cibertec.ventarapida.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.cibertec.ventarapida.entity.Producto;
import pe.edu.cibertec.ventarapida.service.CategoriaService;
import pe.edu.cibertec.ventarapida.service.ProductoService;

@Controller
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService  productoService;
    private final CategoriaService categoriaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos",   productoService.listarTodos());
        return "productos/lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("producto",    new Producto());
        model.addAttribute("categorias",  categoriaService.listarActivas());
        model.addAttribute("modoEdicion", false);
        return "productos/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto,
                          Authentication auth,
                          RedirectAttributes flash) {
        try {
            productoService.guardar(producto, auth.getName());
            flash.addFlashAttribute("mensajeExito", "Producto registrado correctamente");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error: " + e.getMessage());
        }
        return "redirect:/productos";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable String id, Model model,
                                    RedirectAttributes flash) {
        return productoService.buscarPorId(id).map(p -> {
            model.addAttribute("producto",    p);
            model.addAttribute("categorias",  categoriaService.listarActivas());
            model.addAttribute("modoEdicion", true);
            return "productos/form";
        }).orElseGet(() -> {
            flash.addFlashAttribute("mensajeError", "Producto no encontrado");
            return "redirect:/productos";
        });
    }

    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute Producto producto,
                              Authentication auth,
                              RedirectAttributes flash) {
        try {
            productoService.actualizar(producto, auth.getName());
            flash.addFlashAttribute("mensajeExito", "Producto actualizado correctamente");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error: " + e.getMessage());
        }
        return "redirect:/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, Authentication auth,
                           RedirectAttributes flash) {
        try {
            productoService.eliminar(id, auth.getName());
            flash.addFlashAttribute("mensajeExito", "Producto desactivado correctamente");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error: " + e.getMessage());
        }
        return "redirect:/productos";
    }
}
