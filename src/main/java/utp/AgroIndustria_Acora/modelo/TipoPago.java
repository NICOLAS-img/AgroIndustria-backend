
package utp.AgroIndustria_Acora.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_pago")
public class TipoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tipoPagoId;

    private String nombre;

    public TipoPago() {
    }
    

    public Integer getTipo_pago_id() {
        return tipoPagoId;
    }

    public void setTipo_pago_id(Integer tipoPagoId) {
        this.tipoPagoId = tipoPagoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    
}
