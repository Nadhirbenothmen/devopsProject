package tn.esprit.tpfoyer17.services.impementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.tpfoyer17.entities.Bloc;
import tn.esprit.tpfoyer17.entities.Foyer;
import tn.esprit.tpfoyer17.repositories.BlocRepository;
import tn.esprit.tpfoyer17.repositories.FoyerRepository;
import tn.esprit.tpfoyer17.services.impementations.BlocService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest // Optionnel ici car nous allons utiliser Mockito
class BlocServiceTest {

    @Mock
    private BlocRepository blocRepository;

    @Mock
    private FoyerRepository foyerRepository;

    @InjectMocks
    private BlocService blocService;

    private Bloc bloc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialiser les mocks

        // Initialize a Foyer and Bloc
        Foyer foyer = Foyer.builder()
                .nomFoyer("Foyer A")
                .capaciteFoyer(300)
                .build();

        // Simuler le comportement du foyerRepository
        when(foyerRepository.save(any(Foyer.class))).thenReturn(foyer);

        bloc = Bloc.builder()
                .nomBloc("Bloc A")
                .capaciteBloc(100)
                .foyer(foyer)
                .build();

        // Simuler le comportement du blocRepository
        when(blocRepository.save(any(Bloc.class))).thenReturn(bloc);
        when(blocRepository.findById(bloc.getIdBloc())).thenReturn(Optional.of(bloc));
        when(blocRepository.findAll()).thenReturn(List.of(bloc));
    }

    @Test
    void testRetrieveBlocs() {
        List<Bloc> result = blocService.retrieveBlocs();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("Bloc A", result.get(0).getNomBloc());
    }

    @Test
    void testUpdateBloc() {
        bloc.setNomBloc("Bloc B");
        when(blocRepository.save(bloc)).thenReturn(bloc);

        Bloc result = blocService.updateBloc(bloc);

        assertNotNull(result);
        assertEquals("Bloc B", result.getNomBloc());
        assertEquals(100, result.getCapaciteBloc(), "La capacité du bloc doit rester 100 après la mise à jour.");
    }

    @Test
    void testAddBloc() {
        Bloc newBloc = Bloc.builder()
                .nomBloc("Bloc C")
                .capaciteBloc(120)
                .foyer(bloc.getFoyer())
                .build();

        when(blocRepository.save(newBloc)).thenReturn(newBloc);

        Bloc result = blocService.addBloc(newBloc);

        assertNotNull(result);
        assertEquals("Bloc C", result.getNomBloc());

        // Vérifier que le nouveau Bloc est enregistré
        verify(blocRepository, times(1)).save(newBloc);
    }

    @Test
    void testRetrieveBloc() {
        Bloc result = blocService.retrieveBloc(bloc.getIdBloc());

        assertNotNull(result);
        assertEquals("Bloc A", result.getNomBloc());
    }

    @Test
    void testRetrieveBlocNotFound() {
        when(blocRepository.findById(anyLong())).thenReturn(Optional.empty());

        Bloc result = blocService.retrieveBloc(999L); // Un ID qui n'existe pas

        assertNull(result);
    }

    @Test
    void testUpdateBlocNotFound() {
        when(blocRepository.findById(bloc.getIdBloc())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            blocService.updateBloc(bloc);
        });

        assertEquals("Bloc not found", exception.getMessage());
    }

    @Test
    void testRemoveBloc() {
        doNothing().when(blocRepository).deleteById(bloc.getIdBloc());
        blocService.removeBloc(bloc.getIdBloc());

        verify(blocRepository, times(1)).deleteById(bloc.getIdBloc());
    }

    @Test
    void testRemoveBlocNotFound() {
        doThrow(new RuntimeException("Bloc not found")).when(blocRepository).deleteById(anyLong());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            blocService.removeBloc(999L); // Un ID qui n'existe pas
        });

        assertEquals("Bloc not found", exception.getMessage());
    }

    @Test
    void testFindByFoyerIdFoyer() {
        when(blocRepository.findByFoyerIdFoyer(bloc.getFoyer().getIdFoyer())).thenReturn(List.of(bloc));

        List<Bloc> result = blocService.findByFoyerIdFoyer(bloc.getFoyer().getIdFoyer());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("Bloc A", result.get(0).getNomBloc());
    }

    @Test
    void testFindByChambresIdChambre() {
        when(blocRepository.findByChambresIdChambre(1L)).thenReturn(bloc);

        Bloc result = blocService.findByChambresIdChambre(1L);

        assertNotNull(result);
        assertEquals("Bloc A", result.getNomBloc());
        verify(blocRepository, times(1)).findByChambresIdChambre(1L);
    }

    @Test
    void testFindByChambresIdChambreNotFound() {
        when(blocRepository.findByChambresIdChambre(anyLong())).thenReturn(null);

        Bloc result = blocService.findByChambresIdChambre(999L); // ID qui n'existe pas

        assertNull(result);
        verify(blocRepository, times(1)).findByChambresIdChambre(999L);
    }
}
