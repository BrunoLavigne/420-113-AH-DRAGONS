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

    private MembreDTO membreDTO;
    
    private LivreDTO livreDTO;

    private Timestamp dateReservation;
    
    public ReservationDTO() {
        
    }

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

    private MembreDTO getMembreDTO() {
        return this.membreDTO;
    }

    private void setMembreDTO(MembreDTO membreDTO) {
        this.membreDTO = membreDTO;
    }

    private LivreDTO getLivreDTO() {
        return this.livreDTO;
    }

    private void setLivreDTO(LivreDTO livreDTO) {
        this.livreDTO = livreDTO;
    }
}
