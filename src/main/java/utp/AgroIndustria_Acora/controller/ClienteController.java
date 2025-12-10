package utp.AgroIndustria_Acora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utp.AgroIndustria_Acora.modelo.Cliente;
import utp.AgroIndustria_Acora.service.ClienteService;

import java.util.List;

@Controller
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // ==========================================
    // 1. VISTA HTML (La página web)
    // ==========================================

    // Esta ruta carga el archivo HTML bonito donde estará la tabla
    @GetMapping("/admin/clientes")
    public String verClientes() {
        // Buscamos "clientes.html" en la carpeta templates
        return "clientes"; 
    }

    // ==========================================
    // 2. API REST (Los datos para el JavaScript)
    // ==========================================

    // A) Listar todos los clientes (Devuelve JSON)
    @GetMapping("/api/clientes")
    @ResponseBody
    public List<Cliente> listarApi() {
        return clienteService.listarTodos();
    }

    // B) Buscar uno por ID (Para llenar el formulario de editar)
    @GetMapping("/api/clientes/{id}")
    @ResponseBody
    public Cliente obtenerApi(@PathVariable Integer id) {
        return clienteService.buscarPorId(id);
    }

    // C) Guardar o Editar (Recibe los datos del modal)
    @PostMapping("/api/clientes")
    @ResponseBody
    public String guardarApi(@RequestBody Cliente cliente) {
        try {
            // El servicio decide si es nuevo o actualización
            clienteService.guardar(cliente); 
            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR: No se pudo guardar.";
        }
    }

    // D) Eliminar Cliente
    @DeleteMapping("/api/clientes/{id}")
    @ResponseBody
    public String eliminarApi(@PathVariable Integer id) {
        try {
            clienteService.eliminar(id);
            return "OK";
        } catch (Exception e) {
            // Este mensaje sale si intentas borrar a un cliente que ya compró algo
            return "ERROR: No se puede eliminar. El cliente tiene compras registradas.";
        }
    }
}