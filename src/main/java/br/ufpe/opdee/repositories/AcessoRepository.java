package br.ufpe.opdee.repositories;


import br.ufpe.opdee.models.acesso.Acesso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AcessoRepository extends JpaRepository<Acesso, UUID> {
}
