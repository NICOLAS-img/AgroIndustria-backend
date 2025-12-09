package utp.AgroIndustria_Acora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import utp.AgroIndustria_Acora.modelo.Producto;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    // Consultas básicas
    List<Producto> findByDisponibleTrue();
    List<Producto> findByCategoria(String categoria);
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    List<Producto> findByStockGreaterThan(Integer cantidad);

    // --- DASHBOARD: Consultas de Stock ---
    
    // 1. Contar productos con poco stock
    Long countByStockLessThan(Integer cantidad);

    // 2. Sumar todo el inventario (Evita error si es null con COALESCE)
    @Query("SELECT COALESCE(SUM(p.stock), 0) FROM Producto p")
    Long sumStockTotal();
    
    // 3. Listar los productos críticos
    List<Producto> findByStockLessThan(Integer cantidad);
}