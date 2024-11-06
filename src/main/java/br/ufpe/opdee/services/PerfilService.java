package br.ufpe.opdee.services;

import br.ufpe.opdee.models.perfil.PerfilRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import br.ufpe.opdee.models.perfil.Perfil;
import br.ufpe.opdee.repositories.PerfilRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class PerfilService {
    private final PerfilRepository perfilRepository;
    private final ModelMapper modelMapper;

    public List<Perfil> findAll() {
        return perfilRepository.findAll();
    }

    public PerfilService(PerfilRepository perfilRepository, ModelMapper modelMapper) {
        this.perfilRepository = perfilRepository;
        this.modelMapper = modelMapper;
    }

    public Perfil save(Perfil perfil) {
        return perfilRepository.save(perfil);
    }

    public Perfil findByNome(String nome) {
        return perfilRepository.findByNome(nome);
    }

    public Perfil findById(UUID id) {
        return perfilRepository.findById(id).orElse(null);
    }

    public void delete(UUID id) {
        perfilRepository.deleteById(id);
    }

    public Perfil updatePerfil(UUID id, PerfilRequest perfilRequest) {
        Optional<Perfil> optionalPerfil = perfilRepository.findById(id);

        if(optionalPerfil.isPresent()) {
            Perfil existingPerfil = optionalPerfil.get();

            modelMapper.map(perfilRequest.toPerfil(), existingPerfil);
            return perfilRepository.save(existingPerfil);
        }
        else {
            return null;
        }
    }
}
