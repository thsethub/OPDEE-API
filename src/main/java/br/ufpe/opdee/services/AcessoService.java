package br.ufpe.opdee.services;

import br.ufpe.opdee.models.acesso.Acesso;
import br.ufpe.opdee.repositories.AcessoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcessoService {

    private final AcessoRepository acessoRepository;

    public List<Acesso> findAll() {
        return acessoRepository.findAll();
    }

    public AcessoService(AcessoRepository acessoRepository) {
        this.acessoRepository = acessoRepository;
    }

    public Acesso findById(java.util.UUID id) {
        return acessoRepository.findById(id).orElse(null);
    }

    public Acesso save(Acesso acesso) {
        return acessoRepository.save(acesso);
    }

    public void delete(java.util.UUID id) {
        acessoRepository.deleteById(id);
    }


}
