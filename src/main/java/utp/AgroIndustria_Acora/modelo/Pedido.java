package utp.AgroIndustria_Acora.modelo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos") 
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaPedido;

    @Column(nullable = false)
    private Double total; 
    
    private String estado;
    private String direccionEntrega;

    // --- NUEVOS CAMPOS (Estos son los que te faltan y causan el error rojo) ---
    
    @Column(name = "tipo_comprobante")
    private String tipoComprobante; // Para guardar "BOLETA" o "FACTURA"

    @Column(name = "metodo_pago")
    private String metodoPago;      // Para guardar "TARJETA" o "YAPE"

    // RELACIÓN CON CLIENTE
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    // --- CONSTRUCTORES ---
    public Pedido() {}

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDateTime fechaPedido) { this.fechaPedido = fechaPedido; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getDireccionEntrega() { return direccionEntrega; }
    public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    // --- GETTERS Y SETTERS NUEVOS (Al pegar esto, el rojo se irá) ---
    
    public String getTipoComprobante() { return tipoComprobante; }
    public void setTipoComprobante(String tipoComprobante) { this.tipoComprobante = tipoComprobante; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
}