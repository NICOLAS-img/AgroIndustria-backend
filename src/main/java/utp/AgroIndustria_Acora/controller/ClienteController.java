package utp.AgroIndustria_Acora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import utp.AgroIndustria_Acora.repository.ClienteRepository;

@Controller
@RequestMapping("/admin/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    // Listar todos los clientes en el Dashboard
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteRepository.findAll());
        return "cliente/lista"; // Necesitarás crear templates/cliente/lista.html
    }
    
    // Aquí podrías agregar métodos para editar o eliminar clientes si el admin lo requiere
}