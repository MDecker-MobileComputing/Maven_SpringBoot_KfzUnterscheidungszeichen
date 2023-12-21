# Befehl um das Image zu bauen: docker build -t mide76/kfzkennzeichen:1.0 .

# Base-Image mit OpenJDK 17 auf Alpine Linux
FROM openjdk:17-jdk-alpine

# FatJar-Datei in das Image kopieren
COPY target/kfzkennzeichen-0.0.1-SNAPSHOT.jar kfzkennzeichen.jar

# Port 8080 freigeben (eher für Doku-Zwecke)
EXPOSE 8080

# Start der Java-Anwendung
ENTRYPOINT ["java","-jar","/kfzkennzeichen.jar"]
