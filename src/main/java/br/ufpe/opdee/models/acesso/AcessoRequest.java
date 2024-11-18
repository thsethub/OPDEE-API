package br.ufpe.opdee.models.acesso;

import br.ufpe.opdee.models.ambiente.Ambiente;
import br.ufpe.opdee.models.Usuario;

import java.time.LocalDateTime;
import java.util.UUID;

public record AcessoRequest(
        String usuarioId,
        UUID ambienteId,
        boolean ativo,
        String tipoUsuario
) {

    public Acesso criarAcesso(Usuario usuario, Ambiente ambiente){
        return new Acesso(null, usuario, ambiente, this.ativo, LocalDateTime.now(), tipoUsuario);
    }
}