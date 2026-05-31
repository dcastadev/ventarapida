package pe.edu.cibertec.ventarapida.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "CLIENTES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @Column(name = "id_cliente", length = 8)
    private String idCliente;

    @Column(name = "apellidos", nullable = false, length = 200)
    private String apellidos;

    @Column(name = "nombres", nullable = false, length = 200)
    private String nombres;

    @Column(name = "tipo_doc", nullable = false, length = 3)
    private String tipoDoc; // DNI o RUC

    @Column(name = "nro_doc", nullable = false, unique = true, length = 12)
    private String nroDoc;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "celular", length = 9)
    private String celular;

    @Column(name = "direccion", length = 300)
    private String direccion;

    @Column(name = "estado", length = 1)
    private String estado = "1";

    @Column(name = "create_user", length = 50)
    private String createUser;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "updated_user", length = 50)
    private String updatedUser;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;

    @Column(name = "deleted_user", length = 50)
    private String deletedUser;

    // Un cliente tiene muchas ventas
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<Venta> ventas;

    // Metodo auxiliar para mostrar nombre completo
    public String getNombreCompleto() {
        return apellidos + ", " + nombres;
    }
}
