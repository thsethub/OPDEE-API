package br.ufpe.opdee.models.perfil;

import java.util.UUID;

public record PerfilResponse(
        UUID id
) {
    public PerfilResponse(Perfil perfil) {
        this(perfil.getId());
    }
}
