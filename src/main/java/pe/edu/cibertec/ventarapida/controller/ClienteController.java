package pe.edu.cibertec.ventarapida.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.cibertec.ventarapida.entity.Cliente;
import pe.edu.cibertec.ventarapida.service.ClienteService;

@Controller
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        return "clientes/lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("cliente",     new Cliente());
        model.addAttribute("modoEdicion", false);
        return "clientes/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Cliente cliente,
                          Authentication auth,
                          RedirectAttributes flash) {
        if (clienteService.existeDocumento(cliente.getNroDoc())) {
            flash.addFlashAttribute("mensajeError",
                    "Ya existe un cliente con ese número de documento");
            return "redirect:/clientes/nuevo";
        }
        try {
            clienteService.guardar(cliente, auth.getName());
            flash.addFlashAttribute("mensajeExito", "Cliente registrado correctamente");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error: " + e.getMessage());
        }
        return "redirect:/clientes";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable String id, Model model,
                                    RedirectAttributes flash) {
        return clienteService.buscarPorId(id).map(c -> {
            model.addAttribute("cliente",     c);
            model.addAttribute("modoEdicion", true);
            return "clientes/form";
        }).orElseGet(() -> {
            flash.addFlashAttribute("mensajeError", "Cliente no encontrado");
            return "redirect:/clientes";
        });
    }

    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute Cliente cliente,
                              Authentication auth,
                              RedirectAttributes flash) {
        try {
            clienteService.actualizar(cliente, auth.getName());
            flash.addFlashAttribute("mensajeExito", "Cliente actualizado correctamente");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error: " + e.getMessage());
        }
        return "redirect:/clientes";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, Authentication auth,
                           RedirectAttributes flash) {
        try {
            clienteService.eliminar(id, auth.getName());
            flash.addFlashAttribute("mensajeExito", "Cliente desactivado correctamente");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error: " + e.getMessage());
        }
        return "redirect:/clientes";
    }
}
