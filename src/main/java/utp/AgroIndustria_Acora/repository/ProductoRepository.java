package utp.AgroIndustria_Acora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utp.AgroIndustria_Acora.modelo.Producto;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    // Buscar productos disponibles
    List<Producto> findByDisponibleTrue();
    
    // Buscar productos por categoría
    List<Producto> findByCategoria(String categoria);
    
    // Buscar productos por nombre (búsqueda parcial)
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar productos con stock mayor a 0
    List<Producto> findByStockGreaterThan(Integer cantidad);
}