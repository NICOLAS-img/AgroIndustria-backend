package utp.AgroIndustria_Acora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession; 
import utp.AgroIndustria_Acora.modelo.Cliente;
import utp.AgroIndustria_Acora.service.*; 

import java.util.List;
import java.util.ArrayList;

@Controller
public class HomeController {

    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private EmpleadoService empleadoService;
    @Autowired
    private DetallePedidoService detallePedidoService; 

    // --- RUTA PRINCIPAL ---
    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        String nombreUsuario = (String) session.getAttribute("usuarioLogueado");
        if (nombreUsuario != null) model.addAttribute("nombreUsuario", nombreUsuario);
        model.addAttribute("productos", productoService.listarDisponibles());
        return "index";
    }

    // --- ACCESOS ---
    @GetMapping("/login") public String login() { return "login"; }
    @GetMapping("/registro") public String registro() { return "registro"; }
    @GetMapping("/logout") public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // --- DASHBOARD (RESUMEN) ---
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("totalEmpleados", empleadoService.contarEmpleados());
        model.addAttribute("totalInventario", productoService.sumarStockTotal());
        model.addAttribute("totalStockBajo", productoService.contarStockBajo(5));
        model.addAttribute("ventasHoy", pedidoService.sumarVentasHoy());
        model.addAttribute("productosBajos", productoService.listarStockBajo(5));
        model.addAttribute("empleados", empleadoService.listar());
        return "admindashboard";
    }

    // --- REPORTES (GRÁFICOS) - AQUÍ ESTABA EL FALTANTE ---
    @GetMapping("/admin/reportes")
    public String adminReportes(Model model) {
        
        // 1. KPI Cards (Datos Superiores)
        model.addAttribute("ventasMes", pedidoService.ventasMesActual());
        model.addAttribute("pedidosTotales", pedidoService.contarPedidosTotales());
        model.addAttribute("productoTop", detallePedidoService.productoMasVendido());
        model.addAttribute("totalClientes", clienteService.contarClientes());

        // 2. Tabla Últimas Transacciones
        model.addAttribute("ultimosPedidos", pedidoService.obtenerUltimosPedidos());

        // 3. Gráfico de Barras (Meses)
        List<Object[]> ventasMes = pedidoService.reporteVentasPorMes();
        Double[] montosMes = new Double[12];
        for(int i=0; i<12; i++) montosMes[i] = 0.0;
        
        for (Object[] fila : ventasMes) {
            // Verificación segura para evitar errores si la fecha es nula
            if (fila[0] != null) {
                int mes = ((Number) fila[0]).intValue() - 1;
                if (mes >= 0 && mes < 12) montosMes[mes] = ((Number) fila[1]).doubleValue();
            }
        }
        model.addAttribute("dataMeses", montosMes);

        // 4. Gráfico de Pastel (Categorías)
        List<Object[]> ventasCat = detallePedidoService.reporteVentasPorCategoria();
        List<String> catNombres = new ArrayList<>();
        List<Integer> catCantidades = new ArrayList<>();
        
        for (Object[] fila : ventasCat) {
            catNombres.add((String) fila[0]);
            catCantidades.add(((Number) fila[1]).intValue());
        }
        model.addAttribute("catNombres", catNombres);
        model.addAttribute("catCantidades", catCantidades);

        return "reportes";
    }

    // --- REDIRECCIONES ---
    @GetMapping("/admin/empleados") public String adminEmpleados() { return "redirect:/empleados"; }
    @GetMapping("/admin/productos") public String adminProductos() { return "productos"; }
    @GetMapping("/cajero/dashboard") public String cajeroDashboard() { return "cajerodashboard"; }
    @GetMapping("/vendedor/dashboard") public String vendedorDashboard() { return "vendedordashboard"; }

    // --- PROCESAR LOGIN ---
    @PostMapping("/login")
    public String procesarLogin(@RequestParam("correo") String correo, @RequestParam("password") String password, HttpSession session, Model model) {
        if (correo.equals("admin@acora.com") && password.equals("admin123")) {
            session.setAttribute("usuarioLogueado", "Administrador");
            return "redirect:/admin/dashboard";
        }
        try {
            Cliente cliente = clienteService.autenticarCliente(correo, password);
            if (cliente != null) {
                session.setAttribute("usuarioLogueado", cliente.getNombre());
                session.setAttribute("idUsuario", cliente.getCliente_id());
                return "redirect:/"; 
            } else {
                model.addAttribute("error", "Credenciales incorrectas.");
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error de conexión.");
            return "login";
        }
    }
    
    // --- PROCESAR REGISTRO ---
    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute Cliente cliente, Model model) {
        try {
            Cliente nuevo = clienteService.registrarCliente(cliente);
            if (nuevo != null) return "redirect:/login?exito";
            else {
                model.addAttribute("error", "El correo ya existe.");
                return "registro";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar.");
            return "registro";
        }
    }
    // ==========================================
    // 7. VISTA CLIENTE: MIS PEDIDOS (¡NUEVO!)
    // ==========================================
    @GetMapping("/mis-pedidos")
    public String misPedidos(HttpSession session, Model model) {
        // 1. Validar seguridad: ¿Está logueado?
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/login";
        }

        // 2. Buscar al cliente y sus pedidos
        Cliente cliente = clienteService.buscarPorId(idUsuario);
        List<utp.AgroIndustria_Acora.modelo.Pedido> misCompras = pedidoService.buscarPorCliente(cliente);
        
        // 3. Enviar a la vista
        model.addAttribute("misCompras", misCompras);
        model.addAttribute("nombreUsuario", cliente.getNombre()); // Para el saludo
        
        return "mis_pedidos";
    }
}