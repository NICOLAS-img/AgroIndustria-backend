package utp.AgroIndustria_Acora.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utp.AgroIndustria_Acora.modelo.Cliente;
import utp.AgroIndustria_Acora.repository.ClienteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }
    
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }
    
    public Cliente guardar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
    
    public void eliminar(Long id) {
        clienteRepository.deleteById(id);
    }
    
    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }
    
    public List<Cliente> listarActivos() {
        return clienteRepository.findByActivoTrue();
    }
    
    public boolean existeEmail(String email) {
        return clienteRepository.existsByEmail(email);
    }
}