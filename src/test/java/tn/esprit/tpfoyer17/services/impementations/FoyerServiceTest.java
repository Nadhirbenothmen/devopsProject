package tn.esprit.tpfoyer17.services.impementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer17.entities.Foyer;
import tn.esprit.tpfoyer17.entities.Universite;
import tn.esprit.tpfoyer17.repositories.BlocRepository;
import tn.esprit.tpfoyer17.repositories.FoyerRepository;
import tn.esprit.tpfoyer17.repositories.UniversiteRepository;
import tn.esprit.tpfoyer17.services.impementations.FoyerService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FoyerServiceTest {

    @Mock
    private FoyerRepository foyerRepository;

    @Mock
    private BlocRepository blocRepository;

    @Mock
    private UniversiteRepository universiteRepository;

    @InjectMocks
    private FoyerService foyerService;

    private Foyer foyer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize a Foyer
        foyer = Foyer.builder()
                .nomFoyer("Foyer A")
                .capaciteFoyer(300)
                .blocs(new HashSet<>())
                .build();

        // Mock the save behavior for the repository
        when(foyerRepository.save(any(Foyer.class))).thenReturn(foyer);
    }

    @Test
    void testRetrieveAllFoyers() {
        when(foyerRepository.findAll()).thenReturn(List.of(foyer));

        List<Foyer> result = foyerService.retrieveAllFoyers();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("Foyer A", result.get(0).getNomFoyer());
    }

    @Test
    void testAddFoyer() {
        Foyer newFoyer = Foyer.builder()
                .nomFoyer("Foyer B")
                .capaciteFoyer(400)
                .blocs(new HashSet<>())
                .build();

        when(foyerRepository.save(any(Foyer.class))).thenReturn(newFoyer);

        Foyer result = foyerService.addFoyer(newFoyer);

        assertNotNull(result);
        assertEquals("Foyer B", result.getNomFoyer());
    }

    @Test
    void testUpdateFoyer() {
        foyer.setNomFoyer("Updated Foyer A");
        when(foyerRepository.save(any(Foyer.class))).thenReturn(foyer);

        Foyer result = foyerService.updateFoyer(foyer);

        assertNotNull(result);
        assertEquals("Updated Foyer A", result.getNomFoyer());
    }

    @Test
    void testRetrieveFoyer() {
        when(foyerRepository.findById(anyLong())).thenReturn(Optional.of(foyer));

        Foyer result = foyerService.retrieveFoyer(foyer.getIdFoyer());

        assertNotNull(result);
        assertEquals("Foyer A", result.getNomFoyer());
    }

    @Test
    void testRemoveFoyer() {
        doNothing().when(foyerRepository).deleteById(anyLong());

        foyerService.removeFoyer(foyer.getIdFoyer());

        verify(foyerRepository, times(1)).deleteById(foyer.getIdFoyer());
    }

    @Test
    void testAjouterFoyerEtAffecterAUniversite() {
        Universite universite = Universite.builder()
                .idUniversite(1L)
                .foyer(foyer)
                .build();

        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));
        when(foyerRepository.save(any(Foyer.class))).thenReturn(foyer);

        Foyer result = foyerService.ajouterFoyerEtAffecterAUniversite(foyer, 1L);

        assertNotNull(result);
        assertEquals("Foyer A", result.getNomFoyer());
    }
}
