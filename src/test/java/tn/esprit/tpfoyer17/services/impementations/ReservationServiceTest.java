package tn.esprit.tpfoyer17.services.impementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer17.entities.Chambre;
import tn.esprit.tpfoyer17.entities.Etudiant;
import tn.esprit.tpfoyer17.entities.Reservation;
import tn.esprit.tpfoyer17.repositories.ChambreRepository;
import tn.esprit.tpfoyer17.repositories.EtudiantRepository;
import tn.esprit.tpfoyer17.repositories.ReservationRepository;
import tn.esprit.tpfoyer17.repositories.UniversiteRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @Mock
    private ChambreRepository chambreRepository;

    @Mock
    private UniversiteRepository universiteRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveReservation() {
        // Arrange
        String reservationId = "1-BlocA-2023";
        Reservation reservation = Reservation.builder().idReservation(reservationId).estValide(true).build();
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        // Act
        Reservation foundReservation = reservationService.retrieveReservation(reservationId);

        // Assert
        assertNotNull(foundReservation);
        assertEquals(reservationId, foundReservation.getIdReservation());
        verify(reservationRepository, times(1)).findById(reservationId);
    }

    @Test
    void testAnnulerReservation() {
        // Arrange
        long cinEtudiant = 12345678L;
        Etudiant etudiant = Etudiant.builder().cinEtudiant(cinEtudiant).reservations(new HashSet<>()).build();
        Reservation reservation = Reservation.builder().idReservation("1-BlocA-2023").etudiants(new HashSet<>()).estValide(true).build();
        reservation.getEtudiants().add(etudiant);
        etudiant.getReservations().add(reservation);

        when(etudiantRepository.findByCinEtudiant(cinEtudiant)).thenReturn(etudiant);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Act
        Reservation result = reservationService.annulerReservation(cinEtudiant);

        // Assert
        assertNull(result);
        verify(etudiantRepository, times(1)).findByCinEtudiant(cinEtudiant);
        verify(reservationRepository, atLeastOnce()).save(any(Reservation.class));
    }

    @Test
    void testAjouterReservation() {
        // Arrange
        long idChambre = 1L;
        long cinEtudiant = 12345678L;
        Etudiant etudiant = Etudiant.builder().cinEtudiant(cinEtudiant).build();
        Chambre chambre = Chambre.builder().idChambre(idChambre).numeroChambre(101L).reservations(new HashSet<>()).build();

        when(etudiantRepository.findByCinEtudiant(cinEtudiant)).thenReturn(etudiant);
        when(chambreRepository.findById(idChambre)).thenReturn(Optional.of(chambre));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Reservation newReservation = reservationService.ajouterReservation(idChambre, cinEtudiant);

        // Assert
        assertNotNull(newReservation);
        assertTrue(newReservation.getEtudiants().contains(etudiant));
        assertTrue(chambre.getReservations().contains(newReservation));
        verify(chambreRepository, times(1)).findById(idChambre);
        verify(reservationRepository, atLeastOnce()).save(any(Reservation.class));
    }
}
