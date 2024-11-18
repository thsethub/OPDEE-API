package br.ufpe.opdee.models.acesso;

import br.ufpe.opdee.models.ambiente.Ambiente;
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
@Getter@Setter
@Table(name = "acessos")
public class Acesso {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(name = "ambiente_id")
    private Ambiente ambiente;
    @Column(name = "ativado", nullable = false)
    private boolean ativo;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "tipo_usuario", nullable = false)
    private String tipoUsuario;


}
