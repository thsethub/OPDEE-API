package br.ufpe.opdee.controllers;

import br.ufpe.opdee.models.perfil.Perfil;
import br.ufpe.opdee.models.perfil.PerfilRequest;
import br.ufpe.opdee.models.perfil.PerfilResponse;
import br.ufpe.opdee.services.PerfilService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/perfil")
public class PerfilController {

    private final PerfilService perfilService;

    private PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @PostMapping
    public ResponseEntity save(@RequestBody Perfil perfil) {
        return ResponseEntity.ok(perfilService.save(perfil));
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable UUID id) {
        return ResponseEntity.ok(perfilService.findById(id));
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<PerfilResponse> findByNome(@PathVariable String nome) {
        return ResponseEntity.ok(new PerfilResponse(perfilService.findByNome(nome)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        perfilService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity findAll() {
        return ResponseEntity.ok(perfilService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity updatePerfil(@PathVariable UUID id, @RequestBody PerfilRequest perfilRequest){
        Perfil updatedPerfil = perfilService.updatePerfil(id, perfilRequest);
        return ResponseEntity.ok(updatedPerfil);
    }



}
