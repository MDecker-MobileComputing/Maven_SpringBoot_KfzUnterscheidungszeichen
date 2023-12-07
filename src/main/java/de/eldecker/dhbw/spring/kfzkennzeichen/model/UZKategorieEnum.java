package de.eldecker.dhbw.spring.kfzkennzeichen.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Aufzählungstyp für Kategorie eines Unterscheidungszeichen von deutschen KFZ-Kennzeichen:
 * Entweder Bundesland, oder Organisation (Militär, Behörde).
 * <br><br>
 * 
 * Für Zwei-Buchstaben-Abkürzungen der Bundesländer siehe
 * <a href="https://www.giga.de/ratgeber/specials/abkuerzungen-der-bundeslaender-in-deutschland-tabelle/">diese Seite</a>. 
 */
public enum UZKategorieEnum {
 
    NICHT_DEFINIERT(""),
    
    BE("Berlin"),
    BB("Brandenburg"),
    HB("Bremen"),
    HH("Hamburg"),
    HE("Hessen"),
    MV("Mecklenburg-Vorpommern"),
    NI("Niedersachsen"),
    NW("Nordrhein-Westfalen"),
    RP("Rheinland-Pfalz"),
    SL("Saarland"),
    SN("Sachsen"),
    ST("Sachsen-Anhalt"),
    SH("Schleswig-Holstein"),
    TH("Thüringen"),
    BW("Baden-Württemberg"),
    BY("Bayern"),
    
    MIL("Militär"),
    BEH("Behörde/Organisation");
    

    /** Anzeigename des aufrufenden Elements des Aufzählungstyp, kann dem Endnutzer angezeigt werden. */
    private String _anzeigeName;
    
    /**
     * Konstruktor, um an Enum-Objekt den Wert für die Objektvariable mit dem Anzeigenamen zu setzen.
     * 
     * @param anzeigeName Anzeigenamen, z.B. "Bayern" oder "Militär" 
     */
    private UZKategorieEnum(String anzeigeName) {
        
        _anzeigeName = anzeigeName;
    }
    
    /**
     * Methode liefert String-Repräsentation eines Aufzählungsobjekts zurück, nämlich den
     * Anzeigenamen.<br><br>
     * 
     * Die Methode ist mit der Jackson-Annotation {@code JsonValue} versehen, damit ihr Wert
     * statt des technischen Namens des Enum-Elements in JSON-Strings verwendet wird
     * (also z.B. "" statt "NICHT_DEFINIERT" oder "Berlin" statt "BE").
     * 
     * @return Anzeigename, z.B. "Berlin" oder "Militär".
     */
    @Override
    @JsonValue
    public String toString() {
        
        return _anzeigeName;
    }
}
