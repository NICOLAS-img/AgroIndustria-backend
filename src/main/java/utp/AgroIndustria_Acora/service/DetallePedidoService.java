package utp.AgroIndustria_Acora.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utp.AgroIndustria_Acora.modelo.DetallePedido;
import utp.AgroIndustria_Acora.repository.DetallePedidoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DetallePedidoService {
    
    @Autowired
    private DetallePedidoRepository repo;
    
    public List<DetallePedido> listarTodos() { return repo.findAll(); }
    public Optional<DetallePedido> buscarPorId(Long id) { return repo.findById(id); }
    public DetallePedido guardar(DetallePedido detalle) { return repo.save(detalle); }
    public void eliminar(Long id) { repo.deleteById(id); }
    public List<DetallePedido> listarPorPedidoId(Long pedidoId) { return repo.findByPedidoId(pedidoId); }

    // --- MÉTODOS REPORTES ---

    public List<Object[]> reporteVentasPorCategoria() {
        return repo.obtenerVentasPorCategoria();
    }

    public String productoMasVendido() {
        List<String> top = repo.encontrarProductoTop();
        if (top == null || top.isEmpty()) {
            return "Sin Ventas";
        }
        return top.get(0); // Devuelve el primero de la lista
    }
}