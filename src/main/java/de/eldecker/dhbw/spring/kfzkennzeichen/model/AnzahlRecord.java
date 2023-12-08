package de.eldecker.dhbw.spring.kfzkennzeichen.model;

/**
 * Record/Klasse für Rückgabe Anzahl der Datensätze durch REST-Endpunkt.
 * 
 * @param anzahl Gesamtanzahl Unterscheidungszeichen in Datenbank
 */
public record AnzahlRecord(int anzahl) {}
