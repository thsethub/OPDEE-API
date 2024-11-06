package br.ufpe.opdee.models.historico;

import java.time.LocalDateTime;
import java.util.UUID;

public record HistoricoResponse(
        UUID id,
        String nomeCompleto,
        String nomePerfil,
        String nomeAmbiente,
        String mensagem,
        LocalDateTime createdAt
) {
    public HistoricoResponse(Historico historico){
        this(
                historico.getId(),
                historico.getUsuario().getNomeCompleto(),
                historico.getPerfil().getNome(),
                historico.getAmbiente().getNome(),
                historico.getMensagem(),
                historico.getCreatedAt()
        );
    }
}
