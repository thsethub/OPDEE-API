package br.ufpe.opdee.models.acesso;

import br.ufpe.opdee.models.Ambiente;
import br.ufpe.opdee.models.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
@Table(name = "acessos")
public class Acesso {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    private Usuario usuarioId;
    @ManyToOne
    private Ambiente ambienteId;
    @Column(name = "ativado")
    private boolean ativo;
    @Column(name = "created_at")
    private Timestamp createdAt;
    private String tipoUsuario;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Timestamp.from(Instant.now());
    }

}
