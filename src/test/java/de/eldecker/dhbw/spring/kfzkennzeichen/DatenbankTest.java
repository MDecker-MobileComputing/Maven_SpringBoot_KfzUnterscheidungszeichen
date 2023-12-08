package de.eldecker.dhbw.spring.kfzkennzeichen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.eldecker.dhbw.spring.kfzkennzeichen.db.KfzKennzeichenDB;
import de.eldecker.dhbw.spring.kfzkennzeichen.model.UZKategorieEnum;
import de.eldecker.dhbw.spring.kfzkennzeichen.model.Unterscheidungszeichen;

/**
 * Die Annotation {@code SpringBootTest} ist erforderlich, damit Spring Boot gestartet
 * wird, weil sonst u.a. Dependency Injection nicht zur Verfügung steht.
 */
@SpringBootTest
class DatenbankTest {

    /** Bean mit "Code under Test. */
    @Autowired
    KfzKennzeichenDB _cut;
     
    @Test
    void sucheUnbekanntenUnterscheidungszeichen() {
        
        Optional<Unterscheidungszeichen> ergebnisOptional = _cut.sucheUnterscheidungszeichen("abcd");
        assertTrue(ergebnisOptional.isEmpty());
    }

    @ParameterizedTest
    @CsvSource({"B,Berlin,BE", "BA,Bamberg,BY", "BAD,Baden-Baden,BW"})
    void happyPath(String kuerzel, String bedeutung, String kategorieStr) {
        
        Optional<Unterscheidungszeichen> ergebnisOptional = _cut.sucheUnterscheidungszeichen(kuerzel);
        assertTrue(ergebnisOptional.isPresent());
        
        Unterscheidungszeichen ergebnis = ergebnisOptional.get();
        assertEquals(kuerzel  , ergebnis.kuerzel());
        assertEquals(bedeutung, ergebnis.bedeutung());
        
        UZKategorieEnum kategorie = UZKategorieEnum.valueOf(kategorieStr);
        assertEquals(kategorie, ergebnis.kategorie());
    }

}
