package tn.esprit.tpfoyer17.services.interfaces;


import tn.esprit.tpfoyer17.entities.Bloc;

import java.util.List;

public interface IBlocService {
    List<Bloc> retrieveBlocs();
    Bloc updateBloc (Bloc bloc);
    Bloc addBloc (Bloc bloc);
    Bloc retrieveBloc (long idBloc);
    void removeBloc (long idBloc);

    Bloc retrieveBloc(Long idBloc);

    void removeBloc(Long idBloc);

    List<Bloc> findByFoyerIdFoyer(long idFoyer);
    Bloc findByChambresIdChambre(Long idChambre);


}
