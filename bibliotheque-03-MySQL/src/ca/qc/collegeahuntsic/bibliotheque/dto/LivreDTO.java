// Fichier LivreDTO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.dto;

import java.sql.Timestamp;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * DTO de la table <code>livre</code>.
 *
 * @author Dragons Vicieux
 */

public class LivreDTO extends DTO {

    private static final long serialVersionUID = 1L;

    private int idLivre;

    private String titre;

    private String auteur;

    private Timestamp dateAcquisition;

    public LivreDTO() {
        super();
    }

    @Override
    public boolean equals(Object obj) {
        boolean equals = this == obj;
        if(!equals) {
            equals = obj != null
                && obj instanceof LivreDTO;
            if(equals) {
                LivreDTO livreDTO = (LivreDTO) obj;
                EqualsBuilder equalBuilder = new EqualsBuilder();
                equalBuilder.appendSuper(super.equals(livreDTO));
                //...
                //...
            }
        }
    }

    // Region Getters and Setters
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
     * Getter de la variable d'instance <code>this.titre</code>.
     *
     * @return La variable d'instance <code>this.titre</code>
     */
    public String getTitre() {
        return this.titre;
    }

    /**
     * Setter de la variable d'instance <code>this.titre</code>.
     *
     * @param titre La valeur à utiliser pour la variable d'instance <code>this.titre</code>
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
     * Getter de la variable d'instance <code>this.auteur</code>.
     *
     * @return La variable d'instance <code>this.auteur</code>
     */
    public String getAuteur() {
        return this.auteur;
    }

    /**
     * Setter de la variable d'instance <code>this.auteur</code>.
     *
     * @param auteur La valeur à utiliser pour la variable d'instance <code>this.auteur</code>
     */
    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    /**
     * Getter de la variable d'instance <code>this.dateAcquisition</code>.
     *
     * @return La variable d'instance <code>this.dateAcquisition</code>
     */
    public Timestamp getDateAcquisition() {
        return this.dateAcquisition;
    }

    /**
     * Setter de la variable d'instance <code>this.dateAcquisition</code>.
     *
     * @param dateAcquisition La valeur à utiliser pour la variable d'instance <code>this.dateAcquisition</code>
     */

    public void setDateAcquisition(Timestamp dateAcquisition) {
        this.dateAcquisition = dateAcquisition;
    }

    // EndRegion Getters and Setters
}
