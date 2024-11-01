package tn.esprit.tpfoyer17.services.impementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpfoyer17.entities.Chambre;
import tn.esprit.tpfoyer17.entities.Etudiant;
import tn.esprit.tpfoyer17.entities.Reservation;
import tn.esprit.tpfoyer17.entities.enumerations.TypeChambre;
import tn.esprit.tpfoyer17.repositories.ChambreRepository;
import tn.esprit.tpfoyer17.repositories.EtudiantRepository;
import tn.esprit.tpfoyer17.repositories.ReservationRepository;
import tn.esprit.tpfoyer17.services.impementations.ReservationService;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ReservationServiceTestMock {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @Mock
    private ChambreRepository chambreRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRetrieveAllReservation() {
        Reservation reservation1 = Reservation.builder()
                .idReservation("1")
                .estValide(true)
                .anneeUniversitaire(LocalDate.now())
                .build();

        when(reservationRepository.findAll()).thenReturn(List.of(reservation1));

        List<Reservation> reservations = reservationService.retrieveAllReservation();

        assertFalse(reservations.isEmpty());
        assertEquals(1, reservations.size());
        assertEquals("1", reservations.get(0).getIdReservation());
    }

    @Test
    public void testUpdateReservation() {
        Reservation reservation = Reservation.builder()
                .idReservation("1")
                .estValide(true)
                .anneeUniversitaire(LocalDate.now())
                .build();

        when(reservationRepository.save(reservation)).thenReturn(reservation);

        reservation.setEstValide(false);
        Reservation updatedReservation = reservationService.updateReservation(reservation);

        assertFalse(updatedReservation.isEstValide());
    }

    @Test
    public void testRetrieveReservation() {
        Reservation reservation = Reservation.builder()
                .idReservation("1")
                .estValide(true)
                .anneeUniversitaire(LocalDate.now())
                .build();

        when(reservationRepository.findById("1")).thenReturn(Optional.of(reservation));

        Reservation retrievedReservation = reservationService.retrieveReservation("1");

        assertNotNull(retrievedReservation);
        assertEquals("1", retrievedReservation.getIdReservation());
    }

    @Test
    public void testAnnulerReservation() {
        Etudiant etudiant = Etudiant.builder()
                .cinEtudiant(123456)
                .nomEtudiant("John")
                .prenomEtudiant("Doe")
                .build();

        when(etudiantRepository.findByCinEtudiant(123456)).thenReturn(etudiant);

        Chambre chambre = Chambre.builder()
                .numeroChambre(101L)
                .typeChambre(TypeChambre.SIMPLE)
                .reservations(new HashSet<>())
                .build();

        when(chambreRepository.findById(101L)).thenReturn(Optional.of(chambre));

        Reservation reservation = Reservation.builder()
                .idReservation("1")
                .etudiants(new HashSet<>())
                .anneeUniversitaire(LocalDate.now())
                .estValide(true)
                .build();
        reservation.getEtudiants().add(etudiant);
        chambre.getReservations().add(reservation);

        when(reservationRepository.findById("1")).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        reservationService.annulerReservation(etudiant.getCinEtudiant());

        Reservation canceledReservation = reservationService.retrieveReservation("1");
        assertFalse(canceledReservation.isEstValide());
    }

    @Test
    public void testAjouterReservation() {
        Etudiant etudiant = Etudiant.builder()
                .cinEtudiant(123456)
                .nomEtudiant("Jane")
                .prenomEtudiant("Doe")
                .build();

        when(etudiantRepository.findByCinEtudiant(123456)).thenReturn(etudiant);

        Chambre chambre = Chambre.builder()
                .idChambre(102L)
                .numeroChambre(102L)
                .typeChambre(TypeChambre.DOUBLE)
                .reservations(new HashSet<>())
                .build();

        when(chambreRepository.findById(102L)).thenReturn(Optional.of(chambre));

        Reservation addedReservation = reservationService.ajouterReservation(chambre.getIdChambre(), etudiant.getCinEtudiant());

        assertNotNull(addedReservation);
        assertTrue(addedReservation.getIdReservation().contains("102")); // Adjust ID format as needed
    }
}
