package utp.AgroIndustria_Acora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utp.AgroIndustria_Acora.modelo.Cliente;
import utp.AgroIndustria_Acora.service.ClienteService;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    
    @Autowired
    private ClienteService service;
    
    @GetMapping
    public List<Cliente> listar() {
        return service.listarTodos();  // ← CAMBIO: listar() → listarTodos()
    }
    
    @GetMapping("/activos")
    public List<Cliente> listarActivos() {
        return service.listarActivos();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {  // ← CAMBIO: Integer → Long
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public Cliente guardar(@RequestBody Cliente cliente) {
        return service.guardar(cliente);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(@PathVariable Long id, @RequestBody Cliente cliente) {  // ← CAMBIO: Integer → Long
        return service.buscarPorId(id)
                .map(c -> {
                    cliente.setId(id);
                    return ResponseEntity.ok(service.guardar(cliente));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {  // ← CAMBIO: Integer → Long
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}