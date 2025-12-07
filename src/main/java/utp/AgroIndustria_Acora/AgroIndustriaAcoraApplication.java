package utp.AgroIndustria_Acora;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// MODO PRODUCCIÓN: Sin 'exclude'. Spring intentará conectarse a MySQL al iniciar.
// Esto requiere que la configuración en application.properties sea CORRECTA 
// y que el servidor MySQL (XAMPP, etc.) esté encendido.
@SpringBootApplication
public class AgroIndustriaAcoraApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgroIndustriaAcoraApplication.class, args);
	}

}