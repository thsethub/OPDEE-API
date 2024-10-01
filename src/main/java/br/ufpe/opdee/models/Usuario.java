package br.ufpe.opdee.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.Instant;

@Entity // Define model como tabela
@AllArgsConstructor //Construtor com argumentos
@NoArgsConstructor //Construtor vazio
@Getter @Setter //Getters e Setters
@Table(name = "usuarios")//Nome da tabela
public class Usuario {
    @Id
    private String uuid;
    @Column(name = "nome_completo")
    private String nomeCompleto;
    @Column(name = "email_ufpe")
    private String emailUfpe;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "superuser")
    private boolean superUser;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Timestamp.from(Instant.now());
    }

}
