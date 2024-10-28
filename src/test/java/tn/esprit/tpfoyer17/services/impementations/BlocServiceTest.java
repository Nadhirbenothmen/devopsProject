package tn.esprit.tpfoyer17.services.impementations;  // Correction ici

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpfoyer17.entities.Bloc;
import tn.esprit.tpfoyer17.repositories.BlocRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BlocServiceTest {

    @InjectMocks
    private BlocService blocService;

    @Mock
    private BlocRepository blocRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialisation des mocks
    }

    @Test
    void testAddBloc() {
        // Arrange
        Bloc bloc = Bloc.builder().nomBloc("BlocTest").build();  // Création d'un bloc de test
        when(blocRepository.save(any(Bloc.class))).thenReturn(bloc);  // Simulation de la sauvegarde

        // Act
        Bloc savedBloc = blocService.addBloc(bloc);  // Appel de la méthode à tester

        // Assert
        assertNotNull(savedBloc);  // Vérification que le bloc sauvegardé n'est pas null
        assertEquals("BlocTest", savedBloc.getNomBloc());  // Vérification du nom du bloc
        verify(blocRepository, times(1)).save(bloc);  // Vérification que la méthode save a été appelée une fois
    }

    @Test
    void testDeleteBloc() {
        // Arrange
        Bloc blocToDelete = Bloc.builder().idBloc(1L).nomBloc("BlocToDelete").build();  // Bloc à supprimer
        when(blocRepository.findById(1L)).thenReturn(Optional.of(blocToDelete));  // Simulation de la recherche

        // Act
        blocService.removeBloc(1L);  // Appel de la méthode à tester

        // Assert
        verify(blocRepository, times(1)).deleteById(1L);  // Vérification que la méthode deleteById a été appelée une fois
    }
}
