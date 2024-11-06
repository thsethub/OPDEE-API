package br.ufpe.opdee.repositories;

import br.ufpe.opdee.models.broker.Broker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BrokerRepository extends JpaRepository<Broker, UUID> {
}
