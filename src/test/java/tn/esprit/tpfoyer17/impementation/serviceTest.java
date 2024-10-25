package tn.esprit.tpfoyer17.impementation;

import org.mockito.Mock;
import tn.esprit.tpfoyer17.repositories.BlocRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer17.entities.Bloc;
import tn.esprit.tpfoyer17.services.impementations.BlocService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
public class serviceTest {

    @Mock
    private BlocRepository blocRepository;

    @InjectMocks
    private BlocService blocService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRetrieveBlocs() {
        // Given
        List<Bloc> blocs = Arrays.asList(new Bloc(), new Bloc());
        when(blocRepository.findAll()).thenReturn(blocs);

        // When
        List<Bloc> result = blocService.retrieveBlocs();

        // Then
        assertEquals(2, result.size());
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    public void testAddBloc() {
        // Given
        Bloc bloc = new Bloc();
        bloc.setNom("Bloc A");
        when(blocRepository.save(any(Bloc.class))).thenReturn(bloc);

        // When
        Bloc result = blocService.addBloc(bloc);

        // Then
        assertEquals("Bloc A", result.getNom());
        verify(blocRepository, times(1)).save(bloc);
    }

    @Test
    public void testUpdateBloc() {
        // Given
        Bloc bloc = new Bloc();
        bloc.setId(1L);
        bloc.setNom("Updated Bloc");
        when(blocRepository.save(any(Bloc.class))).thenReturn(bloc);

        // When
        Bloc result = blocService.updateBloc(bloc);

        // Then
        assertEquals("Updated Bloc", result.getNom());
        verify(blocRepository, times(1)).save(bloc);
    }

    @Test
    public void testRetrieveBloc() {
        // Given
        Bloc bloc = new Bloc();
        bloc.setId(1L);
        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));

        // When
        Bloc result = blocService.retrieveBloc(1L);

        // Then
        assertEquals(1L, result.getId());
        verify(blocRepository, times(1)).findById(1L);
    }

    @Test
    public void testRetrieveBlocNotFound() {
        // Given
        when(blocRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Bloc result = blocService.retrieveBloc(1L);

        // Then
        assertNull(result);
        verify(blocRepository, times(1)).findById(1L);
    }

    @Test
    public void testRemoveBloc() {
        // When
        blocService.removeBloc(1L);

        // Then
        verify(blocRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindByFoyerIdFoyer() {
        // Given
        List<Bloc> blocs = Arrays.asList(new Bloc(), new Bloc());
        when(blocRepository.findByFoyerIdFoyer(1L)).thenReturn(blocs);

        // When
        List<Bloc> result = blocService.findByFoyerIdFoyer(1L);

        // Then
        assertEquals(2, result.size());
        verify(blocRepository, times(1)).findByFoyerIdFoyer(1L);
    }

    @Test
    public void testFindByChambresIdChambre() {
        // Given
        Bloc bloc = new Bloc();
        when(blocRepository.findByChambresIdChambre(1L)).thenReturn(bloc);

        // When
        Bloc result = blocService.findByChambresIdChambre(1L);

        // Then
        assertEquals(bloc, result);
        verify(blocRepository, times(1)).findByChambresIdChambre(1L);
    }
}
