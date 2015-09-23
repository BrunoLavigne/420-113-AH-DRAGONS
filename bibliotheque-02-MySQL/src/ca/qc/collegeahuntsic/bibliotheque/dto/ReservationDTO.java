// Fichier ReservationDTO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.dto;

import java.sql.Date;

/**
 * Permet de représenter un tuple de la table membre.
 *
 */

public class ReservationDTO extends DTO {

    private static final long serialVersionUID = 1L;

    private int idReservation;

    private int idLivre;

    private int idMembre;

    private Date dateReservation;

    public int getIdReservation() {
        return this.idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public int getIdLivre() {
        return this.idLivre;
    }

    public void setIdLivre(int idLivre) {
        this.idLivre = idLivre;
    }

    public int getIdMembre() {
        return this.idMembre;
    }

    public void setIdMembre(int idMembre) {
        this.idMembre = idMembre;
    }

    public Date getDateReservation() {
        return this.dateReservation;
    }

    public void setDateReservation(Date dateReservation) {
        this.dateReservation = dateReservation;
    }
}
