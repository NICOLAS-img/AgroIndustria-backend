
package utp.AgroIndustria_Acora.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "empleado")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer empleadoId;

    private String nombre;
    private String apellido;
    private String rol;

    public Empleado() {
    }
    

    public Integer getEmpleado_id() {
        return empleadoId;
    }

    public void setEmpleado_id(Integer empleado_id) {
        this.empleadoId = empleado_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    
}
