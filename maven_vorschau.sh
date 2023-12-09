#!/bin/bash

# Anwendung mit Profil "vorschau" starten:
# * Port-Nummer 8000
# * Zusätzliche REST-Endpunkte für internationale KFZ-Kennzeichen

echo

#export spring_profiles_active=vorschau
#./mvnw clean spring-boot:run

./mvnw spring-boot:run -Dspring-boot.run.profiles=vorschau

echo
