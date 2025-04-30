package de.eldecker.dhbw.spring.kfzkennzeichen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Einstiegsklasse für Programm: Startet den Webserver.
 * <br><br>
 * 
 * Aufruf spezielles Maven-Goal für Start einer Spring-Boot-Anwendung:
 * <pre>
 * mvn spring-boot:run
 * </pre> 
 */
@SpringBootApplication
public class KfzkennzeichenApplication {

	public static void main( String[] args ) {

		SpringApplication.run( KfzkennzeichenApplication.class, args );
	}

}
