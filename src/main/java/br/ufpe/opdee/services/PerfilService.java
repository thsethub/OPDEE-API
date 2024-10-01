package br.ufpe.opdee.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.ufpe.opdee.models.Perfil;
import br.ufpe.opdee.repositories.PerfilRepository;

import java.util.List;
import java.util.UUID;


@Service
public class PerfilService {
    private final PerfilRepository perfilRepository;

    public List<Perfil> findAll() {
        return perfilRepository.findAll();
    }

    public PerfilService(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    public Perfil save(Perfil perfil) {
        return perfilRepository.save(perfil);
    }

    public Perfil findById(UUID id) {
        return perfilRepository.findById(id).orElse(null);
    }

    public void delete(UUID id) {
        perfilRepository.deleteById(id);
    }
}
