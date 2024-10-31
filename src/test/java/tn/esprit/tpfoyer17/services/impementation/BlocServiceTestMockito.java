package tn.esprit.tpfoyer17.services.impementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer17.entities.Bloc;
import tn.esprit.tpfoyer17.entities.Foyer;
import tn.esprit.tpfoyer17.repositories.BlocRepository;
import tn.esprit.tpfoyer17.repositories.FoyerRepository; // Assuming this repository exists
import tn.esprit.tpfoyer17.services.impementations.BlocService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BlocServiceTestMockito {
    @Mock
    BlocRepository blocRepository;

    @Mock
    FoyerRepository foyerRepository; // Mock for Foyer repository

    @InjectMocks
    BlocService blocService;

    Bloc bloc;
    Foyer foyer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create and save the foyer before associating it with the bloc
        foyer = Foyer.builder()
                .idFoyer(1L)
                .nomFoyer("Foyer A")
                .capaciteFoyer(300L)
                .build();

        // Mock the FoyerRepository save method
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        bloc = Bloc.builder()
                .idBloc(1L)
                .nomBloc("Bloc A")
                .capaciteBloc(100L)
                .foyer(foyer)
                .chambres(Set.of()) // Use an empty set for simplicity
                .build();
    }

    @Test
    void testRetrieveBlocs() {
        List<Bloc> blocList = new ArrayList<>();
        blocList.add(bloc);

        when(blocRepository.findAll()).thenReturn(blocList);

        List<Bloc> result = blocService.retrieveBlocs();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Bloc A", result.get(0).getNomBloc());
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    void testUpdateBloc() {
        when(blocRepository.save(bloc)).thenReturn(bloc);

        Bloc result = blocService.updateBloc(bloc);

        assertNotNull(result);
        assertEquals("Bloc A", result.getNomBloc());
        verify(blocRepository, times(1)).save(bloc);
    }

    @Test
    void testAddBloc() {
        when(blocRepository.save(bloc)).thenReturn(bloc);

        Bloc result = blocService.addBloc(bloc);

        assertNotNull(result);
        assertEquals("Bloc A", result.getNomBloc());
        verify(blocRepository, times(1)).save(bloc);
    }

    @Test
    void testRetrieveBloc() {
        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));

        Bloc result = blocService.retrieveBloc(1L);

        assertNotNull(result);
        assertEquals("Bloc A", result.getNomBloc());
        verify(blocRepository, times(1)).findById(1L);
    }

    @Test
    void testRemoveBloc() {
        blocService.removeBloc(1L);

        verify(blocRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByFoyerIdFoyer() {
        List<Bloc> blocList = new ArrayList<>();
        blocList.add(bloc);

        when(blocRepository.findByFoyerIdFoyer(1L)).thenReturn(blocList);

        List<Bloc> result = blocService.findByFoyerIdFoyer(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(blocRepository, times(1)).findByFoyerIdFoyer(1L);
    }

    @Test
    void testFindByChambresIdChambre() {
        when(blocRepository.findByChambresIdChambre(1L)).thenReturn(bloc);

        Bloc result = blocService.findByChambresIdChambre(1L);

        assertNotNull(result);
        assertEquals("Bloc A", result.getNomBloc());
        verify(blocRepository, times(1)).findByChambresIdChambre(1L);
    }
}
