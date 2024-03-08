package edu.unimagdalena.microg2.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mensajes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

public class Mensaje {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String creador;
    private String destinatario;
    private String contenido;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createAt;
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

}
