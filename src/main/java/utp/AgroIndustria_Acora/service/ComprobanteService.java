package utp.AgroIndustria_Acora.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utp.AgroIndustria_Acora.modelo.Comprobante;
import utp.AgroIndustria_Acora.repository.ComprobanteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ComprobanteService {
    
   @Autowired
    private ComprobanteRepository comprobanteRepository;
    
    public List<Comprobante> listarTodos() {
        return comprobanteRepository.findAll();
    }
    
    public Comprobante buscarPorId(Integer id) {
        return comprobanteRepository.findById(id).orElse(null);
    }
    
    public Comprobante guardar(Comprobante comprobante) {
        return comprobanteRepository.save(comprobante);
    }
    
    public void eliminar(Integer id) {
        comprobanteRepository.deleteById(id);
    }
    
    public List<Comprobante> buscarPorTipo(String tipo) {
        return comprobanteRepository.findByTipoComprobante(tipo);
    }
}
