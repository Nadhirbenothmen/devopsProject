package tn.esprit.tpfoyer17.services.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.tpfoyer17.entities.Bloc;
import tn.esprit.tpfoyer17.entities.Foyer;
import tn.esprit.tpfoyer17.repositories.BlocRepository;
import tn.esprit.tpfoyer17.services.impementations.BlocService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest // Lance une instance complète du contexte Spring
class BlocServiceTest {

    @Autowired
    private BlocRepository blocRepository;

    @Autowired
    private BlocService blocService;

    private Bloc bloc;

    @BeforeEach
    void setUp() {
        // Initialiser un Foyer et un Bloc pour le test
        Foyer foyer = Foyer.builder()
                .nomFoyer("Foyer A")
                .capaciteFoyer(300)
                .build();

        bloc = Bloc.builder()
                .nomBloc("Bloc A")
                .capaciteBloc(100)
                .foyer(foyer)
                .build();

        // Sauvegarder les entités dans la base de données H2 en mémoire
        blocRepository.save(bloc);
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
}
