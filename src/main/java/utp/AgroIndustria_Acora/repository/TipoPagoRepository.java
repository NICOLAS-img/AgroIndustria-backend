// src/main/java/utp/AgroIndustria_Acora/repository/TipoPagoRepository.java
package utp.AgroIndustria_Acora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utp.AgroIndustria_Acora.modelo.TipoPago;

public interface TipoPagoRepository extends JpaRepository<TipoPago, Integer> {
}