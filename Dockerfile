FROM openjdk:17-slim

EXPOSE 8082

# Installer curl
RUN apt-get update && apt-get install -y curl

# Télécharger le JAR depuis le dépôt distant
RUN curl -o tpFoyer-17.jar -L "http://192.168.1.45:8081/repository/maven-releases/tn/esprit/spring/tpFoyer-17/0.0.1/tpFoyer-17-0.0.1.jar"

# Définir le point d'entrée pour exécuter l'application
ENTRYPOINT ["java", "-jar", "tpFoyer-17.jar"]