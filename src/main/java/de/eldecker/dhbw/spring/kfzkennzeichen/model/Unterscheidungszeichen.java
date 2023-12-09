package de.eldecker.dhbw.spring.kfzkennzeichen.model;

/**
 * Ergebnistyp für DB-Abfragen von Unterscheidungszeichen. Objekte dieser Klasse 
 * werden auch von REST-Methode an Client gesendet. 
 * 
 * @param kuerzel   Unterscheidungszeichen, z.B. "BA" für "Bamberg"
 * 
 * @param bedeutung Bedeutung von {@code kuerzel}, z.B. "Bamberg"
 * 
 * @param kategorie Kategorie des Unterscheidungszeichen, z.B. Bundesland oder "Militär"
 */
public record Unterscheidungszeichen( String kuerzel, 
                                      String bedeutung, 
                                      UZKategorieEnum kategorie) {    
}
