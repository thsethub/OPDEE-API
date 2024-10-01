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
@Table(name = "brokerConfig")
public class Broker {
    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    private String id;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "ip_address")
    private String ipAdress;
    private String port;
    private String username;
    private String password;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Timestamp.from(Instant.now());
    }
}
