package br.ufpe.opdee.services;

import br.ufpe.opdee.models.broker.Broker;
import br.ufpe.opdee.models.broker.BrokerRequest;
import br.ufpe.opdee.repositories.BrokerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BrokerService {

    private final BrokerRepository brokerRepository;
    private final ModelMapper modelMapper;

    public List<Broker> findAll() {
        return brokerRepository.findAll();
    }

    public BrokerService(BrokerRepository brokerRepository, ModelMapper modelMapper) {
        this.brokerRepository = brokerRepository;
        this.modelMapper = modelMapper;
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
    
    public Broker updateBroker(UUID id, BrokerRequest brokerRequest) {
        Optional<Broker> optionalBroker = brokerRepository.findById(id);

        if (optionalBroker.isPresent()) {
            Broker existingBroker = optionalBroker.get();

            modelMapper.map(brokerRequest.toBroker(), existingBroker);

            return brokerRepository.save(existingBroker);
        } else {
            return null;
        }
    }
}
