package br.ufpe.opdee.services;

import br.ufpe.opdee.models.Usuario;
import br.ufpe.opdee.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario findById(String id) {  // Usando String como tipo de ID
        return usuarioRepository.findById(id).orElse(null);
    }

    public void delete(String id) {  // Consistente com o tipo String
        usuarioRepository.deleteById(id);
    }
}
