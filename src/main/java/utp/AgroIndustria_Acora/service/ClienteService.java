package utp.AgroIndustria_Acora.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utp.AgroIndustria_Acora.modelo.Cliente;
import utp.AgroIndustria_Acora.repository.ClienteRepository;

import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente registrarCliente(Cliente cliente) {
        Optional<Cliente> existente = clienteRepository.findByCorreo(cliente.getCorreo());
        if (existente.isPresent()) {
            return null;
        }
        return clienteRepository.save(cliente);
    }

    public Cliente autenticarCliente(String correo, String password) {
        Optional<Cliente> clienteOpt = clienteRepository.findByCorreo(correo);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            if (cliente.getContrasenia().equals(password)) {
                return cliente;
            }
        }
        return null; 
    }

    // --- MÉTODOS NUEVOS NECESARIOS ---

    // 1. Buscar por ID (Usado en PedidoController)
    public Cliente buscarPorId(Integer id) {
        return clienteRepository.findById(id).orElse(null);
    }

    // 2. Contar Clientes Totales (Usado en Reportes)
    public long contarClientes() {
        return clienteRepository.count();
    }
}