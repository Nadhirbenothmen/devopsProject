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
        Universite existingUniversite = Universite.builder().idUniversite(15).nomUniversite("oldName").build();
        Universite updatedUniversite = Universite.builder().idUniversite(15).nomUniversite("newName").build();

        when(universiteRepository.findById(15L)).thenReturn(Optional.of(existingUniversite));
        when(universiteRepository.save(any(Universite.class))).thenReturn(updatedUniversite);

        Universite result = universiteService.updateUniversity(15L, updatedUniversite);

        assertNotNull(result);
        assertEquals("newName", result.getNomUniversite());
        verify(universiteRepository, times(1)).save(existingUniversite); // Vérifiez que l'ancien objet est enregistré
    }

    @Test
    void testRetrieveUniversityNotFound() {
        when(universiteRepository.findById(15L)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            universiteService.retrieveUniversity(15L);
        });

        assertEquals("Université non trouvée", thrown.getMessage());
    }

    // Ajoutez d'autres tests selon vos besoins
}
