package utp.AgroIndustria_Acora.modelo;

import jakarta.persistence.*;

// Mapeo a la tabla 'cliente' de tu MySQL
@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cliente_id;

    @Column(length = 50)
    private String nombre;

    @Column(length = 50)
    private String apellido;

    @Column(length = 10)
    private String tipo_documento;

    @Column(length = 20, unique = true)
    private String numero_documento;

    @Column(length = 15)
    private String telefono;

    @Column(length = 255)
    private String direccion;

    @Column(length = 80, unique = true) // Campo clave para login
    private String correo;

    @Column(length = 255) // Campo clave para login
    private String contrasenia;

    // Constructores
    public Cliente() {}

    // Getters y Setters (Necesarios para que Spring pueda leer/escribir datos)
    public Integer getCliente_id() { return cliente_id; }
    public void setCliente_id(Integer cliente_id) { this.cliente_id = cliente_id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getTipo_documento() { return tipo_documento; }
    public void setTipo_documento(String tipo_documento) { this.tipo_documento = tipo_documento; }
    public String getNumero_documento() { return numero_documento; }
    public void setNumero_documento(String numero_documento) { this.numero_documento = numero_documento; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }
}