package pe.edu.cibertec.ventarapida.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "USUARIOS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @Column(name = "id_user", length = 8)
    private String idUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usPersona", referencedColumnName = "id_persona")
    private Persona persona;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usRol", referencedColumnName = "id_rol")
    private Rol rol;

    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 300)
    private String passwordHash;

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

    // Un usuario registra muchas ventas
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Venta> ventas;
}
