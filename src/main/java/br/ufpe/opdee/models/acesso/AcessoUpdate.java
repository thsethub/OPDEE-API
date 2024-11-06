package br.ufpe.opdee.models.acesso;


public record AcessoUpdate(
        String tipoUsuario,
        boolean ativo
) {
    public Acesso toAcesso(){
        return new Acesso(null, null, null, this.ativo, null, this.tipoUsuario());
    }
}
