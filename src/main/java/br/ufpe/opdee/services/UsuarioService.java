package br.ufpe.opdee.services;

import br.ufpe.opdee.models.Perfil;
import br.ufpe.opdee.models.Usuario;
import br.ufpe.opdee.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario findById(UUID id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public void delete(UUID id) {
        usuarioRepository.deleteById(id);
    }
}
