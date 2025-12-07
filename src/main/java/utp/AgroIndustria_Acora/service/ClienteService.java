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

    /**
     * Intenta registrar un nuevo cliente en la Base de Datos.
     * Si el correo ya existe, retorna null.
     */
    public Cliente registrarCliente(Cliente cliente) {
        // 1. Verificar si el correo ya está registrado (Consulta a la BD)
        Optional<Cliente> existente = clienteRepository.findByCorreo(cliente.getCorreo());
        
        if (existente.isPresent()) {
            return null; // Correo duplicado
        }

        // 2. Guardar cliente nuevo en MySQL
        // Nota: En un entorno real, la contraseña se encriptaría (hash) aquí.
        return clienteRepository.save(cliente);
    }

    /**
     * Intenta autenticar a un cliente comparando el correo y la contraseña.
     * @return El objeto Cliente si las credenciales son correctas, o null si fallan.
     */
    public Cliente autenticarCliente(String correo, String password) {
        // 1. Buscar el cliente por correo
        Optional<Cliente> clienteOpt = clienteRepository.findByCorreo(correo);
        
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            
            // 2. Comparar la contraseña (texto plano)
            if (cliente.getContrasenia().equals(password)) {
                return cliente; // Autenticación exitosa
            }
        }
        
        // Falla: Cliente no encontrado o contraseña incorrecta
        return null; 
    }
}