# REST-API-Server für Abfrage Unterscheidungszeichen KFZ-Kennzeichen #

<br>

Dieses Repository enthält eine einfache Spring-Boot-Anwendung, die eine kleine REST-API zur Abfrage von
Unterscheidungszeichen von deutschen KFZ-Kennzeichen enthält (z.B. "BAD" für "Baden-Baden").

Nach Start der Anwendung (z.B. durch Skript `maven_start` im Wurzelverzeichnis des Repos) kann man lokal
unter http://localhost:8080 eine Dokumentationsseite abrufen, auf der Links zu möglichen REST-Anfragen enthalten sind.

<br>

**REST-Endpunkte:**
```
unterscheidungszeichen/v1/suche/<kuerzel>
unterscheidungszeichen/v1/anzahl
```

<br>

**Beispiel-URLs (lokaler Zugriff mit Standard-Port):**

* http://localhost:8080/unterscheidungszeichen/v1/anzahl (siehe unten für Response)
* http://localhost:8080/unterscheidungszeichen/v1/suche/b
* http://localhost:8080/unterscheidungszeichen/v1/suche/ba
* http://localhost:8080/unterscheidungszeichen/v1/suche/bad (siehe unten für Response)
* http://localhost:8080/unterscheidungszeichen/v1/suche/badx (illegal, weil zu lang)
* http://localhost:8080/unterscheidungszeichen/v1/suche/y

<br>

JSON-Response für Abfrage der Anzahl:
```
{ "anzahl": 10 }
```

<br>

JSON-Response für Abfrage "bad":
```
{
    "erfolgreich": true,
    "fehlermeldung": "",
    "unterscheidungszeichen": {
        "kuerzel": "BAD",
        "bedeutung": "Baden-Baden",
        "kategorie": "Baden-Württemberg"
    }
}
```

<br>

----

## License ##

<br>

See the [LICENSE file](LICENSE.md) for license rights and limitations (BSD 3-Clause License).

<br>