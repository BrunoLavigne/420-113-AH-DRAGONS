// Fichier ReservationDTO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.dto;

import java.sql.Timestamp;

/**
 *
 * DTO de la table <code>reservation</code>.
 *
 * @author Dragons Vicieux
 */
public class ReservationDTO extends DTO {

    private static final long serialVersionUID = 1L;

    private int idReservation;

    private int idLivre;

    private int idMembre;

    private Timestamp dateReservation;

    // Region Getters and Setters
    /**
     * Getter de la variable d'instance <code>this.idReservation</code>.
     *
     * @return La variable d'instance <code>this.idReservation</code>
     */
    public int getIdReservation() {
        return this.idReservation;
    }

    /**
     * Setter de la variable d'instance <code>this.idReservation</code>.
     *
     * @param idReservation La valeur à utiliser pour la variable d'instance <code>this.idReservation</code>
     */
    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    /**
     * Getter de la variable d'instance <code>this.idLivre</code>.
     *
     * @return La variable d'instance <code>this.idLivre</code>
     */
    public int getIdLivre() {
        return this.idLivre;
    }

    /**
     * Setter de la variable d'instance <code>this.idLivre</code>.
     *
     * @param idLivre La valeur à utiliser pour la variable d'instance <code>this.idLivre</code>
     */
    public void setIdLivre(int idLivre) {
        this.idLivre = idLivre;
    }

    /**
     * Getter de la variable d'instance <code>this.idMembre</code>.
     *
     * @return La variable d'instance <code>this.idMembre</code>
     */
    public int getIdMembre() {
        return this.idMembre;
    }

    /**
     * Setter de la variable d'instance <code>this.idMembre</code>.
     *
     * @param idMembre La valeur à utiliser pour la variable d'instance <code>this.idMembre</code>
     */
    public void setIdMembre(int idMembre) {
        this.idMembre = idMembre;
    }

    /**
     * Getter de la variable d'instance <code>this.dateReservation</code>.
     *
     * @return La variable d'instance <code>this.dateReservation</code>
     */
    public Timestamp getDateReservation() {
        return this.dateReservation;
    }

    /**
     * Setter de la variable d'instance <code>this.dateReservation</code>.
     *
     * @param dateReservation La valeur à utiliser pour la variable d'instance <code>this.dateReservation</code>
     */
    public void setDateReservation(Timestamp dateReservation) {
        this.dateReservation = dateReservation;
    }
    //EndRegion Getters and Setters
}
