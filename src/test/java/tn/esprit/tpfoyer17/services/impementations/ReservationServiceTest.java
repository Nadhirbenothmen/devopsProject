package tn.esprit.tpfoyer17.services.impementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer17.entities.Chambre;
import tn.esprit.tpfoyer17.entities.Etudiant;
import tn.esprit.tpfoyer17.entities.Reservation;
import tn.esprit.tpfoyer17.entities.enumerations.TypeChambre;  // Ensure this import is added
import tn.esprit.tpfoyer17.repositories.ChambreRepository;
import tn.esprit.tpfoyer17.repositories.EtudiantRepository;
import tn.esprit.tpfoyer17.repositories.ReservationRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceMockTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @Mock
    private ChambreRepository chambreRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllReservation() {
        // Arrange
        Reservation reservation = new Reservation();
        when(reservationRepository.findAll()).thenReturn(List.of(reservation));

        // Act
        List<Reservation> result = reservationService.retrieveAllReservation();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testUpdateReservation() {
        // Arrange
        Reservation reservation = new Reservation();
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Act
        Reservation result = reservationService.updateReservation(reservation);

        // Assert
        assertNotNull(result);
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testRetrieveReservation() {
        // Arrange
        Reservation reservation = new Reservation();
        when(reservationRepository.findById("1")).thenReturn(Optional.of(reservation));

        // Act
        Reservation result = reservationService.retrieveReservation("1");

        // Assert
        assertNotNull(result);
        verify(reservationRepository, times(1)).findById("1");
    }

    @Test
    void testAnnulerReservation() {
        // Arrange
        Etudiant etudiant = new Etudiant();
        Reservation reservation = new Reservation();
        Set<Reservation> reservations = new HashSet<>();
        reservations.add(reservation);
        etudiant.setReservations(reservations);

        when(etudiantRepository.findByCinEtudiant(anyLong())).thenReturn(etudiant);
        when(chambreRepository.findByReservationsIdReservation(anyString())).thenReturn(new Chambre());

        // Act
        Reservation result = reservationService.annulerReservation(123456789L);

        // Assert
        assertNull(result);  // Based on current method implementation returning null
        verify(etudiantRepository, times(1)).findByCinEtudiant(123456789L);
        verify(reservationRepository, atLeastOnce()).save(any(Reservation.class));
    }

    @Test
    void testAjouterReservation() {
        // Arrange
        Etudiant etudiant = new Etudiant();
        Chambre chambre = new Chambre();
        chambre.setTypeChambre(TypeChambre.SIMPLE);  // Ensure TypeChambre is used correctly here
        when(etudiantRepository.findByCinEtudiant(anyLong())).thenReturn(etudiant);
        when(chambreRepository.findById(anyLong())).thenReturn(Optional.of(chambre));
        when(reservationRepository.findById(anyString())).thenReturn(Optional.empty());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(new Reservation());

        // Act
        Reservation result = reservationService.ajouterReservation(1L, 123456789L);

        // Assert
        assertNotNull(result);
        verify(etudiantRepository, times(1)).findByCinEtudiant(123456789L);
        verify(chambreRepository, times(1)).findById(1L);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testGetReservationParAnneeUniversitaireEtNomUniversite() {
        // Arrange
        List<Reservation> reservations = List.of(new Reservation());
        when(reservationRepository.recupererParBlocEtTypeChambre(anyString(), any(LocalDate.class)))
                .thenReturn(reservations);

        // Act
        List<Reservation> result = reservationService.getReservationParAnneeUniversitaireEtNomUniversite(
                LocalDate.now(), "TestUniversite");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(reservationRepository, times(1))
                .recupererParBlocEtTypeChambre("TestUniversite", LocalDate.now());
    }


}
