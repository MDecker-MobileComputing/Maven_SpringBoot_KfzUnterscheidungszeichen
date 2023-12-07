package de.eldecker.dhbw.spring.kfzkennzeichen.db;

import static de.eldecker.dhbw.spring.kfzkennzeichen.model.UZKategorieEnum.BE;
import static de.eldecker.dhbw.spring.kfzkennzeichen.model.UZKategorieEnum.BW;
import static de.eldecker.dhbw.spring.kfzkennzeichen.model.UZKategorieEnum.BY;
import static de.eldecker.dhbw.spring.kfzkennzeichen.model.UZKategorieEnum.MIL;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import de.eldecker.dhbw.spring.kfzkennzeichen.model.UZKategorieEnum;
import de.eldecker.dhbw.spring.kfzkennzeichen.model.Unterscheidungszeichen;

/**
 * Das Objekt dieser Bean-Klasse ist die "Datenbank", von der die Unterscheidungszeichen 
 * abgefragt werden.
 * 
 * <br><br>
 * Diese Klasse ist mit der Spring-Annotation {@code Annotation} versehen und
 * ist daher eine "Bean", die in anderen Bean-Klasse per Autowiring geholt
 * werden kann. 
 */
@Component
public class KfzKennzeichenDB {
    
    /** 
     * Map mit Daten zu Unterscheidungszeichen, bildet auf Großbuchstaben normalisierten
     * Unterscheidungszeichen auf ein Objekt der Klasse {@code Unterscheidungszeichen} ab. 
     */
    private Map<String,Unterscheidungszeichen> _datenMap = new HashMap<>(100);
        
    
    /**
     * Konstruktor, füllt interne HashMap mit Datenbestand auf.
     */
    public KfzKennzeichenDB() {
                
        addEintrag("B"  , "Berlin"     , BE);
        addEintrag("BA" , "Bamberg"    , BY);
        addEintrag("BAD", "Baden-Baden", BW);
        
        addEintrag("Y", "Bundeswehr", MIL);
    }
    
    /**
     * Methode, um der internen Hashmap einen Eintrag hinzuzufügen.
     * 
     * @param kuerzel   Unterscheidungszeichen (wird auf Großbuchstaben normiert und getrimmt).
     * @param bedeutung Stadt-/Landkreis oder sonstige Bedeutung wie z.B. "Militär".
     * @param kategorie Bundesland oder Organisation
     */
    private void addEintrag(String kuerzel, String bedeutung, UZKategorieEnum kategorie) {
        
        final String kuerzelNormalized = kuerzel.trim().toUpperCase();
        
        Unterscheidungszeichen uz = new Unterscheidungszeichen(kuerzelNormalized, bedeutung, kategorie); 
        _datenMap.put(kuerzelNormalized, uz);
    }
    

    /**
     * Methode um die Bedeutung eines als Argument {@code kuerzelNormalized} übergebenen Unterscheidungszeichen
     * abzufragen.
     * 
     * @param kuerzelNormalized Unterscheidungszeichen, das gesucht werden soll, z.B. "BAD" (Baden-Baden);
     *                          muss bereits normalisiert sein, d.h. nur Großbuchstaben und keine 
     *                          Leerzeichen am Anfang/Ende  
     * @return Optional ist genau dann nicht leer, wenn ein Unterscheidungszeichen mit {@code kuerzelNormalized}
     *         gefunden wurde; es sind dann alle Felder im enthaltenen {@code Unterscheidungszeichen}-Objekt
     *         gefüllt.
     */
    public Optional<Unterscheidungszeichen> sucheUnterscheidungszeichen(String kuerzelNormalized) {
        
        Unterscheidungszeichen ergebnis = _datenMap.get(kuerzelNormalized);
        if (ergebnis == null) {
        
            return Optional.empty();
            
        } else {
            
            return Optional.of(ergebnis);
        }                
    }
    
}
