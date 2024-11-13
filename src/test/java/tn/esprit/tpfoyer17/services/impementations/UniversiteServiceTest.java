package tn.esprit.tpfoyer17.services.impementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer17.entities.Universite;
import tn.esprit.tpfoyer17.repositories.UniversiteRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UniversiteServiceTest {

    @Mock
    private UniversiteRepository universiteRepository;

    @InjectMocks
    private UniversiteService universiteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateUniversity() {
        // Objet existant avec des données initiales
        Universite existingUniversite = Universite.builder().idUniversite(15L).nomUniversite("oldName").build();

        // Nouvelle version de l'objet Université avec des données mises à jour
        Universite updatedUniversite = Universite.builder().idUniversite(15L).nomUniversite("newName").build();

        // Simulation de la récupération de l'université existante et de la sauvegarde de la mise à jour
        when(universiteRepository.findById(15L)).thenReturn(Optional.of(existingUniversite));
        when(universiteRepository.save(any(Universite.class))).thenReturn(updatedUniversite);

        // Appel de la méthode avec uniquement l'objet mis à jour
        Universite result = universiteService.updateUniversity(updatedUniversite);

        // Vérifications
        assertNotNull(result);
        assertEquals("newName", result.getNomUniversite());
        verify(universiteRepository, times(1)).save(updatedUniversite); // Vérifie que la nouvelle version est sauvegardée
    }

    @Test
    void testRetrieveUniversityNotFound() {
        // Simulation d'une université non trouvée
        when(universiteRepository.findById(15L)).thenReturn(Optional.empty());

        // Appel de la méthode et vérification de l'exception
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            universiteService.retrieveUniversity(15L);
        });

        assertEquals("Université non trouvée", thrown.getMessage());
    }

    @Test
    void testRetrieveUniversityFound() {
        // Création d'un objet Université simulé
        Universite universite = Universite.builder().idUniversite(15L).nomUniversite("Test University").build();

        // Simulation de la récupération d'une université par ID
        when(universiteRepository.findById(15L)).thenReturn(Optional.of(universite));

        // Appel de la méthode à tester
        Universite result = universiteService.retrieveUniversity(15L);

        // Vérification des résultats
        assertNotNull(result);
        assertEquals("Test University", result.getNomUniversite());
        assertEquals(15L, result.getIdUniversite());
    }

    // Ajoutez d'autres tests selon vos besoins
}
