package utp.AgroIndustria_Acora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utp.AgroIndustria_Acora.modelo.Cliente;

import java.util.Optional;

// Interfaz mágica de Spring Data JPA
// <Entidad, Tipo de ID>
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    /**
     * Busca un cliente por su correo electrónico.
     * Spring genera el SQL automáticamente: SELECT * FROM cliente WHERE correo = ?
     */
    Optional<Cliente> findByCorreo(String correo);
    
    /**
     * Verifica si existe un correo (útil para no duplicar registros).
     */
    boolean existsByCorreo(String correo);
}