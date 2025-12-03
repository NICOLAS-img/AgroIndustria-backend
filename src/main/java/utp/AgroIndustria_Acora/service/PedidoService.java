package utp.AgroIndustria_Acora.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utp.AgroIndustria_Acora.modelo.Pedido;
import utp.AgroIndustria_Acora.modelo.Cliente;
import utp.AgroIndustria_Acora.repository.PedidoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }
    
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }
    
    public Pedido guardar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }
    
    public void eliminar(Long id) {
        pedidoRepository.deleteById(id);
    }
    
    public List<Pedido> buscarPorEstado(String estado) {
        return pedidoRepository.findByEstado(estado);
    }
    
    public List<Pedido> buscarPorCliente(Cliente cliente) {
        return pedidoRepository.findByCliente(cliente);
    }
    
    public Long contarPorEstado(String estado) {
        return pedidoRepository.countByEstado(estado);
    }
}