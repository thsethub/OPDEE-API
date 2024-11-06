package br.ufpe.opdee.models.acesso;

import java.util.UUID;

public record AcessoResponse (
        UUID ambienteId,
        String nomeAmbiente,
        String topic,
        String mensagem,
        boolean ativo,
        String tipoUsuario
){
    public AcessoResponse(Acesso acesso){
        this(
                acesso.getAmbiente().getId(),
                acesso.getAmbiente().getNome(),
                acesso.getAmbiente().getTopic(),
                acesso.getAmbiente().getMensagem(),
                acesso.isAtivo(),
                acesso.getTipoUsuario()

        );
    }




}
