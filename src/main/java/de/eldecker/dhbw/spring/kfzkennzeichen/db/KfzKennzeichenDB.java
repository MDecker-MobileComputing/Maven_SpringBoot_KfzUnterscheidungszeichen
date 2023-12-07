package de.eldecker.dhbw.spring.kfzkennzeichen.db;

import static de.eldecker.dhbw.spring.kfzkennzeichen.model.UZKategorieEnum.BEH;
import static de.eldecker.dhbw.spring.kfzkennzeichen.model.UZKategorieEnum.BE;
import static de.eldecker.dhbw.spring.kfzkennzeichen.model.UZKategorieEnum.BW;
import static de.eldecker.dhbw.spring.kfzkennzeichen.model.UZKategorieEnum.BY;
import static de.eldecker.dhbw.spring.kfzkennzeichen.model.UZKategorieEnum.HE;
import static de.eldecker.dhbw.spring.kfzkennzeichen.model.UZKategorieEnum.MIL;
import static de.eldecker.dhbw.spring.kfzkennzeichen.model.UZKategorieEnum.NW;
import static de.eldecker.dhbw.spring.kfzkennzeichen.model.UZKategorieEnum.SN;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import de.eldecker.dhbw.spring.kfzkennzeichen.model.UZKategorieEnum;
import de.eldecker.dhbw.spring.kfzkennzeichen.model.Unterscheidungszeichen;
import jakarta.annotation.PostConstruct;

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
    
    private static Logger LOG = LoggerFactory.getLogger(KfzKennzeichenDB.class);
    
    /** 
     * Siehe Konfig-Property {@code unterscheidungszeichen.militaer_ausblenden} in Datei {@code src/main/resources/application.properties} .  
     * Der Wert steht noch nicht im Konstruktor zur Verfügung!
     */ 
    @Value( "${unterscheidungszeichen.militaer_ausblenden:false}" )    
    private boolean _konfigMilitaerischeNichtZeigen;
        
    /** 
     * Map mit Daten zu Unterscheidungszeichen, bildet auf Großbuchstaben normalisierten
     * Unterscheidungszeichen auf ein Objekt der Klasse {@code Unterscheidungszeichen} ab. 
     */
    private Map<String,Unterscheidungszeichen> _datenMap = new HashMap<>(100);
        
    /**
     * Datenbank füllen. Wir verwenden hierzu nicht den Konstruktor, weil im Konstruktor
     * der mit der Annotation {@Value} injizierte Konfigurationswert noch nicht zur Verfügung
     * steht.   
     */
    @PostConstruct
    public void initialisierung() {

        addEintrag("B"  , "Berlin"        , BE);
        addEintrag("BA" , "Bamberg"       , BY);
        addEintrag("BAD", "Baden-Baden"   , BW);
        addEintrag("K"  , "Köln"          , NW); 
        addEintrag("M"  , "München"       , BY); 
        addEintrag("S"  , "Stuttgart"     , BW); 
        addEintrag("F"  , "Frankfurt/Main", HE); 
        addEintrag("D"  , "Dresden"       , SN); 
                
        addEintrag("BW" , "Wasserstraßen- und Schifffahrtsverwaltung des Bundes", BEH);
        addEintrag("THW", "Technischen Hilfswerks"                              , BEH);
        
        addEintrag("X", "Nato"       , MIL);
        addEintrag("Y", "Bundeswehr" , MIL);
        
        if (_konfigMilitaerischeNichtZeigen == false) {
            
            LOG.warn("Es wurden auch die militärischen Unterscheidungszeichen geladen.");
            // für den gegenteiligen Fall werden von der Methode addEintrag() Log-Einträge geschrieben 
        }
     
        LOG.info("Anzahl Unterscheidungszeichen in DB: {}", _datenMap.size());
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
        
        if (_konfigMilitaerischeNichtZeigen && kategorie == MIL) {
            
            LOG.info("Abfrage militärisches Unterscheidungszeichen \"{}\" wegen Konfiguration nicht geladen.", kuerzelNormalized );
            return;
        }
                        
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
        
        LOG.info("Konfig-Wert: {}", _konfigMilitaerischeNichtZeigen);
        
        Unterscheidungszeichen ergebnis = _datenMap.get(kuerzelNormalized);
        if (ergebnis == null) {
        
            return Optional.empty();
            
        } else {
            
            return Optional.of(ergebnis);
        }                
    }
    
}
