// src/main/java/utp/AgroIndustria_Acora/repository/ComprobanteRepository.java
package utp.AgroIndustria_Acora.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import utp.AgroIndustria_Acora.modelo.Comprobante;

public interface ComprobanteRepository extends JpaRepository<Comprobante, Integer> {

    Optional<Comprobante> findById(Integer id);

    void deleteById(Integer id);

    List<Comprobante> findByTipoComprobante(String tipo);
}