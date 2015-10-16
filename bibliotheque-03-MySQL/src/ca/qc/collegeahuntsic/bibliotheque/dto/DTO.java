// Fichier DTO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.dto;

import java.io.Serializable;

/**
 * Classe de base pour tous les DTOs.
 * @author Dragons Vicieux
 */
public class DTO extends Object implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *
     * Crée un nouveau DTO vide.
     *
     */
    public DTO() {
        super();
    }

    @Override
    public boolean equals(Object obj) {
        boolean equals = this == obj;
        if(!equals) {
            equals = obj != null
                && obj instanceof DTO;
        }
        return equals;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }

}
