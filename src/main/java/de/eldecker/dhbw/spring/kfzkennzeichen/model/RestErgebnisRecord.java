package de.eldecker.dhbw.spring.kfzkennzeichen.model;

/**
 * Objekte dieser Klasse sollen in serialisierter Form (JSON) von REST-Methode zurückgegeben
 * werden.
 */
public record RestErgebnisRecord( boolean erfolgreich, 
                                  String fehlermeldung,
                                  Unterscheidungszeichen unterscheidungszeichen ) {
}
