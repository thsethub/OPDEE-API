package br.ufpe.opdee.controllers;

import br.ufpe.opdee.models.ambiente.Ambiente;
import br.ufpe.opdee.models.ambiente.AmbienteRequest;
import br.ufpe.opdee.services.AmbienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/ambiente")
public class AmbienteController {

    private final AmbienteService ambienteService;

    public AmbienteController(AmbienteService ambienteService) {
        this.ambienteService = ambienteService;
    }

    @PostMapping
    public ResponseEntity save(@RequestBody Ambiente ambiente) {
        return ResponseEntity.ok(ambienteService.save(ambiente));
    }

    @GetMapping("/onlyOne/{id}")
    public ResponseEntity findById(@PathVariable UUID id) {
        return ResponseEntity.ok(ambienteService.findById(id));
    }

    @GetMapping("/{nome}")
    public ResponseEntity findAmbiente(@RequestParam String nome) {
        return ResponseEntity.ok(ambienteService.buscarPorNome(nome));
    }

    @GetMapping
    public ResponseEntity findAll() {
        return ResponseEntity.ok(ambienteService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        ambienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public  ResponseEntity updateAmbiente(@PathVariable UUID id, @RequestBody AmbienteRequest ambiente){
        Ambiente updatedAmbiente = ambienteService.updateAmbiente(id, ambiente);
        return ResponseEntity.ok(updatedAmbiente);
    }
}
