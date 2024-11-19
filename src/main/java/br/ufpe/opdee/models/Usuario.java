package br.ufpe.opdee.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity // Define model como tabela
@AllArgsConstructor //Construtor com argumentos
@NoArgsConstructor //Construtor vazio
@Getter @Setter //Getters e Setters
@Table(name = "usuarios")//Nome da tabela
public class Usuario {
    @Id
    private String uuid;
    @Column(name = "nome_completo", nullable = false)
    private String nomeCompleto;
    @Column(name = "email_ufpe", nullable = false, unique = true)
    private String emailUfpe;
    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
    @Column(name = "superuser", nullable = false)
    private boolean superUser;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }



}
