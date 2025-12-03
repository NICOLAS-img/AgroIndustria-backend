package utp.AgroIndustria_Acora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import utp.AgroIndustria_Acora.service.ProductoService;
import utp.AgroIndustria_Acora.service.PedidoService;
import utp.AgroIndustria_Acora.service.ClienteService;

@Controller
public class HomeController {
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private ClienteService clienteService;
    
    // ============================================
    // RUTAS PÚBLICAS
    // ============================================
    
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    // ============================================
    // RUTAS DEL CLIENTE
    // ============================================
    
    @GetMapping("/cliente/dashboard")
    public String clienteDashboard(Model model) {
        // Aquí puedes pasar datos reales a la vista
        Long pedidosPendientes = pedidoService.contarPorEstado("PENDIENTE");
        model.addAttribute("pedidosPendientes", pedidosPendientes);
        return "cliente/dashboard";
    }
    
    // ============================================
    // RUTAS DEL ADMIN
    // ============================================
    
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        // Pasar estadísticas reales a la vista
        Long totalClientes = (long) clienteService.listarTodos().size();
        Long totalProductos = (long) productoService.listarTodos().size();
        Long totalPedidos = (long) pedidoService.listarTodos().size();
        Long pedidosPendientes = pedidoService.contarPorEstado("PENDIENTE");
        
        model.addAttribute("totalClientes", totalClientes);
        model.addAttribute("totalProductos", totalProductos);
        model.addAttribute("totalPedidos", totalPedidos);
        model.addAttribute("pedidosPendientes", pedidosPendientes);
        
        return "admin/dashboard";
    }
}