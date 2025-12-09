package utp.AgroIndustria_Acora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import utp.AgroIndustria_Acora.modelo.Empleado;
import utp.AgroIndustria_Acora.service.EmpleadoService;

import java.util.List;

@Controller
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    // 1. CARGAR LA VISTA HTML
    @GetMapping("/empleados")
    public String verEmpleados() {
        return "empleados"; // Carga empleados.html
    }

    // --- API REST (Para que funcione el JavaScript) ---

    // 2. LISTAR TODOS (JSON)
    @GetMapping("/api/empleados")
    @ResponseBody
    public List<Empleado> listarApi() {
        return empleadoService.listar();
    }

    // 3. BUSCAR UNO PARA EDITAR (JSON)
    @GetMapping("/api/empleados/{id}")
    @ResponseBody
    public Empleado obtenerPorId(@PathVariable Integer id) {
        return empleadoService.buscarPorId(id);
    }

    // 4. GUARDAR O ACTUALIZAR
    @PostMapping("/api/empleados")
    @ResponseBody
    public String guardarApi(@RequestBody Empleado empleado) {
        try {
            empleadoService.guardar(empleado);
            return "OK";
        } catch (Exception e) {
            return "ERROR";
        }
    }

    // 5. ELIMINAR
  

    // NOTA: @PathVariable Integer id (coincide con el JS)
    @DeleteMapping("/api/empleados/{id}")
    @ResponseBody
    public String eliminarApi(@PathVariable Integer id) {
        try {
            empleadoService.eliminar(id);
            return "OK";
        } catch (Exception e) {
            return "ERROR";
        }
    }
    }