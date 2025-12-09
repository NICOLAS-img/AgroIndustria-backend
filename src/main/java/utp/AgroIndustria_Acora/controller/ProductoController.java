// src/main/java/utp/AgroIndustria_Acora/controller/ProductoController.java
package utp.AgroIndustria_Acora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utp.AgroIndustria_Acora.modelo.Producto;
import utp.AgroIndustria_Acora.service.ProductoService;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    
    @Autowired
    private ProductoService service;
    
    @GetMapping
    public List<Producto> listar() {
        return service.listarTodos();  // ← CAMBIAR DE listar() a listarTodos()
    }
    
    @GetMapping("/disponibles")
    public List<Producto> listarDisponibles() {
        return service.listarDisponibles();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarPorId(@PathVariable Long id) {  // ← CAMBIAR Integer a Long
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public Producto guardar(@RequestBody Producto producto) {
        return service.guardar(producto);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto producto) {  // ← Integer a Long
        return service.buscarPorId(id)
                .map(p -> {
                    producto.setId(id);
                    return ResponseEntity.ok(service.guardar(producto));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {  // ← CAMBIAR Integer a Long
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}