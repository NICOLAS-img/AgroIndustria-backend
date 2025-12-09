package utp.AgroIndustria_Acora.controller;

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

            // 2. RECUPERAR DATOS
            String direccion = datos.get("direccion").toString();
            String tipoDoc = datos.get("tipoComprobante").toString(); 
            String metodoPagoStr = datos.get("metodoPago").toString(); 
            BigDecimal total = new BigDecimal(datos.get("total").toString());

            // 3. GUARDAR CABECERA PEDIDO
            Pedido pedido = new Pedido();
            Cliente cliente = clienteService.buscarPorId(idUsuario); 
            pedido.setCliente(cliente);
            pedido.setDireccionEntrega(direccion);
            pedido.setTotal(total);
            pedido.setEstado("PAGADO"); 
            pedido.setFechaPedido(LocalDateTime.now());
            
            Pedido pedidoGuardado = pedidoService.guardar(pedido);

            // 4. GUARDAR DETALLES Y ACTUALIZAR STOCK
            List<Map<String, Object>> productos = (List<Map<String, Object>>) datos.get("productos");
            
            for (Map<String, Object> item : productos) {
                Long prodId = Long.valueOf(item.get("id").toString());
                int cantidad = Integer.parseInt(item.get("cantidad").toString());
                
                Producto prodBD = productoService.buscarPorId(prodId).orElse(null);
                
                if (prodBD != null) {
                    // --- VALIDACIÓN DE STOCK ---
                    if (prodBD.getStock() < cantidad) {
                        throw new RuntimeException("Stock insuficiente para: " + prodBD.getNombre());
                    }

                    // --- ¡AQUÍ ESTÁ LA MAGIA! RESTAMOS EL STOCK ---
                    int nuevoStock = prodBD.getStock() - cantidad;
                    prodBD.setStock(nuevoStock);
                    
                    // Si llega a 0, lo ponemos como NO disponible automáticamente
                    if(nuevoStock == 0) {
                        prodBD.setDisponible(false);
                    }
                    
                    // Guardamos el producto con el nuevo stock en la BD
                    productoService.guardar(prodBD);

                    // --- Guardamos el detalle de la venta ---
                    DetallePedido detalle = new DetallePedido();
                    detalle.setPedido(pedidoGuardado);
                    detalle.setProducto(prodBD);
                    detalle.setCantidad(cantidad);
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
            e.printStackTrace(); // Ver error en consola
            return "ERROR: " + e.getMessage();
        }
    }
}