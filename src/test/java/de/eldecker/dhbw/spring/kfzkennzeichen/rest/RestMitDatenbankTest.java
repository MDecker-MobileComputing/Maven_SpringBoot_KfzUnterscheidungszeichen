package de.eldecker.dhbw.spring.kfzkennzeichen.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * Diese Unit-Test-Klasse nimmt die REST-Endpunkte, die von der Klasse 
 * {@code KfzKennzeichenRestController} bereitgestellt werden, unter Test.
 * Die Datenbank ist nicht weggemockt.
 */
@SpringBootTest(properties = { "unterscheidungszeichen.militaer_ausblenden=false" })
@AutoConfigureMockMvc
public class RestMitDatenbankTest {

    private static final String BASIS_URL = "/unterscheidungszeichen/v1/";
    
    @Autowired
    private MockMvc _mockMvc;
    
    @Test
    void anzahl() throws Exception {
        
        final String url = BASIS_URL + "anzahl";
        
        // REST-Endpunkt unter Test aufrufen
        MvcResult ergebnis = _mockMvc.perform(get(url))
                                .andExpect(status().isOk())
                                .andReturn();
        
        String jsonResult = ergebnis.getResponse().getContentAsString();
        
        JSONObject jsonObj = new JSONObject(jsonResult);
        assertTrue( jsonObj.has("anzahl"), "Attribut \"Anzahl\" in Ergebnis-JSON nicht gefunden.");
        assertTrue( jsonObj.getInt("anzahl") > 0 );
    }
    
    
    /**
     * Test für Erkennung verschiedener Schreibweisen des Unterscheidungszeichen
     * "BA" für Bamberg.
     */
    @ParameterizedTest
    @ValueSource(strings = { "BA", "ba", "Ba", "bA", " BA", "BA ", " BA ", " Ba  " })
    void bamberg(String kuerzel) throws Exception {
        
        final String url = BASIS_URL + "suche/" + kuerzel;
        
        // REST-Endpunkt unter Test aufrufen
        MvcResult ergebnis = _mockMvc.perform(get(url))
                                .andExpect(status().isOk())
                                .andReturn();

        String ergebnisStr = ergebnis.getResponse().getContentAsString();
        
        JSONObject hauptJsonObj = new JSONObject(ergebnisStr);
        assertTrue( hauptJsonObj.getBoolean("erfolgreich") );
        assertTrue( hauptJsonObj.getString("fehlermeldung").isEmpty() );
        
        JSONObject uzObj = hauptJsonObj.getJSONObject("unterscheidungszeichen");
        assertEquals("BA", uzObj.getString("kuerzel"));
        assertEquals("Bamberg", uzObj.getString("bedeutung"));
        assertEquals("Bayern", uzObj.getString("kategorie"));
    }
    
    /**
     * Werden die militärischen Unterscheidungszeichen erkannt?
     * Für diese Unit-Test-Klasse wird per Annotation das Anzeigen von
     * militärischen Unterscheidungszeichen aktivieren:
     * {@code unterscheidungszeichen.militaer_ausblenden=false}  
     */
    @ParameterizedTest
    @CsvSource({ "X,Nato", "Y,Bundeswehr"})
    void militaer(String kuerzel, String beschreibung) throws Exception {

        final String url = BASIS_URL + "suche/" + kuerzel;
        
        // REST-Endpunkt unter Test aufrufen
        MvcResult ergebnis = _mockMvc.perform(get(url))
                                .andExpect(status().isOk())
                                .andReturn();

        String ergebnisStr = ergebnis.getResponse().getContentAsString();
        
        JSONObject hauptJsonObj = new JSONObject(ergebnisStr);
        assertTrue( hauptJsonObj.getBoolean("erfolgreich") );
        assertTrue( hauptJsonObj.getString("fehlermeldung").isEmpty() );

        JSONObject uzObj = hauptJsonObj.getJSONObject("unterscheidungszeichen");
        assertEquals(kuerzel, uzObj.getString("kuerzel"));
        assertEquals(beschreibung, uzObj.getString("bedeutung"));
        assertEquals("Militär", uzObj.getString("kategorie"));
    }
    
    @Test
    void unterscheidungszeichenNotFound() throws Exception {
        
        final String kuerzelFalsch = "xyz";
        
        final String url = BASIS_URL + "suche/" + kuerzelFalsch;
        // REST-Endpunkt unter Test aufrufen
        MvcResult ergebnis = _mockMvc.perform(get(url))
                                .andExpect(status().isNotFound()) // HTTP Status Code 404
                                .andReturn();

        String ergebnisStr = ergebnis.getResponse().getContentAsString();
        
        JSONObject hauptJsonObj = new JSONObject(ergebnisStr);
        assertFalse( hauptJsonObj.getBoolean("erfolgreich") );
        assertTrue( hauptJsonObj.getString("fehlermeldung").contains(kuerzelFalsch.toUpperCase()) );
        
        JSONObject uzObj = hauptJsonObj.getJSONObject("unterscheidungszeichen");
        assertTrue(uzObj.getString("kuerzel").isEmpty());
        assertTrue(uzObj.getString("bedeutung").isEmpty());
        assertTrue(uzObj.getString("kategorie").isEmpty());        
    }

    /**
     * Test für Abfrage-Strings, die "Bad Request" als HTTP-Response-Code bewirken:
     * <ul>
     * <li>"badx": länger als drei Zeichen</li>
     * <li>"%20": Leerzeichen in URL-Encoding</li>
     * </ul>
     */
    @ParameterizedTest
    @ValueSource(strings = { "badx", "ABCxyz" })    
    void badRequest(String abfrageString) throws Exception {
        
        final String url = BASIS_URL + "suche/" + abfrageString;
        
        // REST-Endpunkt unter Test aufrufen
        MvcResult ergebnis = _mockMvc.perform(get(url))
                                .andExpect(status().isBadRequest()) // HTTP Status Code 400
                                .andReturn();

        String ergebnisStr = ergebnis.getResponse().getContentAsString();
        
        JSONObject hauptJsonObj = new JSONObject(ergebnisStr);
        assertFalse( hauptJsonObj.getBoolean("erfolgreich") );
        assertTrue( hauptJsonObj.getString("fehlermeldung").contains(abfrageString.toUpperCase()) );
        
        JSONObject uzObj = hauptJsonObj.getJSONObject("unterscheidungszeichen");
        assertTrue(uzObj.getString("kuerzel").isEmpty());
        assertTrue(uzObj.getString("bedeutung").isEmpty());
        assertTrue(uzObj.getString("kategorie").isEmpty());        
    }
    
}
