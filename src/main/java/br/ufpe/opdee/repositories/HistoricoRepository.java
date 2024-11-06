package br.ufpe.opdee.repositories;

import br.ufpe.opdee.models.historico.Historico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HistoricoRepository extends JpaRepository<Historico, UUID> {
}
