package br.ufpe.opdee.models.historico;

import br.ufpe.opdee.models.ambiente.Ambiente;
import br.ufpe.opdee.models.perfil.Perfil;
import br.ufpe.opdee.models.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Table(name = "historico")
public class Historico {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    private Usuario usuario;
    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "perfil_id")
    private Perfil perfil;
    @ManyToOne
    private Ambiente ambiente;
    private String mensagem;
}