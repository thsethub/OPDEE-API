package br.ufpe.opdee.models.acesso;

import java.util.UUID;

public record UserAcessoResponse (
        UUID id,
        String usuarioId,
        String nomeUsuario,
        String email,
        String tipoUsuario,
        boolean ativo
){
    public UserAcessoResponse (Acesso acesso){
        this(
                acesso.getId(),
                acesso.getUsuario().getUuid(),
                acesso.getUsuario().getNomeCompleto(),
                acesso.getUsuario().getEmailUfpe(),
                acesso.getTipoUsuario(),
                acesso.isAtivo()
        );
    }
}
