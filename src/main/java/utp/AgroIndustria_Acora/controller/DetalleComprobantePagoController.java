// src/main/java/utp/AgroIndustria_Acora/controller/DetalleComprobantePagoController.java
package utp.AgroIndustria_Acora.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import utp.AgroIndustria_Acora.modelo.Comprobante;
import utp.AgroIndustria_Acora.modelo.DetalleComprobantePago;
import utp.AgroIndustria_Acora.service.DetalleComprobantePagoService;
import utp.AgroIndustria_Acora.service.ComprobanteService;
import utp.AgroIndustria_Acora.service.TipoPagoService;

@Controller
@RequestMapping("/detalle-comprobante")
public class DetalleComprobantePagoController {

    private final DetalleComprobantePagoService service;
    private final ComprobanteService comprobanteService;
    private final TipoPagoService tipoPagoService;

    public DetalleComprobantePagoController(DetalleComprobantePagoService service,
                                            ComprobanteService comprobanteService,
                                            TipoPagoService tipoPagoService){
        this.service = service;
        this.comprobanteService = comprobanteService;
        this.tipoPagoService = tipoPagoService;
    }

    @GetMapping("/lista/{compId}")
    public String listar(@PathVariable Integer compId, Model model) {
        model.addAttribute("comprobante", comprobanteService.buscarPorId(compId));
        model.addAttribute("detalles", service.listarPorComprobante(compId));
        return "detallecomprobante/lista";
    }

    @GetMapping("/nuevo/{compId}")
    public String nuevo(@PathVariable Integer compId, Model model) {
        DetalleComprobantePago detalle = new DetalleComprobantePago();
        detalle.setComprobante(comprobanteService.buscarPorId(compId));

        model.addAttribute("detalle", detalle);
        model.addAttribute("tiposPago", tipoPagoService.listar());
        return "detallecomprobante/form";
    }

    @PostMapping
    public String guardar(DetalleComprobantePago detalle) {

        Integer compId = detalle.getComprobante().getComprobanteId();
        detalle.setComprobante(comprobanteService.buscarPorId(compId));

        service.guardar(detalle);
        return "redirect:/detalle-comprobante/lista/" + compId;
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        DetalleComprobantePago det = service.buscarPorId(id);

        Integer compId = det.getComprobante().getComprobanteId();
        service.eliminar(id);

        return "redirect:/detalle-comprobante/lista/" + compId;
    }
}