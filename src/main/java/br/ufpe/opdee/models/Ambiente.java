package br.ufpe.opdee.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;
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
    private String nome;
    @Column(name = "created_at")
    private Timestamp createdAt;
    private String topic;
    private String messagem;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Timestamp.from(Instant.now());
    }
}
