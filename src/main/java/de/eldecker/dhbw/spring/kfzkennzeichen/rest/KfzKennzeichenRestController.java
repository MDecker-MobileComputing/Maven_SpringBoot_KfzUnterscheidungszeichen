package de.eldecker.dhbw.spring.kfzkennzeichen.rest;

import static de.eldecker.dhbw.spring.kfzkennzeichen.model.UZKategorieEnum.NICHT_DEFINIERT;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.eldecker.dhbw.spring.kfzkennzeichen.db.KfzKennzeichenDB;
import de.eldecker.dhbw.spring.kfzkennzeichen.model.RestErgebnisRecord;
import de.eldecker.dhbw.spring.kfzkennzeichen.model.Unterscheidungszeichen;

/**
 * RestController, der REST-API für Abfrage Unterscheidungskennzeichen von KFZ-Kennzeichen
 * in Deutschland bereitstellt.<br><br>
 * 
 * Verwendung nach Annotation gem. Beispiel aus folgender Stelle von offizieller Doku:  
 * https://docs.spring.io/spring-boot/docs/3.2.0/reference/htmlsingle/#web.servlet.spring-mvc
 */
@RestController
@RequestMapping
public class KfzKennzeichenRestController {
    
    private static Logger LOG = LoggerFactory.getLogger(KfzKennzeichenRestController.class);
    
    /** "Datenbank" zur Abfrage der Unterscheidungszeichzen. */
    private KfzKennzeichenDB _kfzKennzeichenDb;
    
    /** Leeres Unterscheidungszeichenobjekt, wird für Fehlermeldungen benötigt. */
    private static final Unterscheidungszeichen UNTERSCHEIDUNGSZEICHEN_EMPTY = new Unterscheidungszeichen("", "", NICHT_DEFINIERT); 
    
    /**
     * Konstruktor für Dependency Injection.
     */
    @Autowired
    public KfzKennzeichenRestController(KfzKennzeichenDB kfzKennzeichenDB) {
        
        _kfzKennzeichenDb = kfzKennzeichenDB;
    }

    /**
     * REST-Methode für Abfrage eines Unterscheidungszeichen von deutschen KFZ-Kennzeichen.
     * <br><br>
     * 
     * URL für lokalen Beispielaufruf zur Abfrage von "BA" (Bamberg):
     * <a href="http://localhost:8080/unterscheidungszeichen/BA">http://localhost:8080/unterscheidungszeichen/BA</a>
     * 
     * @param kuerzel Unterscheidungskennzeichen, z.B. "BA" für "Bamberg"
     * @return possible HTTP status codes: 400, 404, 200
     */
    @GetMapping("/unterscheidungszeichen/{kuerzel}")
    public ResponseEntity<RestErgebnisRecord> queryUnterscheidungskennzeichen(@PathVariable String kuerzel) {
        
        RestErgebnisRecord ergebnisRecord = null;
        
        final String kuerzelNormalized = kuerzel.trim().toUpperCase();        
        if (kuerzelNormalized.isBlank()) {
            
            LOG.warn("Abfrage für leeres Kürzel \"{}\".", kuerzel);
            ergebnisRecord = new RestErgebnisRecord(false, "Leeres Kürzel in Abfrage", UNTERSCHEIDUNGSZEICHEN_EMPTY);             
            return ResponseEntity.status(BAD_REQUEST).body(null);
        }        
        if (kuerzelNormalized.length() > 3) {

            LOG.warn("Abfrage für zu langes Kürzel \"{}\".", kuerzel);
            ergebnisRecord = new RestErgebnisRecord(false, "Kürzel hat mehr als drei Buchstaben", UNTERSCHEIDUNGSZEICHEN_EMPTY);             
            return ResponseEntity.status(BAD_REQUEST).body(null);
        }
                
        // eigentliche Abfrage
        Optional<Unterscheidungszeichen> _unterscheidungszeichenOptional = _kfzKennzeichenDb.sucheUnterscheidungszeichen(kuerzelNormalized);
        if (_unterscheidungszeichenOptional.isEmpty()) {

            LOG.info("Kein Ergebnis gefunden für \"{}\".", kuerzelNormalized);
            ergebnisRecord = new RestErgebnisRecord(false, "Unterscheidungszeichen '" + kuerzelNormalized + "' nicht gefunden.", UNTERSCHEIDUNGSZEICHEN_EMPTY);             
            return ResponseEntity.status(NOT_FOUND).body(ergebnisRecord);
        }
        
        ergebnisRecord = new RestErgebnisRecord(true, "", _unterscheidungszeichenOptional.get());
        LOG.info("Erfolgreiche REST-Antwort: " + ergebnisRecord);
        return ResponseEntity.status(OK).body(ergebnisRecord);
    }
    
}
