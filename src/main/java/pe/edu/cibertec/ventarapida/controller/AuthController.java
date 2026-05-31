package pe.edu.cibertec.ventarapida.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login(
            @RequestParam(value = "error",   required = false) String error,
            @RequestParam(value = "logout",  required = false) String logout,
            @RequestParam(value = "expired", required = false) String expired,
            Model model) {

        if (error   != null) model.addAttribute("errorMsg",   "Usuario o contraseña incorrectos");
        if (logout  != null) model.addAttribute("logoutMsg",  "Sesión cerrada correctamente");
        if (expired != null) model.addAttribute("expiredMsg", "Tu sesión ha expirado");
        return "auth/login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
}
