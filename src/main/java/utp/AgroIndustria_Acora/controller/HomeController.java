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
        // Mantenemos la alerta en 11 para que coincida con el color rojo de la tabla
        model.addAttribute("totalStockBajo", productoService.contarStockBajo(11));
        model.addAttribute("ventasHoy", pedidoService.sumarVentasHoy());
        model.addAttribute("productosBajos", productoService.listarStockBajo(11));
        model.addAttribute("empleados", empleadoService.listar());
        return "admindashboard";
    }

    // --- REPORTES (GRÁFICOS) ---
    @GetMapping("/admin/reportes")
    public String adminReportes(Model model) {
        
        // 1. KPI Cards
        model.addAttribute("ventasMes", pedidoService.ventasMesActual());
        model.addAttribute("pedidosTotales", pedidoService.contarPedidosTotales());
        model.addAttribute("productoTop", detallePedidoService.productoMasVendido());
        model.addAttribute("totalClientes", clienteService.contarClientes());

        // 2. Tabla Últimas Transacciones
        model.addAttribute("ultimosPedidos", pedidoService.obtenerUltimosPedidos());

        // 3. Gráfico de Barras
        List<Object[]> ventasMes = pedidoService.reporteVentasPorMes();
        Double[] montosMes = new Double[12];
        for(int i=0; i<12; i++) montosMes[i] = 0.0;
        
        for (Object[] fila : ventasMes) {
            if (fila[0] != null) {
                int mes = ((Number) fila[0]).intValue() - 1;
                if (mes >= 0 && mes < 12) montosMes[mes] = ((Number) fila[1]).doubleValue();
            }
        }
        model.addAttribute("dataMeses", montosMes);

        // 4. Gráfico de Pastel
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

    // --- REDIRECCIONES MENÚ ADMIN ---
    @GetMapping("/admin/empleados") public String adminEmpleados() { return "redirect:/empleados"; }
    @GetMapping("/admin/productos") public String adminProductos() { return "productos"; }
    // Ojo: La de clientes ahora tiene su propio controlador, pero esto no estorba.
    
    // --- DASHBOARDS EMPLEADOS ---
    @GetMapping("/cajero/dashboard") public String cajeroDashboard() { return "cajerodashboard"; }
    @GetMapping("/vendedor/dashboard") public String vendedorDashboard() { return "vendedordashboard"; }

    // --- PROCESAR LOGIN ---
    @PostMapping("/login")
    public String procesarLogin(@RequestParam("correo") String correo, @RequestParam("password") String password, HttpSession session, Model model) {
        // Accesos Hardcoded (Admin/Staff)
        if (correo.equals("admin@acora.com") && password.equals("admin123")) {
            session.setAttribute("usuarioLogueado", "Administrador");
            return "redirect:/admin/dashboard";
        }
        
        // Acceso Clientes (BD)
        try {
            Cliente cliente = clienteService.autenticarCliente(correo, password);
            if (cliente != null) {
                session.setAttribute("usuarioLogueado", cliente.getNombre());
                
                // --- AQUÍ ESTABA EL ERROR ---
                // Antes: cliente.getCliente_id() -> Ahora: cliente.getId()
                session.setAttribute("idUsuario", cliente.getId()); 
                
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

    // --- MIS PEDIDOS (CLIENTE) ---
    @GetMapping("/mis-pedidos")
    public String misPedidos(HttpSession session, Model model) {
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        if (idUsuario == null) return "redirect:/login";

        Cliente cliente = clienteService.buscarPorId(idUsuario);
        if (cliente != null) {
            model.addAttribute("misCompras", pedidoService.buscarPorCliente(cliente));
            model.addAttribute("nombreUsuario", cliente.getNombre());
        }
        return "mis_pedidos";
    }
    // ==========================================
    // API PARA EL BOTÓN "VER DETALLE"
    // ==========================================
    @GetMapping("/api/pedido/{id}/detalles")
    @ResponseBody
    public List<utp.AgroIndustria_Acora.modelo.DetallePedido> obtenerDetalles(@PathVariable Long id) {
        // Busca los productos de ese pedido específico
        return detallePedidoService.listarPorPedidoId(id);
    }
    // ==========================================
    // 9. FINAL: VOUCHER DE COMPRA EXITOSA
    // ==========================================
    @GetMapping("/compra-exitosa")
    public String compraExitosa(HttpSession session, Model model) {
        // 1. Validar que esté logueado
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        if (idUsuario == null) return "redirect:/login";

        // 2. Buscar al cliente
        Cliente cliente = clienteService.buscarPorId(idUsuario);
        
        // 3. Buscar sus pedidos
        List<utp.AgroIndustria_Acora.modelo.Pedido> pedidos = pedidoService.buscarPorCliente(cliente);
        
        if (pedidos.isEmpty()) {
            return "redirect:/"; // Si no compró nada, pa' fuera
        }
        
        // 4. Obtener EL ÚLTIMO pedido realizado (El que acaba de pagar)
        // Como la lista suele venir en orden de creación, tomamos el último de la lista
        utp.AgroIndustria_Acora.modelo.Pedido ultimoPedido = pedidos.get(pedidos.size() - 1);
        
        // 5. Cargar los detalles (productos) de ese pedido
        model.addAttribute("pedido", ultimoPedido);
        model.addAttribute("detalles", detallePedidoService.listarPorPedidoId(ultimoPedido.getId()));
        
        return "exito"; // Carga la vista exito.html
    }
}