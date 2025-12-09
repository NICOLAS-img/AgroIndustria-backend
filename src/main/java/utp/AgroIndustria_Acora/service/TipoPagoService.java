// src/main/java/utp/AgroIndustria_Acora/service/TipoPagoService.java
package utp.AgroIndustria_Acora.service;

import org.springframework.stereotype.Service;
import utp.AgroIndustria_Acora.modelo.TipoPago;
import utp.AgroIndustria_Acora.repository.TipoPagoRepository;

import java.util.List;

@Service
public class TipoPagoService {

    private final TipoPagoRepository repo;

    public TipoPagoService(TipoPagoRepository repo) {
        this.repo = repo;
    }

    public List<TipoPago> listar() {
        return repo.findAll();
    }

    public TipoPago buscarPorId(Integer id) {
        return repo.findById(id).orElse(null);
    }

    public void guardar(TipoPago tipoPago) {
        repo.save(tipoPago);
    }

    public void eliminar(Integer id) {
        repo.deleteById(id);
    }
}