package tn.esprit.tpfoyer17.services.impementations;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.tpfoyer17.entities.Universite;
import tn.esprit.tpfoyer17.repositories.UniversiteRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
class UniversiteRepositoryTest {

    @Mock
    UniversiteRepository universiteRepository;

    @InjectMocks
    UniversiteService universiteService; // Pour injecter le mock dans le service

    @BeforeEach
    void setUp() {
        Universite manar = new Universite(1L, "Manar", "Some address", null);
        Universite sfax = new Universite(2L, "Sfax", "Another address", null);

        Mockito.when(universiteRepository.save(manar)).thenReturn(manar);
        Mockito.when(universiteRepository.save(sfax)).thenReturn(sfax);
        Mockito.when(universiteRepository.findById(1L)).thenReturn(Optional.empty()); // simulate absence
    }

    @AfterEach
    void destroy() {
        Mockito.reset(universiteRepository);
    }

    @Test
    void testGetInvalidUniversite() {
        assertThrows(NoSuchElementException.class, () -> {
            universiteRepository.findById(1L).get(); // Utilisez 1L pour spécifier un Long
        });
    }

    @Test
    void testDeleteUniversite() {
        Universite saved = new Universite(5L, "ron", "Sample address", null);

        Mockito.when(universiteRepository.findById(5L)).thenReturn(Optional.of(saved));
        universiteRepository.delete(saved);

        Mockito.when(universiteRepository.findById(5L)).thenReturn(Optional.empty()); // simulate deletion

        assertThrows(NoSuchElementException.class, () -> {
            universiteRepository.findById(5L).get();
        });
    }
}
