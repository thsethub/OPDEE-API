package br.ufpe.opdee.repositories;

import br.ufpe.opdee.models.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PerfilRepository extends JpaRepository<Perfil, UUID> {
}
