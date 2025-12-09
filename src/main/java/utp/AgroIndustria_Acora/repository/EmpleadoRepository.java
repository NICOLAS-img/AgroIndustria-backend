// src/main/java/utp/AgroIndustria_Acora/repository/EmpleadoRepository.java
package utp.AgroIndustria_Acora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utp.AgroIndustria_Acora.modelo.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
}