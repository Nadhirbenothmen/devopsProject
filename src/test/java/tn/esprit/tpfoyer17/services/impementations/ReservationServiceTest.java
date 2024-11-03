package tn.esprit.tpfoyer17.services.impementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpfoyer17.entities.Chambre;
import tn.esprit.tpfoyer17.entities.Etudiant;
import tn.esprit.tpfoyer17.entities.Reservation;
import tn.esprit.tpfoyer17.entities.enumerations.TypeChambre; // Assurez-vous que l'importation est correcte
import tn.esprit.tpfoyer17.repositories.ChambreRepository;
import tn.esprit.tpfoyer17.repositories.EtudiantRepository;
import tn.esprit.tpfoyer17.repositories.ReservationRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private ChambreRepository chambreRepository;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
        etudiantRepository.deleteAll();
        chambreRepository.deleteAll(); // Nettoyage de la base pour chaque test
    }

    @Test
    void testRetrieveAllReservations() {
        // Arrange
        Chambre chambre = Chambre.builder().typeChambre(TypeChambre.SIMPLE).build(); // Utilisation de l'énumération
        chambreRepository.save(chambre);

        Etudiant etudiant = Etudiant.builder().nomEtudiant("Dupont").prenomEtudiant("Jean").build();
        etudiantRepository.save(etudiant);

        Reservation reservation = Reservation.builder()
                .idReservation("1-" + chambre.getTypeChambre() + "-" + LocalDate.now().getYear())
                .anneeUniversitaire(LocalDate.now())
                .estValide(true)
                .etudiants(new HashSet<>(Set.of(etudiant)))
                .build();
        reservationRepository.save(reservation);

        // Act
        List<Reservation> result = reservationService.retrieveAllReservation();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testAddReservation() {
        // Arrange
        Chambre chambre = Chambre.builder().typeChambre(TypeChambre.DOUBLE).build(); // Utilisation de l'énumération
        chambreRepository.save(chambre);

        Etudiant etudiant = Etudiant.builder().nomEtudiant("Durand").prenomEtudiant("Paul").build();
        etudiantRepository.save(etudiant);

        // Act
        Reservation savedReservation = reservationService.ajouterReservation(chambre.getIdChambre(), etudiant.getCinEtudiant());

        // Assert
        assertNotNull(savedReservation);
        assertEquals(TypeChambre.DOUBLE, chambre.getTypeChambre()); // Vérifiez avec l'énumération
    }

    @Test
    void testUpdateReservation() {
        // Arrange
        Chambre chambre = Chambre.builder().typeChambre(TypeChambre.SIMPLE).build(); // Utilisation de l'énumération
        chambreRepository.save(chambre);

        Etudiant etudiant = Etudiant.builder().nomEtudiant("Dupont").prenomEtudiant("Jean").build();
        etudiantRepository.save(etudiant);

        Reservation reservation = Reservation.builder()
                .idReservation("1-" + chambre.getTypeChambre() + "-" + LocalDate.now().getYear())
                .anneeUniversitaire(LocalDate.now())
                .estValide(true)
                .etudiants(new HashSet<>(Set.of(etudiant)))
                .build();
        reservationRepository.save(reservation);

        // Act
        reservation.setEstValide(false);
        Reservation updatedReservation = reservationService.updateReservation(reservation);

        // Assert
        assertFalse(updatedReservation.isEstValide());
    }

    @Test
    void testRetrieveReservation() {
        // Arrange
        Chambre chambre = Chambre.builder().typeChambre(TypeChambre.SIMPLE).build(); // Utilisation de l'énumération
        chambreRepository.save(chambre);

        Etudiant etudiant = Etudiant.builder().nomEtudiant("Dupont").prenomEtudiant("Jean").build();
        etudiantRepository.save(etudiant);

        Reservation reservation = Reservation.builder()
                .idReservation("1-" + chambre.getTypeChambre() + "-" + LocalDate.now().getYear())
                .anneeUniversitaire(LocalDate.now())
                .estValide(true)
                .etudiants(new HashSet<>(Set.of(etudiant)))
                .build();
        Reservation savedReservation = reservationRepository.save(reservation);

        // Act
        Reservation result = reservationService.retrieveReservation(savedReservation.getIdReservation());

        // Assert
        assertNotNull(result);
        assertEquals("SIMPLE", result.getEtudiants().iterator().next().getNomEtudiant());
    }

    @Test
    void testAnnulerReservation() {
        // Arrange
        Chambre chambre = Chambre.builder().typeChambre(TypeChambre.TRIPLE).build(); // Utilisation de l'énumération
        chambreRepository.save(chambre);

        Etudiant etudiant = Etudiant.builder().nomEtudiant("Dupont").prenomEtudiant("Jean").build();
        etudiantRepository.save(etudiant);

        Reservation reservation = Reservation.builder()
                .idReservation("1-" + chambre.getTypeChambre() + "-" + LocalDate.now().getYear())
                .anneeUniversitaire(LocalDate.now())
                .estValide(true)
                .etudiants(new HashSet<>(Set.of(etudiant)))
                .build();
        reservationRepository.save(reservation);

        // Act
        reservationService.annulerReservation(etudiant.getCinEtudiant());

        // Assert
        Optional<Reservation> cancelledReservation = reservationRepository.findById(reservation.getIdReservation());
        assertTrue(cancelledReservation.isPresent());
        assertFalse(cancelledReservation.get().isEstValide());
    }

    @Test
    void testGetReservationParAnneeUniversitaireEtNomUniversite() {
        // Arrange
        // Simule l'ajout d'une réservation et de ses dépendances
        LocalDate anneeUniversitaire = LocalDate.now();
        String nomUniversite = "ESPRIT";

        Chambre chambre = Chambre.builder().typeChambre(TypeChambre.DOUBLE).build(); // Utilisation de l'énumération
        chambreRepository.save(chambre);

        Etudiant etudiant = Etudiant.builder().nomEtudiant("Durand").prenomEtudiant("Paul").build();
        etudiantRepository.save(etudiant);

        Reservation reservation = Reservation.builder()
                .idReservation("1-" + chambre.getTypeChambre() + "-" + LocalDate.now().getYear())
                .anneeUniversitaire(anneeUniversitaire)
                .estValide(true)
                .etudiants(new HashSet<>(Set.of(etudiant)))
                .build();
        reservationRepository.save(reservation);

        // Act
        List<Reservation> result = reservationService.getReservationParAnneeUniversitaireEtNomUniversite(anneeUniversitaire, nomUniversite);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
