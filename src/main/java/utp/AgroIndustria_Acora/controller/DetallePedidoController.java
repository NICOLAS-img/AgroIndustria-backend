// src/main/java/utp/AgroIndustria_Acora/controller/DetallePedidoController.java
package utp.AgroIndustria_Acora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utp.AgroIndustria_Acora.modelo.DetallePedido;
import utp.AgroIndustria_Acora.service.DetallePedidoService;
import utp.AgroIndustria_Acora.service.ProductoService;

import java.util.List;

@RestController
@RequestMapping("/api/detalle-pedidos")
public class DetallePedidoController {
    
    @Autowired
    private DetallePedidoService service;
    
    @Autowired
    private ProductoService productoService;
    
    @GetMapping
    public List<DetallePedido> listar() {
        return service.listarTodos();
    }
    
    @GetMapping("/productos")
    public ResponseEntity<?> listarProductos() {
        return ResponseEntity.ok(productoService.listarTodos());  // ← CAMBIAR listar() a listarTodos()
    }
    
    @GetMapping("/pedido/{pedidoId}")
    public List<DetallePedido> listarPorPedido(@PathVariable Long pedidoId) {
        return service.listarPorPedidoId(pedidoId);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DetallePedido> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public DetallePedido guardar(@RequestBody DetallePedido detalle) {
        return service.guardar(detalle);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}