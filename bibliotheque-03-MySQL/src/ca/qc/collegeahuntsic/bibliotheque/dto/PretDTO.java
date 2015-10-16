// Fichier LivreDTO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-10-16

package ca.qc.collegeahuntsic.bibliotheque.dto;

import java.sql.Timestamp;

/**
 * DTO de la table <code>pret</code>.
 *
 * @author Dragons Vicieux
 */
public class PretDTO extends DTO {

    private static final long serialVersionUID = 1L;

    private int idPret;

    private MembreDTO membreDTO;

    private LivreDTO livreDTO;

    private Timestamp datePret;

    /**
     * Getter de la variable d'instance <code>this.idPret</code>.
     *
     * @return La variable d'instance <code>this.idPret</code>
     */
    public int getIdPret() {
        return this.idPret;
    }

    /**
     * Setter de la variable d'instance <code>this.idPret</code>.
     *
     * @param idPret La valeur à utiliser pour la variable d'instance <code>this.idPret</code>
     */
    public void setIdPret(int idPret) {
        this.idPret = idPret;
    }

    /**
     * Getter de la variable d'instance <code>this.membreDTO</code>.
     *
     * @return La variable d'instance <code>this.membreDTO</code>
     */
    public MembreDTO getMembreDTO() {
        return this.membreDTO;
    }

    /**
     * Setter de la variable d'instance <code>this.membreDTO</code>.
     *
     * @param membreDTO La valeur à utiliser pour la variable d'instance <code>this.membreDTO</code>
     */
    public void setMembreDTO(MembreDTO membreDTO) {
        this.membreDTO = membreDTO;
    }

    /**
     * Getter de la variable d'instance <code>this.livreDTO</code>.
     *
     * @return La variable d'instance <code>this.livreDTO</code>
     */
    public LivreDTO getLivreDTO() {
        return this.livreDTO;
    }

    /**
     * Setter de la variable d'instance <code>this.livreDTO</code>.
     *
     * @param livreDTO La valeur à utiliser pour la variable d'instance <code>this.livreDTO</code>
     */
    public void setLivreDTO(LivreDTO livreDTO) {
        this.livreDTO = livreDTO;
    }

    /**
     * Getter de la variable d'instance <code>this.datePret</code>.
     *
     * @return La variable d'instance <code>this.datePret</code>
     */
    public Timestamp getDatePret() {
        return this.datePret;
    }

    /**
     * Setter de la variable d'instance <code>this.datePret</code>.
     *
     * @param datePret La valeur à utiliser pour la variable d'instance <code>this.datePret</code>
     */
    public void setDatePret(Timestamp datePret) {
        this.datePret = datePret;
    }

    /**
     * Getter de la variable d'instance <code>this.dateRetour</code>.
     *
     * @return La variable d'instance <code>this.dateRetour</code>
     */
    public Timestamp getDateRetour() {
        return this.dateRetour;
    }

    /**
     * Setter de la variable d'instance <code>this.dateRetour</code>.
     *
     * @param dateRetour La valeur à utiliser pour la variable d'instance <code>this.dateRetour</code>
     */
    public void setDateRetour(Timestamp dateRetour) {
        this.dateRetour = dateRetour;
    }

    private Timestamp dateRetour;

    public PretDTO() {
        super();
    }

}
