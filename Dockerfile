# Utiliser l'image de base openjdk
FROM openjdk:17-slim

# Exposer le port de l'application
EXPOSE 8082

# Copier le JAR généré par le build Maven (assurez-vous que le chemin est correct)
COPY target/tpFoyer-17-0.0.1.jar tpFoyer-17.jar

# Définir le point d'entrée pour exécuter l'application
ENTRYPOINT ["java", "-jar", "tpFoyer-17.jar"]






#FROM openjdk:17-slim

#EXPOSE 8082

# Installer curl
#RUN apt-get update && apt-get install -y curl

# Télécharger le JAR depuis le dépôt distant
#RUN curl -o tpFoyer-17.jar -L "http://192.168.1.6:8081/repository/maven-releases/tn/esprit/spring/tpFoyer-17/0.0.1/tpFoyer-17-0.0.1.jar"

# Définir le point d'entrée pour exécuter l'application
#ENTRYPOINT ["java", "-jar", "tpFoyer-17.jar"]