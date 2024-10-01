package br.ufpe.opdee.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
@Table(name = "historico")
public class Historico {
    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    private String id;
    @ManyToOne
    private Usuario nome;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @ManyToOne
    private Perfil perfil;
    @ManyToOne
    private Ambiente ambiente;
    private String mensagem;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Timestamp.from(Instant.now());
    }
}
