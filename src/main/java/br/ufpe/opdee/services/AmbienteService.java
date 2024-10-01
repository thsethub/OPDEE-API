package br.ufpe.opdee.services;

import br.ufpe.opdee.models.Ambiente;
import br.ufpe.opdee.repositories.AmbienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AmbienteService {

    private final AmbienteRepository ambienteRepository;

    public List<Ambiente> findAll() {
        return ambienteRepository.findAll();
    }

    public AmbienteService(AmbienteRepository ambienteRepository) {
        this.ambienteRepository = ambienteRepository;
    }

    public Ambiente save(Ambiente ambiente) {
        return ambienteRepository.save(ambiente);
    }

    public Ambiente findById(UUID id) {
        return ambienteRepository.findById(id).orElse(null);
    }

    public void delete(UUID id) {
        ambienteRepository.deleteById(id);
    }




}
