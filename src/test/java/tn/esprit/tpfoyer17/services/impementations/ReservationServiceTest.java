package tn.esprit.tpfoyer17.services.impementations;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpfoyer17.entities.Bloc;
import tn.esprit.tpfoyer17.entities.Chambre;
import tn.esprit.tpfoyer17.entities.Etudiant;
import tn.esprit.tpfoyer17.entities.Reservation;
import tn.esprit.tpfoyer17.entities.enumerations.TypeChambre;
import tn.esprit.tpfoyer17.repositories.ChambreRepository;
import tn.esprit.tpfoyer17.repositories.EtudiantRepository;
import tn.esprit.tpfoyer17.repositories.ReservationRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReservationServiceTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private ChambreRepository chambreRepository;

    private Bloc bloc;
    private Chambre chambre;
    private Etudiant etudiant;

    @BeforeEach
    public void setUp() {
        bloc = new Bloc();
        bloc.setNomBloc("Bloc A");
        bloc.setCapaciteBloc(50L);
        entityManager.persist(bloc);

        chambre = new Chambre();
        chambre.setNumeroChambre(101L);
        chambre.setTypeChambre(TypeChambre.SIMPLE);
        chambre.setBloc(bloc);
        entityManager.persist(chambre);

        etudiant = new Etudiant();
        etudiant.setCinEtudiant(123456789L);
        etudiant.setNomEtudiant("John");
        etudiant.setPrenomEtudiant("Doe");
        entityManager.persist(etudiant);
    }

    @Test
    void testAddReservation() {
        // Arrange
        // Assurez-vous que la chambre et l'étudiant sont déjà persistés dans le setUp

        // Act
        Reservation reservation = reservationService.ajouterReservation(chambre.getIdChambre(), etudiant.getCinEtudiant());

        // Assert
        assertNotNull(reservation);
        assertTrue(reservation.getIdReservation().startsWith(String.valueOf(chambre.getNumeroChambre()))); // Vérifie que l'ID commence par le numéro de chambre
    }

    @Test
    void testRetrieveAllReservations() {
        // Arrange
        Reservation reservation1 = createReservation("R1");
        Reservation reservation2 = createReservation("R2");
        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);

        // Act
        List<Reservation> result = reservationService.retrieveAllReservation();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    private Reservation createReservation(String id) {
        return new Reservation(id, LocalDate.now(), true, new HashSet<>());
    }
}
