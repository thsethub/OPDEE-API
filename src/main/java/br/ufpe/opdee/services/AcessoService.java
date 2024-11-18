package br.ufpe.opdee.services;

import br.ufpe.opdee.models.acesso.AcessoUpdate;
import br.ufpe.opdee.models.ambiente.Ambiente;
import br.ufpe.opdee.models.Usuario;
import br.ufpe.opdee.models.acesso.Acesso;
import br.ufpe.opdee.repositories.AcessoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AcessoService {

    private final AcessoRepository acessoRepository;

//    private final AmbienteRepository ambienteRepository;
    private final ModelMapper modelMapper;

    public List<Acesso> findAll() {
        return acessoRepository.findAll();
    }

    public AcessoService(AcessoRepository acessoRepository, ModelMapper modelMapper) {
        this.acessoRepository = acessoRepository;
//        this.ambienteRepository = ambienteRepository;
        this.modelMapper = modelMapper;
    }

    public List<Acesso> findAcessoByUsuario(Usuario usuario) {
        return acessoRepository.findAcessoByUsuario(usuario);
    }

    public List<Acesso> findAcessoByAmbiente(Ambiente ambiente) {
        return acessoRepository.findAcessoByAmbiente(ambiente);
    }

    public Acesso permissaoAcesso(UUID id){
            var aux = acessoRepository.findById(id);

        if (aux.isPresent()) {
            aux.get().setAtivo(!aux.get().isAtivo());
            return acessoRepository.save(aux.get());
        }
        else {
            return null;
        }
    }

    public Acesso findById(UUID id) {
        return acessoRepository.findById(id).orElse(null);
    }

    public Acesso save(Acesso acesso) {
        return acessoRepository.save(acesso);
    }

    public void delete(UUID id) {
        acessoRepository.deleteById(id);
    }

    public Acesso updateAcesso(UUID id, AcessoUpdate acessoUpdate) {
        Optional<Acesso> optionalAcesso = acessoRepository.findById(id);

        if (optionalAcesso.isPresent()) {
            Acesso existingAcesso = optionalAcesso.get();

            modelMapper.map(acessoUpdate.toAcesso(), existingAcesso);

            return acessoRepository.save(existingAcesso);
        } else {
            return null;
        }
    }
}
