package pe.edu.cibertec.ventarapida.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.ventarapida.entity.Producto;
import pe.edu.cibertec.ventarapida.repository.ProductoRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    public List<Producto> listarActivos() {
        return productoRepository.findByEstadoOrderByNombreAsc("1");
    }

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> buscarPorId(String id) {
        return productoRepository.findById(id);
    }

    public List<Producto> buscar(String termino) {
        return productoRepository.buscarActivos(termino);
    }

    public List<Producto> stockBajoMinimo() {
        return productoRepository.findStockBajoMinimo();
    }

    @Transactional
    public Producto guardar(Producto producto, String usuarioActual) {
        producto.setIdProducto(generarId());
        producto.setEstado("1");
        producto.setCreateUser(usuarioActual);
        producto.setCreateDate(LocalDateTime.now());
        return productoRepository.save(producto);
    }

    @Transactional
    public Producto actualizar(Producto producto, String usuarioActual) {
        producto.setUpdatedUser(usuarioActual);
        producto.setUpdatedDate(LocalDateTime.now());
        return productoRepository.save(producto);
    }

    @Transactional
    public void eliminar(String id, String usuarioActual) {
        productoRepository.findById(id).ifPresent(p -> {
            p.setEstado("2");
            p.setDeletedUser(usuarioActual);
            p.setDeletedDate(LocalDateTime.now());
            productoRepository.save(p);
        });
    }

    private String generarId() {
        long count = productoRepository.count() + 1;
        return String.format("PRD%05d", count);
    }
}
