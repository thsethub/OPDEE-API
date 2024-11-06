package br.ufpe.opdee.models.ambiente;

public record AmbienteRequest(
        String nome,
        String topic,
        String mensagem
) {
    public Ambiente toAmbiente(){
        return new Ambiente(null, this.nome(), null, this.topic(), this.mensagem(), null, null);
    }
}
