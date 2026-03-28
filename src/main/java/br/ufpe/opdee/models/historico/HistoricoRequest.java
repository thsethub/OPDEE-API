package br.ufpe.opdee.models.historico;

import br.ufpe.opdee.models.ambiente.Ambiente;
import br.ufpe.opdee.models.perfil.Perfil;
import br.ufpe.opdee.models.Usuario;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public record HistoricoRequest (
        String usuarioId,
        UUID perfilId,
        UUID ambienteId,
        String mensagem
){
    public Historico criarRegistro(Usuario usuario, Perfil perfil, Ambiente ambiente){
        return new Historico(null, usuario, LocalDateTime.now(ZoneId.of("America/Recife")), perfil, ambiente, this.mensagem);
    }
}
