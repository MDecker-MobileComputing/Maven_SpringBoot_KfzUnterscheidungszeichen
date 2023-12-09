package de.eldecker.dhbw.spring.kfzkennzeichen.rest;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.eldecker.dhbw.spring.kfzkennzeichen.db.KfzKennzeichenDB;
import de.eldecker.dhbw.spring.kfzkennzeichen.model.InternatKennzeichenErgebnisRecord;


/**
 * Dieser REST-Controller bietet einen REST-Endpunkt, mit dem internationale
 * Kennzeichen abgefragt werden können.
 * <br><br>
 *
 * Siehe <a href="https://www.check24.de/kfz-versicherung/kennzeichen/laenderkennzeichen/">hier (check24.de)</a> 
 * für Liste mit internationalen KFZ-Kennzeichen. 
 * <br><br>
 *
 * <b>Achtung:</b> Es wird nur dann eine Bean dieser Klasser erzeugt, wenn das Spring-Profil
 * {@code vorschau} aktiviert ist, da diese Klasse ein noch nicht finales Features bereitstellt.
 */
@RestController
@RequestMapping("/internatkennzeichen/v1")
@Profile("vorschau")
public class InternatKennzeichenRestController {

    private static Logger LOG = LoggerFactory.getLogger(InternatKennzeichenRestController.class);

    /**
     * Konstruktor
     */
    public InternatKennzeichenRestController() {

        LOG.info("Vorschau-Feature \"Internationale Kennzeichen\" ist aktiv.");
    }

    /**
     * REST-Methode für Abfrage eines internationalen KFZ-Kennzeichens.
     * <br><br>
     *
     * Beispiel-URL für Abfrage "H" (Ungarn):
     * <pre>
     *  http://localhost:8000/internatkennzeichen/v1/suche?kennzeichen=H
     * </pre>
     */
    @GetMapping("/suche")
    public ResponseEntity<InternatKennzeichenErgebnisRecord> queryInternationalesKennzeichen(@RequestParam("kennzeichen") String kennzeichen) {

        LOG.info("Abfrage nach internationalem Kennzeichen: {}", kennzeichen);
        
        InternatKennzeichenErgebnisRecord ergebnisRecord;
        
        final String kennzeichenNormalisiert = kennzeichen.trim().toUpperCase();
        final int laenge = kennzeichenNormalisiert.length();
        if (laenge < 1 ||laenge > 3) {
            
            final String fehlertext = String.format("Unzulässige Länge des Suchstrings (%d).", laenge);
            ergebnisRecord = new InternatKennzeichenErgebnisRecord(false, fehlertext, "");                                                                                                           
            return ResponseEntity.status(BAD_REQUEST).body(ergebnisRecord);
        }
        
        Optional<String> ergebnisOptional = queryDb(kennzeichenNormalisiert);
        if (ergebnisOptional.isEmpty()) {

            final String fehlertext = String.format("Nichts gefunden für \"%s\".", kennzeichenNormalisiert);
            ergebnisRecord = new InternatKennzeichenErgebnisRecord(false, fehlertext, "");
            return ResponseEntity.status(NOT_FOUND).body(ergebnisRecord);
            
        } else {

            final String ergebnisString = ergebnisOptional.get();
            ergebnisRecord = new InternatKennzeichenErgebnisRecord(true, "", ergebnisString);
            return ResponseEntity.status(OK).body(ergebnisRecord);            
        }
    }
    
    /**
     * Da die Abfrage internationaler KFZ-Kennzeichen sich noch in Entwicklung befindet, ist die "Datenbank"-Funktionalität
     * direkt im REST-Controller definiert. 
     * <br><br>
     * 
     * TODO: in Klasse {@link KfzKennzeichenDB} verschieben
     * 
     * @param kennzeichenNormalized Such-String, auf Großbuchstaben normiert und Leerzeichen vorne/hinten entfernt
     * 
     * @return Wenn internationales Kennzeichen gefunden wurde, dann enthält das Optional einen String mit der Beschreibung,
     *         z.B. "Ungarn" für "H". 
     */
    private Optional<String> queryDb(String kennzeichenNormalized) {
        
        switch(kennzeichenNormalized) {
        
            case "A":
                return Optional.of("Österreich");
            case "AND":
                return Optional.of("Andorra");
            case "D":
                return Optional.of("Deutschland");
            case "F":
                return Optional.of("Frankreich");
            case "GB":
                return Optional.of("Großbritannien");
            case "H":
                return Optional.of("Ungarn");

            default:
                return Optional.empty();
        }
    }

}
