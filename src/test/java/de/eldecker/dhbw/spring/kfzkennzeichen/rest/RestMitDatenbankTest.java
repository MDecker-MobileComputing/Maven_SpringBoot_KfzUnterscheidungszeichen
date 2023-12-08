package de.eldecker.dhbw.spring.kfzkennzeichen.rest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
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
}
