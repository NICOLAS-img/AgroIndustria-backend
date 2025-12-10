package utp.AgroIndustria_Acora.controller;
import java.time.ZoneId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utp.AgroIndustria_Acora.modelo.*;
import utp.AgroIndustria_Acora.service.*;
import utp.AgroIndustria_Acora.repository.TipoPagoRepository; 

import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private DetallePedidoService detallePedidoService;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private ComprobanteService comprobanteService;
    @Autowired
    private DetalleComprobantePagoService detallePagoService;
    @Autowired
    private TipoPagoRepository tipoPagoRepository; 

    @PostMapping("/guardar")
    @ResponseBody
    public String guardarPedido(@RequestBody Map<String, Object> datos, HttpSession session) {
        try {
            // 1. VALIDAR SESIÓN
            Integer idUsuario = (Integer) session.getAttribute("idUsuario");
            if (idUsuario == null) return "ERROR: Debes iniciar sesión para comprar.";

            // 2. RECUPERAR DATOS (CON BLINDAJE ANTI-ERROR)
            
            // a) DIRECCIÓN: Si es null, usamos la del cliente
            String direccion = null;
            if (datos.get("direccion") != null) {
                direccion = datos.get("direccion").toString();
            }
            
            // Buscamos al cliente para tener sus datos a mano
            Cliente cliente = clienteService.buscarPorId(idUsuario); 
            
            // Si no enviaron dirección, usamos la del perfil
            if (direccion == null || direccion.trim().isEmpty()) {
                direccion = cliente.getDireccion();
            }

            // b) TIPO COMPROBANTE (Por defecto BOLETA)
            String tipoDoc = "BOLETA";
            if (datos.get("tipoComprobante") != null) {
                tipoDoc = datos.get("tipoComprobante").toString();
            }

            // c) MÉTODO DE PAGO (Por defecto TARJETA)
            String metodoPagoStr = "TARJETA";
            if (datos.get("metodoPago") != null) {
                metodoPagoStr = datos.get("metodoPago").toString();
            }

            // d) TOTAL (BigDecimal como en tu código original)
            BigDecimal total = new BigDecimal(datos.get("total").toString());

            // 3. GUARDAR CABECERA PEDIDO
            Pedido pedido = new Pedido();
            pedido.setCliente(cliente);
            pedido.setDireccionEntrega(direccion);
            pedido.setTotal(total.doubleValue()); // Tu modelo usa BigDecimal, así que le pasamos BigDecimal
            pedido.setEstado("PAGADO"); 
            pedido.setFechaPedido(LocalDateTime.now(ZoneId.of("America/Lima")));
            pedido.setTipoComprobante(tipoDoc);     // Guardamos boleta/factura
            pedido.setMetodoPago(metodoPagoStr);    // Guardamos yape/tarjeta
            
            Pedido pedidoGuardado = pedidoService.guardar(pedido);

            // 4. GUARDAR DETALLES Y ACTUALIZAR STOCK
            List<Map<String, Object>> productos = (List<Map<String, Object>>) datos.get("productos");
            
            for (Map<String, Object> item : productos) {
                // Usamos Long como en tu código original
                Long prodId = Long.valueOf(item.get("id").toString());
                int cantidad = Integer.parseInt(item.get("cantidad").toString());
                
                // Usamos .orElse(null) porque tu servicio devuelve Optional
                Producto prodBD = productoService.buscarPorId(prodId).orElse(null);
                
                if (prodBD != null) {
                    // --- VALIDACIÓN DE STOCK ---
                    if (prodBD.getStock() < cantidad) {
                        throw new RuntimeException("Stock insuficiente para: " + prodBD.getNombre());
                    }

                    // --- RESTAMOS EL STOCK ---
                    int nuevoStock = prodBD.getStock() - cantidad;
                    prodBD.setStock(nuevoStock);
                    
                    if(nuevoStock == 0) {
                        prodBD.setDisponible(false);
                    }
                    
                    productoService.guardar(prodBD);

                    // --- Guardamos el detalle ---
                    DetallePedido detalle = new DetallePedido();
                    detalle.setPedido(pedidoGuardado);
                    detalle.setProducto(prodBD);
                    detalle.setCantidad(cantidad);
                    
                    // Usamos tus nombres de campo originales (precioUnitario, subtotal)
                    // Asumiendo que getPrecio() devuelve BigDecimal. Si devuelve Double, avísame.
                    detalle.setPrecioUnitario(prodBD.getPrecio()); 
                    detalle.setSubtotal(prodBD.getPrecio().multiply(new BigDecimal(cantidad)));
                    
                    detallePedidoService.guardar(detalle);
                }
            }

            // 5. GENERAR COMPROBANTE
            Comprobante comprobante = new Comprobante();
            comprobante.setPedido(pedidoGuardado);
            comprobante.setTipoComprobante(tipoDoc);
            comprobante.setFechaEmision(LocalDateTime.now());
            comprobante.setMontoTotal(total.doubleValue());
            comprobante.setEstadoPago("CANCELADO"); 
            
            comprobanteService.guardar(comprobante);

            // 6. REGISTRAR PAGO
            DetalleComprobantePago pago = new DetalleComprobantePago();
            pago.setComprobante(comprobante);
            pago.setFecha_pago(LocalDateTime.now());
            pago.setMontoPagado(total.doubleValue());

            int idTipoPago = metodoPagoStr.equals("YAPE") ? 1 : 2; 
            TipoPago tipoPago = tipoPagoRepository.findById(idTipoPago).orElse(null);
            pago.setTipoPago(tipoPago);

            detallePagoService.guardar(pago);

            return "OK";

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR: " + e.getMessage();
        }
    }
}