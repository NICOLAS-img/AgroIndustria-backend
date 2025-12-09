package utp.AgroIndustria_Acora.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "empleado")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "empleado_id") // IMPORTANTE: Así se llama en tu BD
    private Integer id;           // En Java lo llamaremos 'id' para facilitar el JS

    private String nombre;
    private String apellido;
    private String dni;
    private String correo;
    private String rol;
    private String password; // Asegúrate de que tu columna en BD se llame 'password'

    // CONSTRUCTORES
    public Empleado() {}

    public Empleado(Integer id, String nombre, String apellido, String dni, String correo, String rol, String password) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.correo = correo;
        this.rol = rol;
        this.password = password;
    }

    // GETTERS Y SETTERS (Vitales para que no salga NULL)
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}