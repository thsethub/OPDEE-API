package br.ufpe.opdee.services;

import br.ufpe.opdee.models.Broker;
import br.ufpe.opdee.repositories.BrokerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BrokerService {

    private final BrokerRepository brokerRepository;

    public List<Broker> findAll() {
        return brokerRepository.findAll();
    }

    public BrokerService(BrokerRepository brokerRepository) {
        this.brokerRepository = brokerRepository;
    }

    public Broker findById(UUID id) {
        return brokerRepository.findById(id).orElse(null);
    }

    public Broker save(Broker broker) {
        return brokerRepository.save(broker);
    }

    public void delete(UUID id) {
        brokerRepository.deleteById(id);
    }
}
