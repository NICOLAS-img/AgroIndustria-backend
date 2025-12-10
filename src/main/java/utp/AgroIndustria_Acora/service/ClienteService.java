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

    // --- TIENDA (Login/Registro) ---

    public Cliente registrarCliente(Cliente cliente) {
        Optional<Cliente> existente = clienteRepository.findByCorreo(cliente.getCorreo());
        if (existente.isPresent()) {
            return null; // Correo duplicado
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

    // --- PANEL ADMINISTRADOR ---

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Integer id) {
        return clienteRepository.findById(id).orElse(null);
    }

    // MÉTODO MEJORADO: Evita borrar la contraseña al editar
    public void guardar(Cliente cliente) {
        // 1. Verificamos si es una edición (tiene ID)
        if (cliente.getId() != null) {
            // Buscamos el cliente original en la BD
            Cliente clienteDB = clienteRepository.findById(cliente.getId()).orElse(null);
            
            if (clienteDB != null) {
                // Si la contraseña nueva viene vacía o nula, mantenemos la antigua
                if (cliente.getContrasenia() == null || cliente.getContrasenia().isEmpty()) {
                    cliente.setContrasenia(clienteDB.getContrasenia());
                }
                // Si el correo viene vacío (por seguridad del JS), mantenemos el original
                if (cliente.getCorreo() == null || cliente.getCorreo().isEmpty()) {
                    cliente.setCorreo(clienteDB.getCorreo());
                }
            }
        }
        // 2. Guardamos
        clienteRepository.save(cliente);
    }

    public void eliminar(Integer id) {
        clienteRepository.deleteById(id);
    }

    public long contarClientes() {
        return clienteRepository.count();
    }
}