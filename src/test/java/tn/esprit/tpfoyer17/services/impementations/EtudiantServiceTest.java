package tn.esprit.tpfoyer17.services.impementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpfoyer17.entities.Etudiant;
import tn.esprit.tpfoyer17.repositories.EtudiantRepository;
import tn.esprit.tpfoyer17.services.impementations.EtudiantService;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class EtudiantServiceTest {

    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @BeforeEach
    void setUp() {
        etudiantRepository.deleteAll();  // Nettoyage de la base pour chaque test
    }

    @Test
    void testRetrieveAllEtudiants() {
        // Arrange
        Etudiant etudiant1 = Etudiant.builder().nomEtudiant("Dupont").prenomEtudiant("Jean").build();
        Etudiant etudiant2 = Etudiant.builder().nomEtudiant("Durand").prenomEtudiant("Paul").build();
        etudiantRepository.save(etudiant1);
        etudiantRepository.save(etudiant2);

        // Act
        List<Etudiant> result = etudiantService.retrieveAllEtudiants();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testAddEtudiants() {
        // Arrange
        Etudiant etudiant1 = Etudiant.builder().nomEtudiant("Dupont").prenomEtudiant("Jean").build();
        Etudiant etudiant2 = Etudiant.builder().nomEtudiant("Durand").prenomEtudiant("Paul").build();
        List<Etudiant> etudiants = List.of(etudiant1, etudiant2);

        // Act
        List<Etudiant> savedEtudiants = etudiantService.addEtudiants(etudiants);

        // Assert
        assertEquals(2, savedEtudiants.size());
        assertEquals("Dupont", savedEtudiants.get(0).getNomEtudiant());
    }

    @Test
    void testUpdateEtudiant() {
        // Arrange
        Etudiant etudiant = Etudiant.builder().nomEtudiant("Dupont").prenomEtudiant("Jean").build();
        etudiant = etudiantRepository.save(etudiant);
        etudiant.setNomEtudiant("DupontModifié");

        // Act
        Etudiant updatedEtudiant = etudiantService.updateEtudiant(etudiant);

        // Assert
        assertEquals("DupontModifié", updatedEtudiant.getNomEtudiant());
    }

    @Test
    void testRetrieveEtudiant() {
        // Arrange
        Etudiant etudiant = Etudiant.builder().nomEtudiant("Dupont").prenomEtudiant("Jean").build();
        Etudiant savedEtudiant = etudiantRepository.save(etudiant);

        // Act
        Etudiant result = etudiantService.retrieveEtudiant(savedEtudiant.getIdEtudiant());

        // Assert
        assertNotNull(result);
        assertEquals("Dupont", result.getNomEtudiant());
    }

    @Test
    void testRemoveEtudiant() {
        // Arrange
        Etudiant etudiant = Etudiant.builder().nomEtudiant("Dupont").prenomEtudiant("Jean").build();
        etudiant = etudiantRepository.save(etudiant);

        // Act
        etudiantService.removeEtudiant(etudiant.getIdEtudiant());

        // Assert
        assertNull(etudiantService.retrieveEtudiant(etudiant.getIdEtudiant()));
    }

    @Test
    void testFindByReservationsAnneeUniversitaire() {
        // Arrange
        Etudiant etudiant1 = Etudiant.builder().nomEtudiant("Dupont").prenomEtudiant("Jean").build();
        Etudiant etudiant2 = Etudiant.builder().nomEtudiant("Durand").prenomEtudiant("Paul").build();
        etudiantRepository.save(etudiant1);
        etudiantRepository.save(etudiant2);

        // Act
        List<Etudiant> result = etudiantService.findByReservationsAnneeUniversitaire();

        // Assert
        assertNotNull(result);
    }
}
