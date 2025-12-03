package utp.AgroIndustria_Acora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import utp.AgroIndustria_Acora.modelo.Comprobante;
import utp.AgroIndustria_Acora.service.ComprobanteService;
import utp.AgroIndustria_Acora.service.PedidoService;

import java.util.List;

@Controller
@RequestMapping("/comprobantes")
public class ComprobanteController {

    @Autowired
    private ComprobanteService service;

    @Autowired
    private PedidoService pedidoService;

    // LISTAR TODOS
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("comprobantes", service.listarTodos());
        return "comprobantes/listar"; 
    }

    // LISTAR PEDIDOS (si lo necesitas)
    @GetMapping("/pedidos")
    public String listarPedidos(Model model) {
        model.addAttribute("pedidos", pedidoService.listarTodos());
        return "comprobantes/pedidos";
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    public String buscarPorId(@PathVariable Integer id, Model model) {

        Comprobante comprobante = service.buscarPorId(id); // YA NO ES Optional

        model.addAttribute("comprobante", comprobante);
        return "comprobantes/detalle";
    }

    // LISTAR POR TIPO
    @GetMapping("/tipo/{tipo}")
    public String listarPorTipo(@PathVariable String tipo, Model model) {
        List<Comprobante> lista = service.buscarPorTipo(tipo);
        model.addAttribute("comprobantes", lista);
        return "comprobantes/listar";
    }

    // FORMULARIO NUEVO
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("comprobante", new Comprobante());
        return "comprobantes/form";
    }

    // GUARDAR
    @PostMapping
    public String guardar(@ModelAttribute Comprobante comprobante) {
        service.guardar(comprobante);
        return "redirect:/comprobantes";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return "redirect:/comprobantes";
    }
}

