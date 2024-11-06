package br.ufpe.opdee.services;

import br.ufpe.opdee.models.historico.Historico;
import br.ufpe.opdee.repositories.HistoricoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HistoricoService {

    private final HistoricoRepository historicoRepository;

    public List<Historico> findAll() {
        return historicoRepository.findAll();
    }

    public HistoricoService(HistoricoRepository historicoRepository) {
        this.historicoRepository = historicoRepository;
    }

    public Historico findById(UUID id) {
        return historicoRepository.findById(id).orElse(null);
    }

    public Historico save(Historico historico) {
        return historicoRepository.save(historico);
    }

    public void delete(UUID id) {
        historicoRepository.deleteById(id);
    }

}
