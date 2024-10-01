package br.ufpe.opdee.controllers;

import br.ufpe.opdee.models.Perfil;
import br.ufpe.opdee.services.PerfilService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        perfilService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity findAll() {
        return ResponseEntity.ok(perfilService.findAll());
    }



}
