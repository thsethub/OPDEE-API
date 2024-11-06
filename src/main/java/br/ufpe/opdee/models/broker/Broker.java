package br.ufpe.opdee.models.broker;

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
@Table(name = "brokerConfig")
public class Broker {
    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
    @Column(name = "ip_address", nullable = false)
    private String ipAdress;
    @Column(name = "port", nullable = false)
    private String port;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}
