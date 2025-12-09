// src/main/java/utp/AgroIndustria_Acora/modelo/Comprobante.java
package utp.AgroIndustria_Acora.modelo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comprobante")
public class Comprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer comprobanteId;

    @ManyToOne
    @JoinColumn(name = "pedidoId")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "cajeraId")
    private Empleado cajera;

    private String tipoComprobante;
    private LocalDateTime fechaEmision;
    private Double montoTotal;
    private String estadoPago;

    public Comprobante() {
    }

    public Integer getComprobanteId() {
        return comprobanteId;
    }

    public void setComprobanteId(Integer comprobanteId) {
        this.comprobanteId = comprobanteId;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Empleado getCajera() {
        return cajera;
    }

    public void setCajera(Empleado cajera) {
        this.cajera = cajera;
    }

    public String getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(String tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }
    

}