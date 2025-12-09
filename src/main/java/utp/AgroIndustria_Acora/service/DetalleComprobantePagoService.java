// src/main/java/utp/AgroIndustria_Acora/service/DetalleComprobantePagoService.java
package utp.AgroIndustria_Acora.service;

import org.springframework.stereotype.Service;
import utp.AgroIndustria_Acora.modelo.DetalleComprobantePago;
import utp.AgroIndustria_Acora.repository.DetalleComprobantePagoRepository;

import java.util.List;

@Service
public class DetalleComprobantePagoService {

    private final DetalleComprobantePagoRepository repo;

    public DetalleComprobantePagoService(DetalleComprobantePagoRepository repo) {
        this.repo = repo;
    }

    public List<DetalleComprobantePago> listarPorComprobante(Integer compId) {
        return repo.findByComprobante_ComprobanteId(compId);
    }

    public DetalleComprobantePago buscarPorId(Integer id) {
        return repo.findById(id).orElse(null);
    }

    public void guardar(DetalleComprobantePago detalle) {
        repo.save(detalle);
    }

    public void eliminar(Integer id) {
        repo.deleteById(id);
    }
}