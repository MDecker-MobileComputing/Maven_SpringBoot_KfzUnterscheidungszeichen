package de.eldecker.dhbw.spring.kfzkennzeichen.test.rest;

import static de.eldecker.dhbw.spring.kfzkennzeichen.model.UZKategorieEnum.BW;

import static org.mockito.ArgumentMatchers.anyString;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import de.eldecker.dhbw.spring.kfzkennzeichen.db.KfzKennzeichenDB;
import de.eldecker.dhbw.spring.kfzkennzeichen.model.Unterscheidungszeichen;


/**
 * Diese Unit-Test-Klasse nimmt die REST-Endpunkte, die von der Klasse
 * {@code UnterscheidungszeichenRestController} bereitgestellt werden, unter Test.
 * Statt der "richtigen" Datenbank-Klasse wird ein Mock-Objekt verwendet.
 */
@SpringBootTest(properties = { "unterscheidungszeichen.militaer_ausblenden=false" })
@AutoConfigureMockMvc
public class RestMitMockDatenbankTest {

    /**
     * Basis-URL für REST-Endpunkte der Klasse {@code UnterscheidungszeichenRestController},
     * siehe Klassen-Annotation {@code @RequestMapping}.
     */
    private static final String BASIS_URL = "/unterscheidungszeichen/v1/";

    /** Bean für Absetzen simulierter REST-Requests. */
    @Autowired
    private MockMvc _mockMvc;

    /**
     * Mock-Objekt für Datenbank, wird wegen Dependency Injection von Bean der Klasse
     * {@code UnterscheidungszeichenRestController} verwendet werden.
     */
    @MockBean
    private KfzKennzeichenDB _dbMock;

    /**
     * Test für REST-Endpunkt, der die Anzahl der Datensätze in der Datenbank
     * zurückliefert.
     */
    @Test
    void anzahl() throws Exception {

        final int anzahl = 123;
        final String url = BASIS_URL + "anzahl";

        Mockito.when(_dbMock.getAnzahlDatensaetze()).thenReturn(123);

        // REST-Endpunkt unter Test aufrufen
        MvcResult ergebnis = _mockMvc.perform(get(url))
                                     .andExpect(status().isOk())
                                     .andReturn();

        String jsonResult = ergebnis.getResponse().getContentAsString();

        JSONObject jsonObj = new JSONObject(jsonResult);
        assertEquals( anzahl, jsonObj.getInt("anzahl") );
    }

    /**
     * Test für Erkennung verschiedener Schreibweisen des Unterscheidungszeichen
     * "BAD" für Baden-Baden.
     */
    @ParameterizedTest
    @ValueSource(strings = { "BAD", "bad", "BaD", "bAD", "bAd", "baD", " bAd  " })
    void badenBaden(String suchstring) throws Exception {

        final String suchstringNormalized = "BAD";
        final String url = BASIS_URL + "suche/" + suchstring;

        Unterscheidungszeichen uz = new Unterscheidungszeichen(suchstringNormalized, "Baden-Baden", BW);
        Optional<Unterscheidungszeichen> uzOptional = Optional.of(uz);

        Mockito.when(_dbMock.sucheUnterscheidungszeichen(suchstringNormalized))
               .thenReturn(uzOptional);

        // REST-Endpunkt unter Test aufrufen
        MvcResult ergebnis = _mockMvc.perform(get(url))
                                     .andExpect(status().isOk())
                                     .andReturn();

        String ergebnisStr = ergebnis.getResponse().getContentAsString();

        JSONObject hauptJsonObj = new JSONObject(ergebnisStr);
        assertTrue( hauptJsonObj.getBoolean("erfolgreich") );
        assertTrue( hauptJsonObj.getString("fehlermeldung").isEmpty() );

        JSONObject uzObj = hauptJsonObj.getJSONObject("unterscheidungszeichen");
        assertEquals(suchstringNormalized, uzObj.getString("kuerzel"));
        assertEquals("Baden-Baden", uzObj.getString("bedeutung"));
        assertEquals("Baden-Württemberg", uzObj.getString("kategorie"));
    }


    /**
     * Test für den Fall, dass die Datenbank das gesuchte Unterscheidungszeichen
     * nicht kennt.
     */
    @Test
    void nichtGefunden() throws Exception {

        final String uzUnbekannt = "XYZ";
        final String url = BASIS_URL + "suche/" + uzUnbekannt;

        Optional<Unterscheidungszeichen> emptyOptional = Optional.empty();

        Mockito.when(_dbMock.sucheUnterscheidungszeichen(anyString()))
               .thenReturn(emptyOptional);

        // REST-Endpunkt unter Test aufrufen
        MvcResult ergebnis = _mockMvc.perform(get(url))
                                     .andExpect(status().isNotFound())
                                     .andReturn();

        String ergebnisStr = ergebnis.getResponse().getContentAsString();

        JSONObject hauptJsonObj = new JSONObject(ergebnisStr);
        assertFalse( hauptJsonObj.getBoolean("erfolgreich") );
        assertTrue( hauptJsonObj.getString("fehlermeldung").contains(uzUnbekannt) );

        JSONObject uzObj = hauptJsonObj.getJSONObject("unterscheidungszeichen");
        assertEquals("", uzObj.getString("kuerzel"));
        assertEquals("", uzObj.getString("bedeutung"));
        assertEquals("", uzObj.getString("kategorie"));
    }

}
