#!/bin/bash

clear

./mvnw clean javadoc:javadoc -Dshow=private -DadditionalJOption=-Xdoclint:none
# -Xdoclint:none: Keine Warnung, wenn für eine Methode/Argument/Klasse kein JavaDoc-Kommentar angegeben

echo
