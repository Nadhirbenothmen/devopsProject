package tn.esprit.tpfoyer17.services.impementations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer17.entities.Bloc;
import tn.esprit.tpfoyer17.entities.Chambre;
import tn.esprit.tpfoyer17.entities.Etudiant;
import tn.esprit.tpfoyer17.entities.Reservation;
import tn.esprit.tpfoyer17.entities.enumerations.TypeChambre;
import tn.esprit.tpfoyer17.repositories.ChambreRepository;
import tn.esprit.tpfoyer17.repositories.EtudiantRepository;
import tn.esprit.tpfoyer17.repositories.ReservationRepository;
import tn.esprit.tpfoyer17.repositories.UniversiteRepository;
import tn.esprit.tpfoyer17.services.impementations.ReservationService;

import java.time.LocalDate;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplMockTest {

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    EtudiantRepository etudiantRepository;

    @Mock
    ChambreRepository chambreRepository;

    @Mock
    UniversiteRepository universiteRepository;

    @InjectMocks
    ReservationService reservationService;

    @Test
    public void testRetrieveAllReservations() {
        Reservation reservation1 = new Reservation("1", LocalDate.now(), true, new HashSet<>());
        Reservation reservation2 = new Reservation("2", LocalDate.now(), false, new HashSet<>());
        List<Reservation> reservations = Arrays.asList(reservation1, reservation2);

        Mockito.when(reservationRepository.findAll()).thenReturn(reservations);

        List<Reservation> result = reservationService.retrieveAllReservation();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("1", result.get(0).getIdReservation());
    }

    @Test
    public void testRetrieveReservation() {
        Reservation reservation = new Reservation("1", LocalDate.now(), true, new HashSet<>());
        Mockito.when(reservationRepository.findById("1")).thenReturn(Optional.of(reservation));

        Reservation result = reservationService.retrieveReservation("1");

        Assertions.assertNotNull(result);
        Assertions.assertEquals("1", result.getIdReservation());
    }

    @Test
    public void testAddReservation() {
        // Arrange
        long cinEtudiant = 123L;
        long idChambre = 1L;

        // Create a student
        Etudiant etudiant = new Etudiant();
        etudiant.setCinEtudiant(cinEtudiant);

        // Create a block and initialize it
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Block A"); // Set a name for the block

        // Create a chambre using constructor
        Chambre chambre = new Chambre(idChambre, 1L, TypeChambre.SIMPLE, bloc, new HashSet<>());

        // Create a reservation
        Reservation reservation = new Reservation("1", LocalDate.now(), true, new HashSet<>(Collections.singleton(etudiant)));

        // Set up mock behavior
        Mockito.when(etudiantRepository.findByCinEtudiant(cinEtudiant)).thenReturn(etudiant);
        Mockito.when(chambreRepository.findById(idChambre)).thenReturn(Optional.of(chambre));
        Mockito.when(reservationRepository.save(Mockito.any(Reservation.class))).thenReturn(reservation);

        // Act
        Reservation result = reservationService.ajouterReservation(idChambre, cinEtudiant);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals("1", result.getIdReservation());
    }



    @Test
    public void testCancelReservation() {
        // Arrange
        long cinEtudiant = 123L;

        // Create a student
        Etudiant etudiant = new Etudiant();
        etudiant.setCinEtudiant(cinEtudiant);

        // Create a reservation and link it to the student
        Reservation reservation = new Reservation("1", LocalDate.now(), true, new HashSet<>(Collections.singleton(etudiant)));
        etudiant.setReservations(new HashSet<>(Collections.singleton(reservation)));

        // Mock behavior for etudiantRepository
        Mockito.when(etudiantRepository.findByCinEtudiant(cinEtudiant)).thenReturn(etudiant);

        // Create a Chambre instance and initialize the typeChambre and reservations set
        Chambre chambre = new Chambre();
        chambre.setTypeChambre(TypeChambre.SIMPLE); // Set the typeChambre to a valid enum value
        chambre.setReservations(new HashSet<>()); // Initialize the reservations set
        Mockito.when(chambreRepository.findByReservationsIdReservation("1")).thenReturn(chambre);

        // Act
        reservationService.annulerReservation(cinEtudiant);

        // Assert
        Mockito.verify(reservationRepository, Mockito.times(1)).save(Mockito.any(Reservation.class));
    }


    @Test
    public void testGetReservationParAnneeUniversitaireEtNomUniversite() {
        Reservation reservation1 = new Reservation("1", LocalDate.of(2022, 9, 1), true, new HashSet<>());
        Reservation reservation2 = new Reservation("2", LocalDate.of(2022, 9, 1), false, new HashSet<>());
        List<Reservation> reservations = Arrays.asList(reservation1, reservation2);

        Mockito.when(reservationRepository.recupererParBlocEtTypeChambre("Esprit", LocalDate.of(2022, 9, 1)))
                .thenReturn(reservations);

        List<Reservation> result = reservationService.getReservationParAnneeUniversitaireEtNomUniversite(
                LocalDate.of(2022, 9, 1), "Esprit");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
    }
}