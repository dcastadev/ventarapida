package pe.edu.cibertec.ventarapida.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.ventarapida.entity.Usuario;
import pe.edu.cibertec.ventarapida.repository.UsuarioRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository
                .findByUsernameAndEstado(username, "1")
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado: " + username));

        // El rol en BD es "ADMIN" o "VENDEDOR"
        // Spring Security necesita el prefijo ROLE_
        String rol = "ROLE_" + usuario.getRol().getNombreRol().toUpperCase();

        return new org.springframework.security.core.userdetails.User(
                usuario.getUsername(),
                usuario.getPasswordHash(),
                List.of(new SimpleGrantedAuthority(rol))
        );
    }
}
