package pe.edu.cibertec.ventarapida.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.cibertec.ventarapida.entity.Categoria;
import pe.edu.cibertec.ventarapida.service.CategoriaService;

@Controller
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    // Listar
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "categorias/lista";
    }

    // Formulario nueva
    @GetMapping("/nueva")
    public String formularioNueva(Model model) {
        model.addAttribute("categoria", new Categoria());
        model.addAttribute("modoEdicion", false);
        return "categorias/form";
    }

    // Guardar nueva
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Categoria categoria,
                          Authentication auth,
                          RedirectAttributes flash) {
        try {
            categoriaService.guardar(categoria, auth.getName());
            flash.addFlashAttribute("mensajeExito", "Categoría registrada correctamente");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/categorias";
    }

    // Formulario editar
    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable String id, Model model,
                                    RedirectAttributes flash) {
        return categoriaService.buscarPorId(id).map(cat -> {
            model.addAttribute("categoria", cat);
            model.addAttribute("modoEdicion", true);
            return "categorias/form";
        }).orElseGet(() -> {
            flash.addFlashAttribute("mensajeError", "Categoría no encontrada");
            return "redirect:/categorias";
        });
    }

    // Actualizar
    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute Categoria categoria,
                              Authentication auth,
                              RedirectAttributes flash) {
        try {
            categoriaService.actualizar(categoria, auth.getName());
            flash.addFlashAttribute("mensajeExito", "Categoría actualizada correctamente");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error al actualizar: " + e.getMessage());
        }
        return "redirect:/categorias";
    }

    // Eliminar logico
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id,
                           Authentication auth,
                           RedirectAttributes flash) {
        try {
            categoriaService.eliminar(id, auth.getName());
            flash.addFlashAttribute("mensajeExito", "Categoría desactivada correctamente");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/categorias";
    }
}
