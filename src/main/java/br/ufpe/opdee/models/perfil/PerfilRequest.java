package br.ufpe.opdee.models.perfil;

public record PerfilRequest(
        String nome
) {
    public Perfil toPerfil(){
        return new Perfil(null, this.nome(), null, null);
    }
}
