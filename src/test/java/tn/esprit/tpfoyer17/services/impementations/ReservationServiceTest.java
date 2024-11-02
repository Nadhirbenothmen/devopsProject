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
import tn.esprit.tpfoyer17.entities.enumerations.TypeChambre;
import tn.esprit.tpfoyer17.repositories.ChambreRepository;
import tn.esprit.tpfoyer17.repositories.EtudiantRepository;
import tn.esprit.tpfoyer17.repositories.ReservationRepository;
import tn.esprit.tpfoyer17.repositories.UniversiteRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // Utilisation de MockitoExtension
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
        MockitoAnnotations.openMocks(this);  // Initialisation des mocks
    }

    @Test
    void testRetrieveAllReservation() {
        // Arrange
        Reservation reservation1 = new Reservation();
        Reservation reservation2 = new Reservation();
        when(reservationRepository.findAll()).thenReturn(List.of(reservation1, reservation2));

        // Act
        List<Reservation> reservations = reservationService.retrieveAllReservation();

        // Assert
        assertNotNull(reservations);
        assertEquals(2, reservations.size());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testUpdateReservation() {
        // Arrange
        Reservation reservation = new Reservation();
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Act
        Reservation updatedReservation = reservationService.updateReservation(reservation);

        // Assert
        assertNotNull(updatedReservation);
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testRetrieveReservation() {
        // Arrange
        String reservationId = "res1";
        Reservation reservation = new Reservation();
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        // Act
        Reservation retrievedReservation = reservationService.retrieveReservation(reservationId);

        // Assert
        assertNotNull(retrievedReservation);
        verify(reservationRepository, times(1)).findById(reservationId);
    }

    @Test
    void testRetrieveReservation_NotFound() {
        // Arrange
        String reservationId = "nonexistent";
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        // Act
        Reservation retrievedReservation = reservationService.retrieveReservation(reservationId);

        // Assert
        assertNull(retrievedReservation);
        verify(reservationRepository, times(1)).findById(reservationId);
    }

    @Test
    void testAnnulerReservation() {
        // Arrange
        long cinEtudiant = 12345L;
        Etudiant etudiant = new Etudiant();
        Reservation reservation = new Reservation();
        reservation.setEstValide(true);
        reservation.setEtudiants(new HashSet<>(Set.of(etudiant)));

        etudiant.setReservations(new HashSet<>(Set.of(reservation)));
        Chambre chambre = new Chambre();
        chambre.setTypeChambre(TypeChambre.SIMPLE);

        when(etudiantRepository.findByCinEtudiant(cinEtudiant)).thenReturn(etudiant);
        when(chambreRepository.findByReservationsIdReservation(reservation.getIdReservation())).thenReturn(chambre);

        // Act
        Reservation result = reservationService.annulerReservation(cinEtudiant);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEstValide());
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testAnnulerReservation_ReservationNotFound() {
        // Arrange
        long cinEtudiant = 12345L;
        when(etudiantRepository.findByCinEtudiant(cinEtudiant)).thenReturn(null);

        // Act
        Reservation result = reservationService.annulerReservation(cinEtudiant);

        // Assert
        assertNull(result);
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void testAjouterReservation() {
        // Arrange
        long idChambre = 1L;
        long cinEtudiant = 12345L;
        Chambre chambre = new Chambre();
        chambre.setTypeChambre(TypeChambre.DOUBLE);
        chambre.setNumeroChambre(101);
        chambre.setReservations(new HashSet<>());

        Etudiant etudiant = new Etudiant();
        when(etudiantRepository.findByCinEtudiant(cinEtudiant)).thenReturn(etudiant);
        when(chambreRepository.findById(idChambre)).thenReturn(Optional.of(chambre));

        Reservation reservation = Reservation.builder()
                .idReservation("101-Bloc1-2024")
                .etudiants(new HashSet<>())
                .estValide(false)
                .build();

        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Act
        Reservation addedReservation = reservationService.ajouterReservation(idChambre, cinEtudiant);

        // Assert
        assertNotNull(addedReservation);
        assertFalse(addedReservation.isEstValide());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testGetReservationParAnneeUniversitaireEtNomUniversite() {
        // Arrange
        LocalDate anneeUniversite = LocalDate.of(2024, 1, 1);
        String nomUniversite = "Université Exemple";
        List<Reservation> reservations = List.of(new Reservation(), new Reservation());
        when(reservationRepository.recupererParBlocEtTypeChambre(nomUniversite, anneeUniversite)).thenReturn(reservations);

        // Act
        List<Reservation> result = reservationService.getReservationParAnneeUniversitaireEtNomUniversite(anneeUniversite, nomUniversite);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(reservationRepository, times(1)).recupererParBlocEtTypeChambre(nomUniversite, anneeUniversite);
    }

    @Test
    void testGetReservationParAnneeUniversitaireEtNomUniversiteKeyWord() {
        // Arrange
        LocalDate anneeUniversite = LocalDate.of(2024, 1, 1);
        String nomUniversite = "Université Exemple";
        List<Reservation> reservations = List.of(new Reservation(), new Reservation());
        when(universiteRepository.findByFoyerBlocsChambresReservationsAnneeUniversitaireAndNomUniversite(anneeUniversite, nomUniversite)).thenReturn(reservations);

        // Act
        List<Reservation> result = reservationService.getReservationParAnneeUniversitaireEtNomUniversiteKeyWord(anneeUniversite, nomUniversite);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(universiteRepository, times(1)).findByFoyerBlocsChambresReservationsAnneeUniversitaireAndNomUniversite(anneeUniversite, nomUniversite);
    }
}
