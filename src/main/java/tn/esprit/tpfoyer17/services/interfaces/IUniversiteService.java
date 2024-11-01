package tn.esprit.tpfoyer17.services.interfaces;

import tn.esprit.tpfoyer17.entities.Universite;

import java.util.List;

public interface IUniversiteService {
    List<Universite> retrieveAllUniversities();
    Universite addUniversity(Universite u);
    Universite updateUniversity(long idUniversite, Universite u); // Mise à jour de la signature
    Universite retrieveUniversity(long idUniversity);
    Universite desaffecterFoyerAUniversite(long idUniversite);
    Universite affecterFoyerAUniversite(long idFoyer, String nomUniversite);
}
