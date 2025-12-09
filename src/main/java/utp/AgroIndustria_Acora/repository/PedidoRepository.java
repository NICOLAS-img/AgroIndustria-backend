package utp.AgroIndustria_Acora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import utp.AgroIndustria_Acora.modelo.Pedido;
import utp.AgroIndustria_Acora.modelo.Cliente;
import java.util.List;
import java.math.BigDecimal;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    // Métodos que ya usabas
    List<Pedido> findByCliente(Cliente cliente);
    List<Pedido> findByEstado(String estado);
    List<Pedido> findByClienteAndEstado(Cliente cliente, String estado);
    Long countByEstado(String estado);

    // --- NUEVOS PARA DASHBOARD Y REPORTES ---

    // 1. Ventas de HOY
    @Query("SELECT COALESCE(SUM(p.total), 0) FROM Pedido p WHERE DATE(p.fechaPedido) = CURRENT_DATE")
    BigDecimal sumVentasHoy();

    // 2. Ventas del MES ACTUAL
    @Query("SELECT COALESCE(SUM(p.total), 0) FROM Pedido p WHERE MONTH(p.fechaPedido) = MONTH(CURRENT_DATE) AND YEAR(p.fechaPedido) = YEAR(CURRENT_DATE)")
    BigDecimal sumVentasMesActual();

    // 3. Últimos 5 Pedidos
    List<Pedido> findTop5ByOrderByFechaPedidoDesc();

    // 4. Datos para gráfico de barras (Meses) - Usamos SQL Nativo para asegurar compatibilidad
    @Query(value = "SELECT MONTH(fecha_pedido) as mes, SUM(total) as monto FROM pedidos GROUP BY MONTH(fecha_pedido)", nativeQuery = true)
    List<Object[]> obtenerVentasPorMes();
}