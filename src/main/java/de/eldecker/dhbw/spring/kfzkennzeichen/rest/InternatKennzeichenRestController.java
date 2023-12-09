package de.eldecker.dhbw.spring.kfzkennzeichen.rest;

import de.eldecker.dhbw.spring.kfzkennzeichen.model.InternatKennzeichenErgebnisRecords;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Dieser REST-Controller bietet einen REST-Endpunkt, mit dem internationale
 * Kennzeichen abgefragt werden können.
 * <br><br>
 *
 * Liste mit internationalen KFZ-Kennzeichen:
 * https://www.check24.de/kfz-versicherung/kennzeichen/laenderkennzeichen/
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
     * Beispiel-URL für Aufruf:
     * <pre>
     *  http://localhost:8000/internatkennzeichen/v1/suche?kennzeichen=H
     * </pre>
     */
    @GetMapping("/suche")
    public ResponseEntity<InternatKennzeichenErgebnisRecords> queryInternationalesKennzeichen(@RequestParam("kennzeichen") String kennzeichen) {

        LOG.info("Abfrage nach internationalem Kennzeichen: {}", kennzeichen);

        InternatKennzeichenErgebnisRecords ergebnisRecord = new InternatKennzeichenErgebnisRecords(false, "Nicht implementiert", "");
        return ResponseEntity.status(OK).body(ergebnisRecord);
    }

}
