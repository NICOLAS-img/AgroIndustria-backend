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
    private DetallePedidoRepository detallePedidoRepository;
    
    public List<DetallePedido> listarTodos() {
        return detallePedidoRepository.findAll();
    }
    
    public Optional<DetallePedido> buscarPorId(Long id) {
        return detallePedidoRepository.findById(id);
    }
    
    public DetallePedido guardar(DetallePedido detalle) {
        return detallePedidoRepository.save(detalle);
    }
    
    public void eliminar(Long id) {
        detallePedidoRepository.deleteById(id);
    }
    
    public List<DetallePedido> listarPorPedidoId(Long pedidoId) {
        return detallePedidoRepository.findByPedidoId(pedidoId);
    }
}