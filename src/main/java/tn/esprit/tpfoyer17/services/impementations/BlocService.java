package tn.esprit.tpfoyer17.services.impementations;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer17.entities.Bloc;
import tn.esprit.tpfoyer17.repositories.BlocRepository;
import tn.esprit.tpfoyer17.services.interfaces.IBlocService;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlocService implements IBlocService {
    BlocRepository blocRepository;

    @Override
    public List<Bloc> retrieveBlocs() {
        return (List<Bloc>) blocRepository.findAll();
    }

    @Override
    public Bloc updateBloc(Bloc bloc) {
        // Vérifier si le Bloc existe avant de le mettre à jour
        if (!blocRepository.existsById(bloc.getIdBloc())) {
            throw new RuntimeException("Bloc not found with ID: " + bloc.getIdBloc());
        }
        return blocRepository.save(bloc);
    }

    @Override
    public Bloc addBloc(Bloc bloc) {
        // Vérifier si l'identifiant est nul
        if (bloc.getIdBloc() != 0) {
            throw new RuntimeException("Bloc ID must be null when adding a new Bloc");
        }
        return blocRepository.save(bloc);
    }

    @Override
    public Bloc retrieveBloc(long idBloc) {
        if (idBloc == 0) {
            throw new RuntimeException("Bloc ID cannot be null");
        }
        return blocRepository.findById(idBloc)
                .orElseThrow(() -> new RuntimeException("Bloc not found with ID: " + idBloc));
    }

    @Override
    public void removeBloc(long idBloc) {
        // Vérifier si le Bloc existe avant de le supprimer
        if (!blocRepository.existsById(idBloc)) {
            throw new RuntimeException("Bloc not found with ID: " + idBloc);
        }
        blocRepository.deleteById(idBloc);
    }

    @Override
    public Bloc retrieveBloc(Long idBloc) {
        if (idBloc == null) {
            throw new RuntimeException("Bloc ID cannot be null");
        }
        return blocRepository.findById(idBloc)
                .orElseThrow(() -> new RuntimeException("Bloc not found with ID: " + idBloc));
    }

    @Override
    public void removeBloc(Long idBloc) {
        // Vérifier si le Bloc existe avant de le supprimer
        if (!blocRepository.existsById(idBloc)) {
            throw new RuntimeException("Bloc not found with ID: " + idBloc);
        }
        blocRepository.deleteById(idBloc);
    }

    @Override
    public List<Bloc> findByFoyerIdFoyer(long idFoyer) {
        return blocRepository.findByFoyerIdFoyer(idFoyer);
    }

    @Override
    public Bloc findByChambresIdChambre(Long idChambre) {
        return blocRepository.findByChambresIdChambre(idChambre);
    }
}
