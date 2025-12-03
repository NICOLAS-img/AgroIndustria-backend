package utp.AgroIndustria_Acora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utp.AgroIndustria_Acora.modelo.DetalleComprobantePago;
import java.util.List;

public interface DetalleComprobantePagoRepository extends JpaRepository<DetalleComprobantePago, Integer> {

    List<DetalleComprobantePago> findByComprobante_ComprobanteId(Integer comprobanteId);
}
