package utp.AgroIndustria_Acora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utp.AgroIndustria_Acora.modelo.Pedido;
import utp.AgroIndustria_Acora.modelo.Cliente;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    // Buscar pedidos por cliente
    List<Pedido> findByCliente(Cliente cliente);
    
    // Buscar pedidos por estado
    List<Pedido> findByEstado(String estado);
    
    // Buscar pedidos de un cliente por estado
    List<Pedido> findByClienteAndEstado(Cliente cliente, String estado);
    
    // Contar pedidos por estado
    Long countByEstado(String estado);
}