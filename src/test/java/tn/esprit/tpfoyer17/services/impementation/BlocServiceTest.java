package tn.esprit.tpfoyer17.services.impementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.tpfoyer17.entities.Bloc;
import tn.esprit.tpfoyer17.entities.Chambre; // Importer l'entité Chambre
import tn.esprit.tpfoyer17.entities.Foyer;
import tn.esprit.tpfoyer17.repositories.BlocRepository;
import tn.esprit.tpfoyer17.repositories.ChambreRepository; // Assurez-vous d'importer le repository de Chambre
import tn.esprit.tpfoyer17.repositories.FoyerRepository;
import tn.esprit.tpfoyer17.services.impementations.BlocService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest // Launch a complete instance of the Spring context
class BlocServiceTest {

    @Autowired
    private BlocRepository blocRepository;

    @Autowired
    private FoyerRepository foyerRepository;

    @Autowired
    private ChambreRepository chambreRepository; // Injecter le repository de Chambre

    @Autowired
    private BlocService blocService;

    private Bloc bloc;

    @BeforeEach
    void setUp() {
        // Clear the database to avoid conflicts
        blocRepository.deleteAll();
        foyerRepository.deleteAll();
        chambreRepository.deleteAll(); // Clear chambres as well

        // Initialize a Foyer and save it to the database
        Foyer foyer = Foyer.builder()
                .nomFoyer("Foyer A")
                .capaciteFoyer(300)
                .build();

        foyer = foyerRepository.save(foyer); // Save the foyer first

        // Initialize the Bloc with the saved foyer
        bloc = Bloc.builder()
                .nomBloc("Bloc A")
                .capaciteBloc(100)
                .foyer(foyer) // Use the saved foyer
                .build();

        // Save the Bloc in the database
        bloc = blocRepository.save(bloc); // Save the bloc

        // Create a Chambre associated with the Bloc
        Chambre chambre = Chambre.builder()
                .bloc(bloc) // Associate the chambre with the bloc
                .build();

        // Save the Chambre in the database
        chambreRepository.save(chambre); // Save the chambre
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
        Bloc result = blocService.updateBloc(bloc);

        assertNotNull(result);
        assertEquals("Bloc B", result.getNomBloc());
    }

    @Test
    void testAddBloc() {
        Bloc newBloc = Bloc.builder()
                .nomBloc("Bloc C")
                .capaciteBloc(120)
                .foyer(bloc.getFoyer())
                .build();

        Bloc result = blocService.addBloc(newBloc);

        assertNotNull(result);
        assertEquals("Bloc C", result.getNomBloc());

        // Verify the new Bloc is saved in the database
        Bloc savedBloc = blocRepository.findById(result.getIdBloc()).orElse(null);
        assertNotNull(savedBloc);
        assertEquals("Bloc C", savedBloc.getNomBloc());
    }

    @Test
    void testRetrieveBloc() {
        Bloc result = blocService.retrieveBloc(bloc.getIdBloc());

        assertNotNull(result);
        assertEquals("Bloc A", result.getNomBloc());
    }

    @Test
    void testRemoveBloc() {
        blocService.removeBloc(bloc.getIdBloc());
        Bloc result = blocService.retrieveBloc(bloc.getIdBloc());

        assertNull(result);
    }

    // Test for findByFoyerIdFoyer method
    @Test
    void testFindByFoyerIdFoyer() {
        List<Bloc> result = blocService.findByFoyerIdFoyer(bloc.getFoyer().getIdFoyer());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("Bloc A", result.get(0).getNomBloc());
    }

    // Test for findByChambresIdChambre method
    @Test
    void testFindByChambresIdChambre() {
        // Récupérer la chambre associée au bloc
        Chambre chambre = chambreRepository.findByBlocId(bloc.getIdBloc()).get(0); // Récupérer la chambre associée

        Bloc result = blocService.findByChambresIdChambre(chambre.getIdChambre()); // Utiliser l'ID de la chambre récupérée

        assertNotNull(result);
        assertEquals("Bloc A", result.getNomBloc());
    }
}
