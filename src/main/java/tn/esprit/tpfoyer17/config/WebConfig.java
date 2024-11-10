package tn.esprit.tpfoyer17.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")  // Permet les requêtes CORS sur toutes les routes
                .allowedOrigins("http://localhost:4200")  // URL de votre frontend Angular
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Autoriser GET, POST, PUT et DELETE
                .allowedHeaders("*")  // Autoriser tous les en-têtes
                .allowCredentials(true);  // Autoriser les cookies et autres informations d'identification
    }
}
