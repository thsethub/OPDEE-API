package br.ufpe.opdee.repositories;

import br.ufpe.opdee.models.ambiente.Ambiente;
import br.ufpe.opdee.models.Usuario;
import br.ufpe.opdee.models.acesso.Acesso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AcessoRepository extends JpaRepository<Acesso, UUID> {

    List<Acesso> findAcessoByUsuario(Usuario usuario);
    List<Acesso> findAcessoByAmbiente(Ambiente ambiente);

}
