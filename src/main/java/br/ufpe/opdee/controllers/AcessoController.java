package br.ufpe.opdee.controllers;


import br.ufpe.opdee.models.acesso.*;
import br.ufpe.opdee.models.ambiente.Ambiente;
import br.ufpe.opdee.models.Usuario;
import br.ufpe.opdee.repositories.AcessoRepository;
import br.ufpe.opdee.services.AmbienteService;
import br.ufpe.opdee.services.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.ufpe.opdee.services.AcessoService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/acesso")
public class AcessoController {

    private final AcessoService acessoService;
    private final AmbienteService ambienteService;
    private final UsuarioService usuarioService;
    private final AcessoRepository acessoRepository;

    public AcessoController(AcessoService acessoService, AmbienteService ambienteService, UsuarioService usuarioService, AcessoRepository acessoRepository) {
        this.acessoService = acessoService;
        this.ambienteService = ambienteService;
        this.usuarioService = usuarioService;
        this.acessoRepository = acessoRepository;
    }

    @PostMapping
    public ResponseEntity save(@RequestBody AcessoRequest acesso) {

        var ambiente = ambienteService.findById(acesso.ambienteId());
        var usuario = usuarioService.findById(acesso.usuarioId());
        var aux = acesso.criarAcesso(usuario, ambiente);
        return ResponseEntity.ok(acessoService.save(aux));
    }

    @GetMapping("/{usuario}")
    public ResponseEntity<List<AcessoResponse>> findAcessoByUsuario(@PathVariable Usuario usuario) {
        var aux = acessoService.findAcessoByUsuario(usuario);
        return ResponseEntity.ok(aux.stream().map(AcessoResponse::new).toList());
    }

    @GetMapping("/ambiente/{ambiente}")
    public ResponseEntity<List<UserAcessoResponse>> findAcessoByAmbiente(@PathVariable Ambiente ambiente){
        var aux = acessoService.findAcessoByAmbiente(ambiente);
        return ResponseEntity.ok(aux.stream().map(UserAcessoResponse::new).toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Acesso> permissaoAcesso(@PathVariable UUID id){
        var aux = acessoService.permissaoAcesso(id);
        return ResponseEntity.ok(aux);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        acessoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity findAll() {
        return ResponseEntity.ok(acessoService.findAll());
    }

    @PutMapping("/perfil/{id}")
    public ResponseEntity updateAcesso(@PathVariable UUID id, @RequestBody AcessoUpdate acesso){
        Acesso updatedAcesso = acessoService.updateAcesso(id, acesso);
        return ResponseEntity.ok(updatedAcesso);
    }

}
