package utp.AgroIndustria_Acora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import utp.AgroIndustria_Acora.modelo.DetallePedido;
import java.util.List;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    
    List<DetallePedido> findByPedidoId(Long pedidoId);

    // 1. Encontrar el producto más vendido (Nombre)
    @Query("SELECT d.producto.nombre FROM DetallePedido d GROUP BY d.producto.nombre ORDER BY SUM(d.cantidad) DESC")
    List<String> encontrarProductoTop();

    // 2. Datos para gráfico de pastel (Categorías)
    @Query("SELECT d.producto.categoria, SUM(d.cantidad) FROM DetallePedido d GROUP BY d.producto.categoria")
    List<Object[]> obtenerVentasPorCategoria();
}