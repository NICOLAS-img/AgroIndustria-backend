package utp.AgroIndustria_Acora.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import utp.AgroIndustria_Acora.modelo.TipoPago;
import utp.AgroIndustria_Acora.service.TipoPagoService;

@Controller
@RequestMapping("/tipo-pago")
public class TipoPagoController {

    private final TipoPagoService service;

    public TipoPagoController(TipoPagoService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("tiposPago", service.listar());
        return "tipopago/lista";  // templates/tipopago/lista.html
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("tipoPago", new TipoPago());
        return "tipopago/form";   // templates/tipopago/form.html
    }

    @PostMapping
    public String guardar(TipoPago tipoPago) {
        service.guardar(tipoPago);
        return "redirect:/tipo-pago";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        model.addAttribute("tipoPago", service.buscarPorId(id));
        return "tipopago/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return "redirect:/tipo-pago";
    }
}
