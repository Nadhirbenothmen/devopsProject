package tn.esprit.tpfoyer17.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reservation implements Serializable {

    @Id
    @Column(length = 255)  // Assurez-vous que la colonne peut contenir un ID unique
    String idReservation;

    LocalDate anneeUniversitaire;
    boolean estValide;

    @ManyToMany(mappedBy = "reservations")
    @JsonIgnore
    Set<Etudiant> etudiants = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "chambre_id")  // Relie la réservation à une chambre spécifique
    @JsonIgnore
    Chambre chambre;
}
