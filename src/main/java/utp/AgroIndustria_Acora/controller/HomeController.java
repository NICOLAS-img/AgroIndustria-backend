package utp.AgroIndustria_Acora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.servlet.http.HttpSession; 
import utp.AgroIndustria_Acora.modelo.Cliente;
import utp.AgroIndustria_Acora.service.ClienteService;

@Controller
public class HomeController {

    // Inyección del servicio para conectar con la BD
    // Si esto falla al iniciar, es porque MySQL está apagado o mal configurado
    @Autowired
    private ClienteService clienteService;

    // ==========================================
    // 1. RUTA PRINCIPAL (TIENDA)
    // ==========================================
    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        // Verificar si hay sesión activa para mostrar el nombre en el menú
        String nombreUsuario = (String) session.getAttribute("usuarioLogueado");
        if (nombreUsuario != null) {
            model.addAttribute("nombreUsuario", nombreUsuario);
        }
        return "index"; // Carga index.html
    }

    // ==========================================
    // 2. VISTAS DE ACCESO
    // ==========================================
    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/registro")
    public String registro() { return "registro"; }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Cierra la sesión
        return "redirect:/";
    }

    // ==========================================
    // 3. PANELES DE GESTIÓN (ADMIN Y EMPLEADOS)
    // ==========================================
    @GetMapping("/admin/dashboard")
    public String adminDashboard() { return "admindashboard"; }

    @GetMapping("/admin/empleados")
    public String adminEmpleados() { return "empleados"; }

    @GetMapping("/admin/productos")
    public String adminProductos() { return "productos"; }

    @GetMapping("/admin/reportes")
    public String adminReportes() { return "reportes"; }
    
    @GetMapping("/cajero/dashboard")
    public String cajeroDashboard() { return "cajerodashboard"; }
    
    @GetMapping("/vendedor/dashboard")
    public String vendedorDashboard() { return "vendedordashboard"; }

    // ==========================================
    // 4. PROCESAR LOGIN (POST) - CONSULTA A BD REAL
    // ==========================================
    @PostMapping("/login")
    public String procesarLogin(@RequestParam("correo") String correo, 
                                @RequestParam("password") String password,
                                HttpSession session,
                                Model model) {
        
        // A. ACCESOS ADMINISTRATIVOS (Fijos para facilitar pruebas)
        if (correo.equals("admin@acora.com") && password.equals("admin123")) {
            session.setAttribute("usuarioLogueado", "Administrador");
            return "redirect:/admin/dashboard";
        }
        if (correo.equals("cajero@acora.com") && password.equals("123")) {
            session.setAttribute("usuarioLogueado", "Cajero Principal");
            return "redirect:/cajero/dashboard";
        }
        if (correo.equals("vendedor@acora.com") && password.equals("123")) {
            session.setAttribute("usuarioLogueado", "Vendedor Turno Mañana");
            return "redirect:/vendedor/dashboard";
        }

        // B. ACCESO CLIENTES (Desde MySQL)
        try {
            // Llama al servicio que consulta la tabla 'cliente'
            Cliente cliente = clienteService.autenticarCliente(correo, password);

            if (cliente != null) {
                // Login correcto -> Guardar sesión
                session.setAttribute("usuarioLogueado", cliente.getNombre());
                session.setAttribute("idUsuario", cliente.getCliente_id());
                return "redirect:/"; // Mandar a la tienda
            } else {
                model.addAttribute("error", "Correo o contraseña incorrectos.");
                return "login";
            }
        } catch (Exception e) {
            // Captura errores si la BD se cae
            e.printStackTrace();
            model.addAttribute("error", "Error de conexión con la Base de Datos.");
            return "login";
        }
    }
    
    // ==========================================
    // 5. PROCESAR REGISTRO (POST) - GUARDA EN BD REAL
    // ==========================================
    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute Cliente cliente, Model model) {
        try {
            // Intenta guardar en MySQL usando el servicio
            Cliente nuevo = clienteService.registrarCliente(cliente);
            
            if (nuevo != null) {
                return "redirect:/login?exito"; // Éxito
            } else {
                model.addAttribute("error", "El correo ya está registrado.");
                return "registro";
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al guardar en Base de Datos. Verifique conexión.");
            return "registro";
        }
    }
}