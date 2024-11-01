package tn.esprit.tpfoyer17.services.impementations;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer17.entities.Foyer;
import tn.esprit.tpfoyer17.entities.Universite;
import tn.esprit.tpfoyer17.repositories.FoyerRepository;
import tn.esprit.tpfoyer17.repositories.UniversiteRepository;
import tn.esprit.tpfoyer17.services.interfaces.IUniversiteService;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UniversiteService implements IUniversiteService {

    UniversiteRepository universiteRepository;
    FoyerRepository foyerRepository;

    @Override
    public List<Universite> retrieveAllUniversities() {
        return (List<Universite>) universiteRepository.findAll();
    }

    @Override
    public Universite addUniversity(Universite u) {
        if (u == null || u.getNomUniversite() == null) {
            throw new IllegalArgumentException("L'université ou le nom de l'université ne peut pas être null");
        }
        return universiteRepository.save(u);
    }

    @Override
    public Universite updateUniversity(long idUniversity, Universite u) {
        Universite existingUniversite = universiteRepository.findById(idUniversity)
                .orElseThrow(() -> new RuntimeException("Université non trouvée pour l'ID : " + idUniversity));
        existingUniversite.setNomUniversite(u.getNomUniversite());  // Mettez à jour les champs nécessaires
        return universiteRepository.save(existingUniversite);
    }


    @Override
    public Universite retrieveUniversity(long idUniversity) {
        return universiteRepository.findById(idUniversity).orElse(null);
    }

    @Override
    public void deleteUniversity(long idUniversity) {
        if (universiteRepository.existsById(idUniversity)) {
            universiteRepository.deleteById(idUniversity);
        } else {
            throw new RuntimeException("Université non trouvée pour l'ID : " + idUniversity);
        }
    }

    @Override
    public Universite desaffecterFoyerAUniversite(long idUniversite) {
        Universite universite = universiteRepository.findById(idUniversite)
                .orElseThrow(() -> new RuntimeException("Université non trouvée pour l'ID : " + idUniversite));
        universite.setFoyer(null);
        return universiteRepository.save(universite);
    }

    @Override
    public Universite affecterFoyerAUniversite(long idFoyer, String nomUniversite) {
        Foyer foyer = foyerRepository.findById(idFoyer)
                .orElseThrow(() -> new RuntimeException("Foyer non trouvé pour l'ID : " + idFoyer));
        Universite universite = universiteRepository.findByNomUniversiteLike(nomUniversite);
        if (universite == null) {
            throw new RuntimeException("Université non trouvée pour le nom : " + nomUniversite);
        }
        universite.setFoyer(foyer);
        return universiteRepository.save(universite);
    }
}
