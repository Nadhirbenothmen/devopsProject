package tn.esprit.tpfoyer17.services.impementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpfoyer17.entities.Bloc;
import tn.esprit.tpfoyer17.entities.Chambre;
import tn.esprit.tpfoyer17.entities.enumerations.TypeChambre;
import tn.esprit.tpfoyer17.repositories.BlocRepository;
import tn.esprit.tpfoyer17.repositories.ChambreRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class ChambreServiceTest {

    @Autowired
    ChambreRepository chambreRepository;

    @Autowired
    BlocRepository blocRepository;

    @Autowired
    ChambreService chambreService;

    Bloc testBloc;
    Chambre testChambre;

    @BeforeEach
    void setUp() {
        // Initialize a test Bloc and Chambre
        testBloc = new Bloc();
        testBloc = blocRepository.save(testBloc);

        testChambre = Chambre.builder()
                .numeroChambre(101L)
                .typeChambre(TypeChambre.DOUBLE)
                .bloc(testBloc)
                .build();
        chambreRepository.save(testChambre);
    }

    @Test
    void testAddChambre() {
        Chambre chambre = new Chambre();
        chambre.setNumeroChambre(102L);
        chambre.setTypeChambre(TypeChambre.SINGLE);

        Chambre savedChambre = chambreService.addChambre(chambre);

        assertNotNull(savedChambre);
        assertEquals(102L, savedChambre.getNumeroChambre());
        assertEquals(TypeChambre.SINGLE, savedChambre.getTypeChambre());
    }

    @Test
    void testRetrieveAllChambres() {
        List<Chambre> chambres = chambreService.retrieveAllChambres();

        assertFalse(chambres.isEmpty());
    }

    @Test
    void testUpdateChambre() {
        testChambre.setNumeroChambre(103L);
        Chambre updatedChambre = chambreService.updateChambre(testChambre);

        assertEquals(103L, updatedChambre.getNumeroChambre());
    }

    @Test
    void testRetrieveChambre() {
        Chambre foundChambre = chambreService.retrieveChambre(testChambre.getIdChambre());

        assertNotNull(foundChambre);
        assertEquals(testChambre.getIdChambre(), foundChambre.getIdChambre());
    }

    @Test
    void testAffecterChambresABloc() {
        Bloc newBloc = new Bloc();
        newBloc = blocRepository.save(newBloc);

        Chambre chambre1 = Chambre.builder().numeroChambre(201L).typeChambre(TypeChambre.SINGLE).build();
        Chambre chambre2 = Chambre.builder().numeroChambre(202L).typeChambre(TypeChambre.DOUBLE).build();

        chambreRepository.save(chambre1);
        chambreRepository.save(chambre2);

        List<Long> chambreNumbers = List.of(201L, 202L);
        Bloc affectedBloc = chambreService.affecterChambresABloc(chambreNumbers, newBloc.getIdBloc());

        assertNotNull(affectedBloc);
        List<Chambre> affectedChambres = chambreRepository.findByBlocIdBloc(newBloc.getIdBloc());

        assertEquals(2, affectedChambres.size());
        assertEquals(newBloc.getIdBloc(), affectedChambres.get(0).getBloc().getIdBloc());
        assertEquals(newBloc.getIdBloc(), affectedChambres.get(1).getBloc().getIdBloc());
    }
}
