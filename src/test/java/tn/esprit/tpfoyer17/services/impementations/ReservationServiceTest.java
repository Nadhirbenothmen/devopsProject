package tn.esprit.tpfoyer17.services.impementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tn.esprit.tpfoyer17.entities.Chambre;
import tn.esprit.tpfoyer17.entities.Etudiant;
import tn.esprit.tpfoyer17.entities.Reservation;
import tn.esprit.tpfoyer17.entities.enumerations.TypeChambre; // Ajouté ici
import tn.esprit.tpfoyer17.repositories.ChambreRepository;
import tn.esprit.tpfoyer17.repositories.EtudiantRepository;
import tn.esprit.tpfoyer17.repositories.ReservationRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ReservationServiceTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private ChambreRepository chambreRepository;

    private ReservationService reservationService;

    @BeforeEach
    public void setUp() {
        reservationService = new ReservationService(reservationRepository, etudiantRepository, chambreRepository, null);
        // Ajouter des données de test pour Etudiant et Chambre ici si nécessaire
    }

    @Test
    public void testRetrieveAllReservation() {
        // Données de test
        Reservation reservation1 = Reservation.builder()
                .idReservation("1")
                .estValide(true)
                .anneeUniversitaire(LocalDate.now())
                .build();
        reservationRepository.save(reservation1);

        List<Reservation> reservations = reservationService.retrieveAllReservation();

        assertFalse(reservations.isEmpty());
        assertEquals(1, reservations.size());
        assertEquals("1", reservations.get(0).getIdReservation());
    }

    @Test
    public void testUpdateReservation() {
        // Créer et sauvegarder une réservation
        Reservation reservation = Reservation.builder()
                .idReservation("1")
                .estValide(true)
                .anneeUniversitaire(LocalDate.now())
                .build();
        reservationRepository.save(reservation);

        // Mettre à jour la réservation
        reservation.setEstValide(false);
        Reservation updatedReservation = reservationService.updateReservation(reservation);

        assertFalse(updatedReservation.isEstValide());
    }

    @Test
    public void testRetrieveReservation() {
        // Créer et sauvegarder une réservation
        Reservation reservation = Reservation.builder()
                .idReservation("1")
                .estValide(true)
                .anneeUniversitaire(LocalDate.now())
                .build();
        reservationRepository.save(reservation);

        // Récupérer la réservation
        Reservation retrievedReservation = reservationService.retrieveReservation("1");

        assertNotNull(retrievedReservation);
        assertEquals("1", retrievedReservation.getIdReservation());
    }

    @Test
    public void testAnnulerReservation() {
        // Créer et sauvegarder un étudiant et une chambre pour la réservation
        Etudiant etudiant = Etudiant.builder()
                .cinEtudiant(123456)
                .nomEtudiant("John")
                .prenomEtudiant("Doe")
                .build();
        etudiantRepository.save(etudiant);

        Chambre chambre = Chambre.builder()
                .numeroChambre(101L)
                .typeChambre(TypeChambre.SIMPLE)
                .reservations(new HashSet<>())
                .build();
        chambreRepository.save(chambre);

        // Créer une réservation
        Reservation reservation = Reservation.builder()
                .idReservation("1")
                .etudiants(new HashSet<>())
                .anneeUniversitaire(LocalDate.now())
                .estValide(true)
                .build();
        reservation.getEtudiants().add(etudiant);
        chambre.getReservations().add(reservation);
        reservationRepository.save(reservation);

        // Annuler la réservation
        reservationService.annulerReservation(etudiant.getCinEtudiant());

        // Vérifier que la réservation est annulée
        Reservation canceledReservation = reservationService.retrieveReservation("1");
        assertFalse(canceledReservation.isEstValide());
    }

    @Test
    public void testAjouterReservation() {
        // Créer et sauvegarder un étudiant
        Etudiant etudiant = Etudiant.builder()
                .cinEtudiant(123456)
                .nomEtudiant("Jane")
                .prenomEtudiant("Doe")
                .build();
        etudiantRepository.save(etudiant);

        // Créer et sauvegarder une chambre
        Chambre chambre = Chambre.builder()
                .numeroChambre(102L)
                .typeChambre(TypeChambre.DOUBLE)
                .reservations(new HashSet<>())
                .build();
        chambreRepository.save(chambre);

        // Ajouter une réservation
        Reservation addedReservation = reservationService.ajouterReservation(chambre.getIdChambre(), etudiant.getCinEtudiant());

        assertNotNull(addedReservation);
        assertEquals("102-BLOC", addedReservation.getIdReservation()); // Adaptez le format selon votre logique d'ID
    }
}
