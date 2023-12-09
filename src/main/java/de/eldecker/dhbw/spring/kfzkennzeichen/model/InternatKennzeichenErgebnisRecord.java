package de.eldecker.dhbw.spring.kfzkennzeichen.model;

/**
 * Ein Objekt dieser Klasse wird von der REST-Methode als Ergebnis für die Abfrage 
 * nach einem internationalen KFZ-Kennzeichen zurückgegeben. 
 *
 * @param erfolgreich {@code true} gdw. das internationale KFZ-Kennzeichen gefunden wurde
 * @param fehlermeldung wenn {@code erfolgreich=false}, dann ist dieser String nicht leer
 * @param bedeutung Gefundene Bedeutung für internationales KFZ-Kennzeichen, z.B. "Ungarn" für "H";
 *                  nur gefüllt wenn {@code erfolgreich=true}
 */
public record InternatKennzeichenErgebnisRecord( boolean erfolgreich,
                                                 String fehlermeldung,
                                                 String bedeutung ) {
}