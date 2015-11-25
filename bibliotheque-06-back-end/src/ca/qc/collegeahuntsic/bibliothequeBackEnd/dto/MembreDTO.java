// Fichier MembreDTO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliothequeBackEnd.dto;

import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * DTO de la table <code>membre</code>.
 *
 * @author Dragons Vicieux
 *
 */

public class MembreDTO extends DTO {

    private static final long serialVersionUID = 1L;

    public static String ID_MEMBRE_COLUMN_NAME = "idMembre";

    public static String NOM_COLUMN_NAME = "nom";

    public static String TELEPHONE_COLUMN_NAME = "telephone";

    public static String LIMITE_PRET_COLUMN_NAME = "limitePret";

    public static String NB_PRET_COLUMN_NAME = "nbPret";

    private String idMembre;

    private String nom;

    private long telephone;

    private int limitePret;

    private int nbPret;

    private Set<PretDTO> prets;

    private Set<ReservationDTO> reservations;

    // Region Getters and Setters
    /**
     * Getter de la variable d'instance <code>this.idMembre</code>.
     *
     * @return La variable d'instance <code>this.idMembre</code>
     */
    public String getIdMembre() {
        return this.idMembre;
    }

    /**
     * Setter de la variable d'instance <code>this.idMembre</code>.
     *
     * @param idMembre La valeur à utiliser pour la variable d'instance <code>this.idMembre</code>
     */
    public void setIdMembre(String idMembre) {
        this.idMembre = idMembre;
    }

    /**
     * Getter de la variable d'instance <code>this.nom</code>.
     *
     * @return La variable d'instance <code>this.nom</code>
     */
    public String getNom() {
        return this.nom;
    }

    /**
     * Setter de la variable d'instance <code>this.nom</code>.
     *
     * @param nom La valeur à utiliser pour la variable d'instance <code>this.nom</code>
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Getter de la variable d'instance <code>this.telephone</code>.
     *
     * @return La variable d'instance <code>this.telephone</code>
     */
    public long getTelephone() {
        return this.telephone;
    }

    /**
     * Setter de la variable d'instance <code>this.telephone</code>.
     *
     * @param telephone La valeur à utiliser pour la variable d'instance <code>this.telephone</code>
     */
    public void setTelephone(long telephone) {
        this.telephone = telephone;
    }

    /**
     * Getter de la variable d'instance <code>this.limitePret</code>.
     *
     * @return La variable d'instance <code>this.limitePret</code>
     */
    public int getLimitePret() {
        return this.limitePret;
    }

    /**
     * Setter de la variable d'instance <code>this.limitePret</code>.
     *
     * @param limitePret La valeur à utiliser pour la variable d'instance <code>this.limitePret</code>
     */
    public void setLimitePret(int limitePret) {
        this.limitePret = limitePret;
    }

    /**
     * Getter de la variable d'instance <code>this.nbPret</code>.
     *
     * @return La variable d'instance <code>this.nbPret</code>
     */
    public int getNbPret() {
        return this.nbPret;
    }

    /**
     * Setter de la variable d'instance <code> this.nbPret</code>.
     *
     * @param nbPret La valeur à utiliser pour la variable d'instance <code>this.nbPret</code>
     */
    public void setNbPret(int nbPret) {
        this.nbPret = nbPret;
    }

    /**
     * Getter de la variable d'instance <code>this.prets</code>.
     *
     * @return La variable d'instance <code>this.prets</code>
     */
    public Set<PretDTO> getPrets() {
        return this.prets;
    }

    /**
     * Setter de la variable d'instance <code>this.prets</code>.
     *
     * @param prets La valeur à utiliser pour la variable d'instance <code>this.prets</code>
     */
    public void setPrets(Set<PretDTO> prets) {
        this.prets = prets;
    }

    /**
     * Getter de la variable d'instance <code>this.reservations</code>.
     *
     * @return La variable d'instance <code>this.reservations</code>
     */
    public Set<ReservationDTO> getReservations() {
        return this.reservations;
    }

    /**
     * Setter de la variable d'instance <code>this.reservations</code>.
     *
     * @param reservations La valeur à utiliser pour la variable d'instance <code>this.reservations</code>
     */
    public void setReservations(Set<ReservationDTO> reservations) {
        this.reservations = reservations;
    }

    // EndRegion Getters and Setters
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        boolean equals = this == obj;
        if(!equals) {
            equals = obj != null
                && obj instanceof MembreDTO;
            if(equals) {
                final MembreDTO membreDTO = (MembreDTO) obj;
                final EqualsBuilder equalsBuilder = new EqualsBuilder();
                equalsBuilder.appendSuper(super.equals(membreDTO));
                equalsBuilder.append(getIdMembre(),
                    membreDTO.getIdMembre());
                equals = equalsBuilder.isEquals();
            }
        }
        return equals;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(461,
            451);
        hashCodeBuilder.appendSuper(super.hashCode());
        hashCodeBuilder.append(getIdMembre());
        return hashCodeBuilder.toHashCode();
    }
}
