package br.ufpe.opdee.controllers;


import br.ufpe.opdee.models.acesso.Acesso;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.ufpe.opdee.services.AcessoService;

import java.util.UUID;

@RestController
@RequestMapping("/api/acesso")
public class AcessoController {

    public final AcessoService acessoService;

    public AcessoController(AcessoService acessoService) {
        this.acessoService = acessoService;
    }

    @PostMapping
    public ResponseEntity save(@RequestBody Acesso acesso) {
        return ResponseEntity.ok(acessoService.save(acesso));
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable UUID id) {
        return ResponseEntity.ok(acessoService.findById(id));
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

}
