package tn.esprit.tpfoyer17.services.impementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpfoyer17.entities.Bloc;
import tn.esprit.tpfoyer17.repositories.BlocRepository;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BlocServiceTest {

    @InjectMocks
    private BlocService blocService;

    @Mock
    private BlocRepository blocRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void testAddBloc() {
        // Arrange
        Bloc bloc = Bloc.builder().nomBloc("BlocTest").build();
        when(blocRepository.save(any(Bloc.class))).thenReturn(bloc);

        // Act
        Bloc savedBloc = blocService.addBloc(bloc);

        // Assert
        assertNotNull(savedBloc);
        assertEquals("BlocTest", savedBloc.getNomBloc());
        verify(blocRepository, times(1)).save(bloc);
    }

    @Test
    void testDeleteBloc() {
        // Arrange
        Bloc blocToDelete = Bloc.builder().idBloc(1L).nomBloc("BlocToDelete").build();
        when(blocRepository.findById(1L)).thenReturn(Optional.of(blocToDelete));

        // Act
        blocService.removeBloc(1L);

        // Assert
        verify(blocRepository, times(1)).delete(blocToDelete);
    }
}