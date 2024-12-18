package br.ufpe.opdee.controllers;

import br.ufpe.opdee.models.broker.Broker;
import br.ufpe.opdee.models.broker.BrokerRequest;
import br.ufpe.opdee.services.BrokerService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/broker")
public class BrokerController {

    private final BrokerService brokerService;

    public BrokerController(BrokerService brokerService) {
        this.brokerService = brokerService;
    }

    @PostMapping
    public ResponseEntity save(@RequestBody Broker broker) {
        return ResponseEntity.ok(brokerService.save(broker));
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable UUID id) {
        return ResponseEntity.ok(brokerService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        brokerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity findAll() {
        return ResponseEntity.ok(brokerService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity updateBroker(@PathVariable UUID id, @RequestBody BrokerRequest broker) {
        Broker updatedBroker = brokerService.updateBroker(id, broker);
        return ResponseEntity.ok(updatedBroker);
    }

}
