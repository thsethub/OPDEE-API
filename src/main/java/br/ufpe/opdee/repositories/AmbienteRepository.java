package br.ufpe.opdee.repositories;

import br.ufpe.opdee.models.Ambiente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AmbienteRepository extends JpaRepository<Ambiente, UUID> {
}
