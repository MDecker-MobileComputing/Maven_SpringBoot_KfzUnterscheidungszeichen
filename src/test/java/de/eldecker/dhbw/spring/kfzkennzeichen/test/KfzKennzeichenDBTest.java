// FILEPATH: /c:/Users/decker/Documents/BackupNein/GitRepos/GitHub3_SpringBoot_RestApiKfzKennzeichen/src/test/java/de/eldecker/dhbw/spring/kfzkennzeichen/db/KfzKennzeichenDBTest.java

package de.eldecker.dhbw.spring.kfzkennzeichen.test;

import static de.eldecker.dhbw.spring.kfzkennzeichen.model.UZKategorieEnum.BY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.eldecker.dhbw.spring.kfzkennzeichen.db.KfzKennzeichenDB;
import de.eldecker.dhbw.spring.kfzkennzeichen.model.Unterscheidungszeichen;

/**
 * Die folgende Unit-Test-Klasse wurde mit "GitHub Copilot Chat" und dem Befehl {@code /tests}
 * bei Selektion der Methode {@code sucheUnterscheidungszeichen()} in der Klasse 
 * {@code KfzKennzeichenDB} erzeugt; es mussten aber zwei Import-Statements ergänzt
 * und der Konstruktoraufruf für die Klasse {@code Unterscheidungszeichen} korrigiert 
 * werden.
 */
public class KfzKennzeichenDBTest {

    @InjectMocks
    private KfzKennzeichenDB kfzKennzeichenDB;

    @Mock
    private Map<String, Unterscheidungszeichen> _datenMap;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSucheUnterscheidungszeichen_whenKuerzelExists() {
        Unterscheidungszeichen u = new Unterscheidungszeichen("BA", "Bamberg", BY);
        when(_datenMap.get("BA")).thenReturn(u);

        Optional<Unterscheidungszeichen> result = kfzKennzeichenDB.sucheUnterscheidungszeichen("BA");

        assertTrue(result.isPresent());
        assertEquals(u, result.get());
    }

    @Test
    public void testSucheUnterscheidungszeichen_whenKuerzelDoesNotExist() {
        when(_datenMap.get("XYZ")).thenReturn(null);

        Optional<Unterscheidungszeichen> result = kfzKennzeichenDB.sucheUnterscheidungszeichen("XYZ");

        assertFalse(result.isPresent());
    }
}