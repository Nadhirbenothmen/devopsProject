package tn.esprit.tpfoyer17.services.impementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.tpfoyer17.entities.Universite;
import tn.esprit.tpfoyer17.repositories.UniversiteRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
//test test
@ExtendWith(SpringExtension.class)
class UniversiteServiceTest {

    @Mock
    UniversiteRepository universiteRepository;

    @InjectMocks
    UniversiteService universiteService;

    @BeforeEach
    public void setup() {
        reset(universiteRepository);
    }

    @Test
    void testGetUniversitiesList() {
        Universite universite1 = Universite.builder().idUniversite(9).nomUniversite("ben").build();
        Universite universite2 = Universite.builder().idUniversite(8).nomUniversite("kevin").build();
        when(universiteRepository.findAll()).thenReturn(Arrays.asList(universite1, universite2));

        List<Universite> universiteList = universiteService.retrieveAllUniversities();

        assertEquals(2, universiteList.size());
        assertEquals("ben", universiteList.get(0).getNomUniversite());
        assertEquals("kevin", universiteList.get(1).getNomUniversite());
    }

    @Test
    void testGetUniversityById() {
        Universite universite = Universite.builder().idUniversite(10).nomUniversite("george").build();
        when(universiteRepository.findById(10L)).thenReturn(Optional.of(universite));

        Universite universiteById = universiteService.retrieveUniversity(10);

        assertNotNull(universiteById);
        assertEquals("george", universiteById.getNomUniversite());
    }

    @Test
    void testGetInvalidUniversityById() {
        when(universiteRepository.findById(17L)).thenReturn(Optional.empty());

        Universite result = universiteService.retrieveUniversity(17);

        assertNull(result);
    }

    @Test
    void testCreateUniversity() {
        Universite universite = Universite.builder().nomUniversite("john").build();
        when(universiteRepository.save(any(Universite.class))).thenAnswer(invocation -> {
            Universite savedUniversite = invocation.getArgument(0);
            return savedUniversite;
        });

        Universite savedUniversite = universiteService.addUniversity(universite);

        assertEquals("john", savedUniversite.getNomUniversite());
        verify(universiteRepository, times(1)).save(universite);
    }

    @Test
    void testUpdateUniversity() {
        Universite existingUniversite = Universite.builder().idUniversite(15).nomUniversite("oldName").build();
        Universite updatedUniversite = Universite.builder().idUniversite(15).nomUniversite("newName").build();

        when(universiteRepository.findById(15L)).thenReturn(Optional.of(existingUniversite));
        when(universiteRepository.save(any(Universite.class))).thenReturn(updatedUniversite);

        Universite result = universiteService.updateUniversity(15L, updatedUniversite);

        assertNotNull(result);
        assertEquals("newName", result.getNomUniversite());
        verify(universiteRepository, times(1)).save(updatedUniversite);
    }

    @Test
    void testDeleteUniversity() {
        doNothing().when(universiteRepository).deleteById(10L);

        universiteService.deleteUniversity(10L);

        verify(universiteRepository, times(1)).deleteById(10L);
    }

    @Test
    void testDeleteNonExistentUniversity() {
        doThrow(new RuntimeException("University not found")).when(universiteRepository).deleteById(99L);

        assertThrows(RuntimeException.class, () -> universiteService.deleteUniversity(99L));
        verify(universiteRepository, times(1)).deleteById(99L);
    }

    @Test
    void testCreateUniversityWithNullName() {
        Universite universite = Universite.builder().nomUniversite(null).build();

        assertThrows(IllegalArgumentException.class, () -> universiteService.addUniversity(universite));
    }
}
