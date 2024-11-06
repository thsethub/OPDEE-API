package br.ufpe.opdee.services;

import br.ufpe.opdee.models.ambiente.Ambiente;
import br.ufpe.opdee.models.ambiente.AmbienteRequest;
import br.ufpe.opdee.repositories.AmbienteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AmbienteService {

    private final AmbienteRepository ambienteRepository;
    private final ModelMapper modelMapper;

    public List<Ambiente> findAll() {
        return ambienteRepository.findAll();
    }

    public AmbienteService(AmbienteRepository ambienteRepository, ModelMapper modelMapper) {
        this.ambienteRepository = ambienteRepository;
        this.modelMapper = modelMapper;
    }

    public Ambiente save(Ambiente ambiente) {
        return ambienteRepository.save(ambiente);
    }

    public Ambiente findById(UUID id) {
        return ambienteRepository.findById(id).orElse(null);
    }

    public Ambiente buscarPorNome(String nome) {
        return ambienteRepository.findAmbienteByNomeContainingIgnoreCase(nome);
    }

    public void delete(UUID id) {
        ambienteRepository.deleteById(id);
    }

    public Ambiente updateAmbiente(UUID id, AmbienteRequest ambienteRequest){
        Optional<Ambiente> optionalAmbiente = ambienteRepository.findById(id);

        if(optionalAmbiente.isPresent()){
            Ambiente existingAmbiente = optionalAmbiente.get();

            modelMapper.map(ambienteRequest.toAmbiente(), existingAmbiente);
            return ambienteRepository.save(existingAmbiente);
        }
        else {
            return null;
        }
    }



}
