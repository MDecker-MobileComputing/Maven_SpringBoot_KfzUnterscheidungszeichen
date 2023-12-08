package de.eldecker.dhbw.spring.kfzkennzeichen.model;

/**
 * Ergebnistyp für DB-Abfragen von Unterscheidungszeichen. 
 * 
 * @param kuerzel   Unterscheidungszeichen, z.B. "BA" für "Bamberg"
 * @param bedeutung Bedeutung von {@code kuerzel}, z.B. "Bamberg"
 * @param kategorie Kategorie des Unterscheidungszeichen, z.B. Bundesland oder "Militär"
 */
public record Unterscheidungszeichen( String kuerzel, 
                                      String bedeutung, 
                                      UZKategorieEnum kategorie) {    
}
