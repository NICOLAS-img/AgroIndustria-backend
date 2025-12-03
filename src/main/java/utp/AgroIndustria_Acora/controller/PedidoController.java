package utp.AgroIndustria_Acora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utp.AgroIndustria_Acora.modelo.Pedido;
import utp.AgroIndustria_Acora.service.PedidoService;
import utp.AgroIndustria_Acora.service.ClienteService;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    
    @Autowired
    private PedidoService service;
    
    @Autowired
    private ClienteService clienteService;
    
    @GetMapping
    public List<Pedido> listar() {
        return service.listarTodos();  // ← CAMBIO: listar() → listarTodos()
    }
    
    @GetMapping("/clientes")
    public ResponseEntity<?> listarClientes() {
        return ResponseEntity.ok(clienteService.listarTodos());  // ← CAMBIO: listar() → listarTodos()
    }
    
    @GetMapping("/estado/{estado}")
    public List<Pedido> listarPorEstado(@PathVariable String estado) {
        return service.buscarPorEstado(estado);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {  // ← CAMBIO: Integer → Long
        return service.buscarPorId(id)
                .map(pedido -> {
                    // Aquí puedes agregar lógica adicional si necesitas
                    return ResponseEntity.ok(pedido);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public Pedido guardar(@RequestBody Pedido pedido) {
        return service.guardar(pedido);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> actualizar(@PathVariable Long id, @RequestBody Pedido pedido) {  // ← CAMBIO: Integer → Long
        return service.buscarPorId(id)
                .map(p -> {
                    pedido.setId(id);
                    return ResponseEntity.ok(service.guardar(pedido));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {  // ← CAMBIO: Integer → Long
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}