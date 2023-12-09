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
import de.eldecker.dhbw.spring.kfzkennzeichen.model.RestAnzahlRecord;
import de.eldecker.dhbw.spring.kfzkennzeichen.model.RestErgebnisRecord;
import de.eldecker.dhbw.spring.kfzkennzeichen.model.Unterscheidungszeichen;

/**
 * RestController, der REST-API für Abfrage Unterscheidungskennzeichen von KFZ-Kennzeichen
 * in Deutschland bereitstellt.<br><br>
 *
 * Verwendung nach Annotation gem. Beispiel aus folgender Stelle von offizieller Doku:
 * https://docs.spring.io/spring-boot/docs/3.2.0/reference/htmlsingle/#web.servlet.spring-mvc
 * <br><br>
 *
 * In der Teil-URL laut {@code RequestMapping} für die ganze Klasse ist "v1" für "Version 1"
 * enthalten (REST-APIs sollten auch versioniert werden).
 */
@RestController
@RequestMapping("/unterscheidungszeichen/v1")
public class UnterscheidungszeichenRestController {

    private static Logger LOG = LoggerFactory.getLogger(UnterscheidungszeichenRestController.class);

    /** "Datenbank" zur Abfrage der Unterscheidungszeichzen. */
    private KfzKennzeichenDB _kfzKennzeichenDb;

    /** Leeres Unterscheidungszeichenobjekt, wird für Fehlermeldungen benötigt. */
    private static final Unterscheidungszeichen UNTERSCHEIDUNGSZEICHEN_EMPTY =
                                        new Unterscheidungszeichen( "", "", NICHT_DEFINIERT );

    /**
     * Konstruktor für Dependency Injection.
     */
    @Autowired
    public UnterscheidungszeichenRestController(KfzKennzeichenDB kfzKennzeichenDB) {

        _kfzKennzeichenDb = kfzKennzeichenDB;
    }


    /**
     * REST-Methode für Abfrage eines Unterscheidungszeichen von deutschen KFZ-Kennzeichen.
     * <br><br>
     *
     * URL für lokalen Beispielaufruf zur Abfrage von "BA" (Bamberg):
     * <pre>
     *   http://localhost:8080/unterscheidungszeichen/v1/suche/b
     * </pre>
     *
     * @param kuerzel Unterscheidungskennzeichen, z.B. "BA" für "Bamberg"
     * @return Objekt der Klasse {@code RestErgebnisRecord} in JSON-Form;
     *         mögliche HTTP-Status-Codes: 200 (OK), 400 (Bad Request), 404 (Not Found)
     */
    @GetMapping("/suche/{kuerzel}")
    public ResponseEntity<RestErgebnisRecord> queryUnterscheidungskennzeichen(@PathVariable String kuerzel) {

        LOG.debug("HTTP-Request mit kuerzel=\"{}\" empfangen.", kuerzel );
        RestErgebnisRecord ergebnisRecord = null;

        final String kuerzelNormalized = kuerzel.trim().toUpperCase();
        final int laenge = kuerzelNormalized.length();
        if (laenge < 1 || laenge > 3) {

            LOG.warn("Abfrage-String \"{}\" hat unzulässige Länge von {} Zeichen.", kuerzel, laenge);

            RestErgebnisRecord ergRecord = buildFehlerRecord("Abfrage-String \"" + kuerzelNormalized + "\" hat unzulässige Länge");
            return ResponseEntity.status(BAD_REQUEST).body(ergRecord);
        }

        // eigentliche Abfrage
        Optional<Unterscheidungszeichen> _unterscheidungszeichenOptional = _kfzKennzeichenDb.sucheUnterscheidungszeichen(kuerzelNormalized);
        if (_unterscheidungszeichenOptional.isEmpty()) {

            LOG.info("Kein Ergebnis gefunden für \"{}\".", kuerzelNormalized);
            ergebnisRecord = buildFehlerRecord("Kein Unterscheidungszeichen für Abfrage-String \"" + kuerzelNormalized + "\" gefunden");
            return ResponseEntity.status(NOT_FOUND).body(ergebnisRecord);
        }

        ergebnisRecord = new RestErgebnisRecord(true, "", _unterscheidungszeichenOptional.get());
        LOG.info("Erfolgreiche REST-Antwort: " + ergebnisRecord);
        return ResponseEntity.status(OK).body(ergebnisRecord);
    }


    /**
     * Hilfsmethode zur Erstellung Ergebnis-Record im Fehlerfall.
     *
     * @param fehlertext Beschreibung was schief gegangen ist
     * @return Ergebnis-Record für Fehlerfall mit Fehlertext aus {@code fehlertext}
     */
    private RestErgebnisRecord buildFehlerRecord(String fehlertext) {

        return new RestErgebnisRecord(false, fehlertext, UNTERSCHEIDUNGSZEICHEN_EMPTY);
    }

    /**
     * REST-Methode für Abfrage Anzahl der Datensätze in Datenbank.
     * <br><br>
     *
     * Beispiel-URL für Aufruf:
     * <pre>
     *    http://localhost:8080/unterscheidungszeichen/v1/anzahl
     * <pre>
     *
     * @return Objekt der Klasse {@code AnzahlRecord} in JSON-Form;
     *         immer HTTP-Status-Code 200.
     */
    @GetMapping("/anzahl")
    public ResponseEntity<RestAnzahlRecord> queryAnzahl() {

        int anzahl = _kfzKennzeichenDb.getAnzahlDatensaetze();

        RestAnzahlRecord ar = new RestAnzahlRecord(anzahl);
        return ResponseEntity.status(OK).body(ar);
    }

}
