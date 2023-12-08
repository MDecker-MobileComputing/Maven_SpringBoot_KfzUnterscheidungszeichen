package de.eldecker.dhbw.spring.kfzkennzeichen.dbdirekt;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.eldecker.dhbw.spring.kfzkennzeichen.db.KfzKennzeichenDB;
import de.eldecker.dhbw.spring.kfzkennzeichen.model.Unterscheidungszeichen;

/**
 * Die Anwendung kann über die Konfiguration {@code unterscheidungszeichen.militaer_ausblenden=true}
 * in der Datei  {@code application.properties} angewiesen werden, keine militärischen Unterscheidungszeichen
 * in die Datenbank zu laden. Dies wird von dieser Testklasse überprüft.
 */
@SpringBootTest(properties = { "unterscheidungszeichen.militaer_ausblenden=true" })
public class KeinMilitaerAnzeigenTest {

    /** Bean mit "Code under Test. */
    @Autowired
    private KfzKennzeichenDB _cut;
    
    @ParameterizedTest
    @CsvSource({"X,Nato,MIL", "Y,Bundeswehr,MIL"})
    void militaerNichtGefunden(String kuerzel, String bedeutung, String kategorieStr) {
        
        Optional<Unterscheidungszeichen> ergebnisOptional = _cut.sucheUnterscheidungszeichen(kuerzel);
        assertTrue(ergebnisOptional.isEmpty());
    }
    
    /**
     * Auch bei unterdrückten militärischen Unterscheidungszeichen müssen die "normalen"
     * Unterscheidungszeichen noch vorhanden sein.
     */
    @Test
    void smokeTest() {

        Optional<Unterscheidungszeichen> ergebnisOptional = _cut.sucheUnterscheidungszeichen("BAD");
        assertTrue(ergebnisOptional.isPresent());
    }
    
}
