package tn.esprit.tpfoyer17.services.impementations;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
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
    private Reservation reservation;

    @BeforeEach
    public void setUp() {
        // Configuration de la base de données de test
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

        reservation = new Reservation();
        reservation.setIdReservation("R001");
        reservation.setAnneeUniversitaire(LocalDate.of(2024, 9, 1));
        reservation.setEstValide(true);
        reservation.setEtudiants(new HashSet<>()); // Assurez-vous d'initialiser la collection
        entityManager.persist(reservation);
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

    @Test
    void testAddReservation() {
        // Arrange
        Chambre chambre = new Chambre();
        chambre.setNumeroChambre(102L);
        chambre.setTypeChambre(TypeChambre.SIMPLE);
        chambreRepository.save(chambre);

        Etudiant etudiant = new Etudiant();
        etudiant.setCinEtudiant(987654321L);
        etudiant.setNomEtudiant("Dupont");
        etudiant.setPrenomEtudiant("Jean");
        etudiantRepository.save(etudiant);

        // Act
        Reservation reservation = reservationService.ajouterReservation(chambre.getIdChambre(), etudiant.getCinEtudiant());

        // Assert
        assertNotNull(reservation);
        assertTrue(reservation.getIdReservation().startsWith(String.valueOf(chambre.getNumeroChambre()))); // Vérifie que l'ID commence par le numéro de chambre
    }

    @Test
    void testUpdateReservation() {
        // Arrange
        Reservation reservation = createReservation("R1");
        reservationRepository.save(reservation);
        reservation.setEstValide(false);

        // Act
        Reservation updatedReservation = reservationService.updateReservation(reservation);

        // Assert
        assertFalse(updatedReservation.isEstValide());
    }

    @Test
    void testRetrieveReservation() {
        // Arrange
        Reservation reservation = createReservation("R1");
        reservationRepository.save(reservation);

        // Act
        Reservation result = reservationService.retrieveReservation(reservation.getIdReservation());

        // Assert
        assertNotNull(result);
        assertEquals("R1", result.getIdReservation());
    }

    @Test
    void testGetReservationParAnneeUniversitaireEtNomUniversite() {
        // Arrange
        LocalDate anneeUniversitaire = LocalDate.of(2024, 9, 1); // Exemple d'année universitaire
        Chambre chambre = new Chambre();
        chambre.setNumeroChambre(101L);
        chambre.setTypeChambre(TypeChambre.SIMPLE);
        chambreRepository.save(chambre);

        Etudiant etudiant = new Etudiant();
        etudiant.setCinEtudiant(987654321L);
        etudiant.setNomEtudiant("Dupont");
        etudiant.setPrenomEtudiant("Jean");
        etudiantRepository.save(etudiant);

        reservationService.ajouterReservation(chambre.getIdChambre(), etudiant.getCinEtudiant());

        // Act
        List<Reservation> result = reservationService.getReservationParAnneeUniversitaireEtNomUniversite(anneeUniversitaire, "NomUniversite");

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    private Reservation createReservation(String id) {
        return new Reservation(id, LocalDate.now(), true, new HashSet<>());
    }
}
