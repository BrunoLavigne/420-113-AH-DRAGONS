// Fichier LivreDTO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.dto;

import java.sql.Date;

/**
 * Permet de représenter un tuple de la table livre.
 *
 */

public class LivreDTO extends DTO {

    private static final long serialVersionUID = 1L;

    public int idLivre;

    public String titre;

    public String auteur;

    public Date dateAcquisition;

    public int idMembre;

    public Date datePret;
}
