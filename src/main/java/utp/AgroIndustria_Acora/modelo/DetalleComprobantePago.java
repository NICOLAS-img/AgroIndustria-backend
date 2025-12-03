
package utp.AgroIndustria_Acora.modelo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "detalleComprobantePago")
public class DetalleComprobantePago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer detallePagoId;

    @ManyToOne
    @JoinColumn(name = "comprobanteId")
    private Comprobante comprobante;

    @ManyToOne
    @JoinColumn(name = "tipoPagoId")
    private TipoPago tipoPago;

    private LocalDateTime fecha_pago;
    private Double montoPagado;

    public DetalleComprobantePago() {
    }

    public Integer getDetallePagoId() {
        return detallePagoId;
    }

    public void setDetallePagoId(Integer detallePagoId) {
        this.detallePagoId = detallePagoId;
    }

    public Comprobante getComprobante() {
        return comprobante;
    }

    public void setComprobante(Comprobante comprobante) {
        this.comprobante = comprobante;
    }

    public TipoPago getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(TipoPago tipoPago) {
        this.tipoPago = tipoPago;
    }

    public LocalDateTime getFecha_pago() {
        return fecha_pago;
    }

    public void setFecha_pago(LocalDateTime fecha_pago) {
        this.fecha_pago = fecha_pago;
    }

    public Double getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(Double montoPagado) {
        this.montoPagado = montoPagado;
    }
    

    
}
