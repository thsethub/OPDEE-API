package br.ufpe.opdee.repositories;

import br.ufpe.opdee.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
}
