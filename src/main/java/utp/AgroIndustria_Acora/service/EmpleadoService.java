package utp.AgroIndustria_Acora.service;

import org.springframework.stereotype.Service;
import utp.AgroIndustria_Acora.modelo.Empleado;
import utp.AgroIndustria_Acora.repository.EmpleadoRepository;

import java.util.List;

@Service
public class EmpleadoService {

    private final EmpleadoRepository repo;

    public EmpleadoService(EmpleadoRepository repo) {
        this.repo = repo;
    }

    public List<Empleado> listar() {
        return repo.findAll();
    }

    public Empleado buscarPorId(Integer id) {
        return repo.findById(id).orElse(null);
    }

    public void guardar(Empleado empleado) {
        repo.save(empleado);
    }

    public void eliminar(Integer id) {
        repo.deleteById(id);
    }
}
