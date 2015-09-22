// Fichier MembreDTO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.dto;

/**
 * Permet de représenter un tuple de la table membre.
 *
 */

public class MembreDTO extends DTO {

    private static final long serialVersionUID = 1L;

    public int idMembre;

    public String nom;

    public long telephone;

    public int limitePret;

    public int nbPret;
}
