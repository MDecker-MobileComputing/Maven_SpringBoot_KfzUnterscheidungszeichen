#!/bin/bash

clear

./mvnw clean javadoc:javadoc -Dshop=private -DadditionalJOption=-Xdoclint:none
# -Xdoclint:none: Keine Warnung, wenn für eine Methode/Argument/Klasse kein JavaDoc-Kommentar angegeben

echo
