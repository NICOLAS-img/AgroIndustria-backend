package utp.AgroIndustria_Acora.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "cliente") // Mantenemos tu tabla original
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cliente_id") // En la BD se llama así
    private Integer id;          // En Java lo llamamos 'id' para facilitar el JS

    @Column(length = 50)
    private String nombre;

    @Column(length = 50)
    private String apellido;

    @Column(name = "tipo_documento", length = 10) // Mapeo exacto
    private String tipoDocumento;

    @Column(name = "numero_documento", length = 20, unique = true)
    private String numeroDocumento;

    @Column(length = 15)
    private String telefono;

    @Column(length = 255)
    private String direccion;

    @Column(length = 80, unique = true)
    private String correo;

    @Column(length = 255)
    private String contrasenia;

    // CONSTRUCTORES
    public Cliente() {}

    public Cliente(Integer id, String nombre, String apellido, String tipoDocumento, String numeroDocumento, String telefono, String direccion, String correo, String contrasenia) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.telefono = telefono;
        this.direccion = direccion;
        this.correo = correo;
        this.contrasenia = contrasenia;
    }

    // GETTERS Y SETTERS ACTUALIZADOS (CamelCase)
    // Esto es lo que arregla el error "undefined" en el frontend
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }
}