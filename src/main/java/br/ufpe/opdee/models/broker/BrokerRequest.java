package br.ufpe.opdee.models.broker;

public record BrokerRequest(
        String ipAdress,
        String port,
        String username,
        String password
){
    public Broker toBroker(){
        return new Broker(null, null, this.ipAdress(), this.port(), this.username(), this.password());
    }
}

