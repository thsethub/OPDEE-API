package br.ufpe.opdee.models.ambiente;

import br.ufpe.opdee.models.acesso.Acesso;
import br.ufpe.opdee.models.historico.Historico;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity // Define model como tabela
@AllArgsConstructor //Construtor com argumentos
@NoArgsConstructor //Construtor vazio
@Getter @Setter //Getters e Setters
@Table(name = "ambientes") //Nome da tabela
public class Ambiente {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "nome", nullable = false, unique = true)
    private String nome;
    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
    @Column(name = "topic", nullable = false)
    private String topic;
    @Column(name = "mensagem", nullable = false)
    private String mensagem;
    @OneToMany(mappedBy = "ambiente", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Acesso> acessos;
    @OneToMany(mappedBy = "ambiente", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Historico> historicos;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
