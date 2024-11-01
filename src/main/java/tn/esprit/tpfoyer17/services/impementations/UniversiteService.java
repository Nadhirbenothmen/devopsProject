package tn.esprit.tpfoyer17.services.impementations;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer17.entities.Universite;
import tn.esprit.tpfoyer17.repositories.UniversiteRepository;
import tn.esprit.tpfoyer17.services.interfaces.IUniversiteService;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UniversiteService implements IUniversiteService {

    private final UniversiteRepository universiteRepository;

    @Override
    public List<Universite> retrieveAllUniversities() {
        // Convertir l'Iterable en List
        List<Universite> universities = new ArrayList<>();
        universiteRepository.findAll().forEach(universities::add);
        return universities;
    }

    @Override
    public Universite addUniversity(Universite u) {
        return universiteRepository.save(u);
    }

    @Override
    public Universite updateUniversity(long idUniversite, Universite u) {
        Universite existingUniversite = universiteRepository.findById(idUniversite)
                .orElseThrow(() -> new RuntimeException("Université non trouvée"));

        existingUniversite.setNomUniversite(u.getNomUniversite());
        // mettez à jour d'autres champs si nécessaire
        return universiteRepository.save(existingUniversite);
    }

    @Override
    public Universite retrieveUniversity(long idUniversity) {
        return universiteRepository.findById(idUniversity)
                .orElseThrow(() -> new RuntimeException("Université non trouvée"));
    }

    @Override
    public Universite desaffecterFoyerAUniversite(long idUniversite) {
        // logique pour désaffecter un foyer
        return null;
    }

    @Override
    public Universite affecterFoyerAUniversite(long idFoyer, String nomUniversite) {
        // logique pour affecter un foyer
        return null;
    }
}
