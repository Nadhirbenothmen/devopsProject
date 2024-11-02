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
import tn.esprit.tpfoyer17.repositories.UniversiteRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
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
    void testRetrieveAllReservation() {
        Reservation reservation1 = new Reservation();
        Reservation reservation2 = new Reservation();
        when(reservationRepository.findAll()).thenReturn(List.of(reservation1, reservation2));

        List<Reservation> reservations = reservationService.retrieveAllReservation();

        assertNotNull(reservations);
        assertEquals(2, reservations.size());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testUpdateReservation() {
        Reservation reservation = new Reservation();
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation updatedReservation = reservationService.updateReservation(reservation);

        assertNotNull(updatedReservation);
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testRetrieveReservation() {
        String reservationId = "res1";
        Reservation reservation = new Reservation();
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        Reservation retrievedReservation = reservationService.retrieveReservation(reservationId);

        assertNotNull(retrievedReservation);
        verify(reservationRepository, times(1)).findById(reservationId);
    }

    @Test
    void testRetrieveReservation_NotFound() {
        String reservationId = "nonexistent";
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        Reservation retrievedReservation = reservationService.retrieveReservation(reservationId);

        assertNull(retrievedReservation);
        verify(reservationRepository, times(1)).findById(reservationId);
    }

    @Test
    void testAnnulerReservation() {
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

        Reservation result = reservationService.annulerReservation(cinEtudiant);

        assertNotNull(result);
        assertFalse(result.isEstValide());
        verify(reservationRepository, times(1)).save(reservation);
    }
    @Test
    void testAnnulerReservation_ReservationNotFound() {
        long cinEtudiant = 12345L;
        when(etudiantRepository.findByCinEtudiant(cinEtudiant)).thenReturn(null);

        Reservation result = reservationService.annulerReservation(cinEtudiant);

        assertNull(result);
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void testAjouterReservation() {
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

        Reservation addedReservation = reservationService.ajouterReservation(idChambre, cinEtudiant);

        assertNotNull(addedReservation);
        assertFalse(addedReservation.isEstValide());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testGetReservationParAnneeUniversitaireEtNomUniversite() {
        LocalDate anneeUniversite = LocalDate.of(2024, 1, 1);
        String nomUniversite = "Université Exemple";
        List<Reservation> reservations = List.of(new Reservation(), new Reservation());
        when(reservationRepository.recupererParBlocEtTypeChambre(nomUniversite, anneeUniversite)).thenReturn(reservations);

        List<Reservation> result = reservationService.getReservationParAnneeUniversitaireEtNomUniversite(anneeUniversite, nomUniversite);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(reservationRepository, times(1)).recupererParBlocEtTypeChambre(nomUniversite, anneeUniversite);
    }

    @Test
    void testGetReservationParAnneeUniversitaireEtNomUniversiteKeyWord() {
        LocalDate anneeUniversite = LocalDate.of(2024, 1, 1);
        String nomUniversite = "Université Exemple";
        List<Reservation> reservations = List.of(new Reservation(), new Reservation());
        when(universiteRepository.findByFoyerBlocsChambresReservationsAnneeUniversitaireAndNomUniversite(anneeUniversite, nomUniversite)).thenReturn(reservations);

        List<Reservation> result = reservationService.getReservationParAnneeUniversitaireEtNomUniversiteKeyWord(anneeUniversite, nomUniversite);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(universiteRepository, times(1)).findByFoyerBlocsChambresReservationsAnneeUniversitaireAndNomUniversite(anneeUniversite, nomUniversite);
    }
}
