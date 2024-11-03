package tn.esprit.tpfoyer17.services.impementations;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpfoyer17.entities.Reservation;
import tn.esprit.tpfoyer17.repositories.ReservationRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class ReservationServiceTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    private Reservation reservation;

    @BeforeEach
    public void setUp() {
        reservation = new Reservation();
        reservation.setIdReservation("R001");
        reservation.setAnneeUniversitaire(LocalDate.of(2024, 9, 1));
        reservation.setEstValide(true);
        entityManager.persist(reservation);
    }

    @Test
    void testRetrieveAllReservations() {
        // Arrange
        Reservation reservation1 = createReservation("R002");
        Reservation reservation2 = createReservation("R003");
        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);

        // Act
        List<Reservation> result = reservationService.retrieveAllReservation();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size()); // Compte la réservation initiale et les deux nouvelles
    }

    @Test
    void testAddReservation() {
        // Arrange
        long chambreId = 1L; // Remplacez par un ID de chambre valide dans votre base de données
        long cinEtudiant = 123456789L; // Remplacez par un CIN d'étudiant valide dans votre base de données

        // Act
        Reservation savedReservation = reservationService.ajouterReservation(chambreId, cinEtudiant);

        // Assert
        assertNotNull(savedReservation);
        assertEquals("1-BLOC-2024", savedReservation.getIdReservation()); // Assurez-vous que cela correspond à votre logique d'ID
        assertTrue(savedReservation.isEstValide());
    }


    @Test
    void testUpdateReservation() {
        // Arrange
        reservation.setEstValide(false);

        // Act
        Reservation updatedReservation = reservationService.updateReservation(reservation);

        // Assert
        assertFalse(updatedReservation.isEstValide());
    }

    @Test
    void testRetrieveReservation() {
        // Act
        Reservation result = reservationService.retrieveReservation(reservation.getIdReservation());

        // Assert
        assertNotNull(result);
        assertEquals("R001", result.getIdReservation());
    }



    private Reservation createReservation(String id) {
        return Reservation.builder()
                .idReservation(id)
                .anneeUniversitaire(LocalDate.now())
                .estValide(true)
                .build();
    }
}
