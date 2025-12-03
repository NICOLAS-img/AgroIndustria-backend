package utp.AgroIndustria_Acora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utp.AgroIndustria_Acora.modelo.Cliente;
import java.util.Optional;
import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    // Buscar cliente por email
    Optional<Cliente> findByEmail(String email);
    
    // Buscar cliente por DNI
    Optional<Cliente> findByDni(String dni);
    
    // Buscar clientes activos
    List<Cliente> findByActivoTrue();
    
    // Verificar si existe un email
    boolean existsByEmail(String email);
}