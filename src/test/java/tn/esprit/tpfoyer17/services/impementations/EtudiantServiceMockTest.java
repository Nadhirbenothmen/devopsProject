package tn.esprit.tpfoyer17.services.impementations;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer17.entities.Etudiant;
import tn.esprit.tpfoyer17.repositories.EtudiantRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // Extension Mockito pour une meilleure intégration
class EtudiantServiceMockTest {

    @InjectMocks
    private EtudiantService etudiantService;  // Injection du service avec les dépendances simulées

    @Mock
    private EtudiantRepository etudiantRepository;  // Simuler l'interaction avec le repository

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialisation des mocks
    }

    @Test
    void testRetrieveAllEtudiants() {
        // Arrange
        List<Etudiant> etudiants = new ArrayList<>();
        etudiants.add(new Etudiant());
        when(etudiantRepository.findAll()).thenReturn(etudiants);  // Simulation de la méthode findAll()

        // Act
        List<Etudiant> result = etudiantService.retrieveAllEtudiants();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(etudiantRepository, times(1)).findAll();  // Vérification que la méthode findAll() a bien été appelée
    }

    @Test
    void testAddEtudiants() {
        // Arrange
        List<Etudiant> etudiants = new ArrayList<>();
        etudiants.add(new Etudiant());
        when(etudiantRepository.saveAll(anyList())).thenReturn(etudiants);  // Simulation de la méthode saveAll()

        // Act
        List<Etudiant> result = etudiantService.addEtudiants(etudiants);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(etudiantRepository, times(1)).saveAll(etudiants);  // Vérification que saveAll() a bien été appelée une fois
    }

    @Test
    void testUpdateEtudiant() {
        // Arrange
        Etudiant etudiant = new Etudiant();
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);  // Simulation de la méthode save()

        // Act
        Etudiant result = etudiantService.updateEtudiant(etudiant);

        // Assert
        assertNotNull(result);
        verify(etudiantRepository, times(1)).save(etudiant);  // Vérification que save() a été appelée
    }

    @Test
    void testRetrieveEtudiant() {
        // Arrange
        Etudiant etudiant = new Etudiant();
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));  // Simulation de la méthode findById()

        // Act
        Etudiant result = etudiantService.retrieveEtudiant(1L);

        // Assert
        assertNotNull(result);
        verify(etudiantRepository, times(1)).findById(1L);  // Vérification que findById() a été appelée
    }

    @Test
    void testRemoveEtudiant() {
        // Act
        etudiantService.removeEtudiant(1L);  // Appel de la méthode à tester

        // Assert
        verify(etudiantRepository, times(1)).deleteById(1L);  // Vérification que deleteById() a bien été appelée une fois
    }

    @Test
    void testFindByReservationsAnneeUniversitaire() {
        // Arrange
        List<Etudiant> etudiants = new ArrayList<>();
        when(etudiantRepository.findByReservationsAnneeUniversitaire(any(LocalDate.class))).thenReturn(etudiants);  // Simulation de la méthode findByReservationsAnneeUniversitaire()

        // Act
        List<Etudiant> result = etudiantService.findByReservationsAnneeUniversitaire();

        // Assert
        assertNotNull(result);
        verify(etudiantRepository, times(1)).findByReservationsAnneeUniversitaire(LocalDate.now());  // Vérification que findByReservationsAnneeUniversitaire() a été appelée
    }
}
