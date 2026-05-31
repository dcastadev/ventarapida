package pe.edu.cibertec.ventarapida.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.ventarapida.entity.Cliente;
import pe.edu.cibertec.ventarapida.repository.ClienteRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public List<Cliente> listarActivos() {
        return clienteRepository.findByEstadoOrderByApellidosAsc("1");
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(String id) {
        return clienteRepository.findById(id);
    }

    public List<Cliente> buscar(String termino) {
        return clienteRepository.buscarActivos(termino);
    }

    public boolean existeDocumento(String nroDoc) {
        return clienteRepository.existsByNroDoc(nroDoc);
    }

    @Transactional
    public Cliente guardar(Cliente cliente, String usuarioActual) {
        cliente.setIdCliente(generarId());
        cliente.setEstado("1");
        cliente.setCreateUser(usuarioActual);
        cliente.setCreateDate(LocalDateTime.now());
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente actualizar(Cliente cliente, String usuarioActual) {
        cliente.setUpdatedUser(usuarioActual);
        cliente.setUpdatedDate(LocalDateTime.now());
        return clienteRepository.save(cliente);
    }

    @Transactional
    public void eliminar(String id, String usuarioActual) {
        clienteRepository.findById(id).ifPresent(c -> {
            c.setEstado("2");
            c.setDeletedUser(usuarioActual);
            c.setDeletedDate(LocalDateTime.now());
            clienteRepository.save(c);
        });
    }

    private String generarId() {
        long count = clienteRepository.count() + 1;
        return String.format("CLI%05d", count);
    }
}
