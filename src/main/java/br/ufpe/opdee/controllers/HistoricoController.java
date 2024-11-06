package br.ufpe.opdee.controllers;


import br.ufpe.opdee.models.historico.HistoricoRequest;
import br.ufpe.opdee.models.historico.HistoricoResponse;
import br.ufpe.opdee.services.AmbienteService;
import br.ufpe.opdee.services.HistoricoService;
import br.ufpe.opdee.services.PerfilService;
import br.ufpe.opdee.services.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/historico")
public class HistoricoController {

    private final HistoricoService historicoService;
    private final UsuarioService usuarioService;
    private final PerfilService perfilService;
    private final AmbienteService ambienteService;

    public HistoricoController(HistoricoService historicoService, UsuarioService usuarioService, PerfilService perfilService, AmbienteService ambienteService) {
        this.historicoService = historicoService;
        this.ambienteService = ambienteService;
        this.usuarioService = usuarioService;
        this.perfilService = perfilService;
    }

    @PostMapping
    public ResponseEntity save(@RequestBody HistoricoRequest historico) {

        var ambiente = ambienteService.findById(historico.ambienteId());
        var usuario = usuarioService.findById(historico.usuarioId());
        var perfil = perfilService.findById(historico.perfilId());
        var aux = historico.criarRegistro(usuario, perfil, ambiente);
        return ResponseEntity.ok(historicoService.save(aux));
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable UUID id) {
        return ResponseEntity.ok(historicoService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        historicoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<HistoricoResponse>> findAll() {
        var aux = historicoService.findAll();
        return ResponseEntity.ok(aux.stream().map(HistoricoResponse::new).toList());
    }




}
