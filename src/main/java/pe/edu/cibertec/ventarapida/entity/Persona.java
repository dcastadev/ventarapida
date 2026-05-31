package pe.edu.cibertec.ventarapida.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "PERSONAS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Persona {

    @Id
    @Column(name = "id_persona", length = 8)
    private String idPersona;

    @Column(name = "apellidos", nullable = false, length = 200)
    private String apellidos;

    @Column(name = "nombres", nullable = false, length = 200)
    private String nombres;

    @Column(name = "dni", nullable = false, length = 8)
    private String dni;

    @Column(name = "sexo", nullable = false, length = 1)
    private String sexo;

    @Column(name = "fecha_nac")
    private LocalDate fechaNac;

    @Column(name = "celular", length = 9)
    private String celular;

    @Column(name = "email", length = 100)
    private String email;

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

    // Relacion: una persona tiene un usuario
    @OneToOne(mappedBy = "persona", fetch = FetchType.LAZY)
    private Usuario usuario;
}
