# Befehl um das Image zu bauen: docker build -t mide76/kfz-kennzeichen:1.0 .
# Container mit Image starten: docker run -p 8080:8080 mide76/kfz-kennzeichen:1.0
# Repo on Docker Hub: https://hub.docker.com/repository/docker/mide76/kfz-kennzeichen/general

# Base-Image mit OpenJDK 17 auf Alpine Linux
FROM openjdk:17-jdk-alpine

# FatJar-Datei in das Image kopieren
COPY target/kfzkennzeichen-0.0.1-SNAPSHOT.jar kfzkennzeichen.jar

# Port 8080 freigeben (eher für Doku-Zwecke)
EXPOSE 8080

# Start der Java-Anwendung
ENTRYPOINT ["java","-jar","/kfzkennzeichen.jar"]
