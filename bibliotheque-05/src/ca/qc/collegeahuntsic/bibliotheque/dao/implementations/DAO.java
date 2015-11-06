// Fichier DAO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.dao.implementations;

import ca.qc.collegeahuntsic.bibliotheque.dto.DTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOClassException;

/**
 * Classe de base pour tous les DAOs.<br />
 * Tous les DAOs devraient en hériter.
 *
 * @author Dragons Vicieux
 */
public class DAO {
    private Class<? extends DTO> dtoClass;

    /**
     *
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion La connexion à utiliser
     * @throws InvalidDTOClassException Si la classe de DTO est <code>null</code>
     */
    protected DAO(Class<? extends DTO> dtoClass) throws InvalidDTOClassException {
        super();
        if(dtoClass == null) {
            throw new InvalidDTOClassException("La classe de DTO ne peut être null");
        }
        setDtoClass(dtoClass);
    }

    /**
     * Getter de la variable d'instance <code>this.dtoClass</code>.
     *
     * @return La variable d'instance <code>this.dtoClass</code>
     */
    protected Class<? extends DTO> getDtoClass() {
        return this.dtoClass;
    }

    /**
     * Setter de la variable d'instance <code>this.dtoClass</code>.
     *
     * @param dtoClass La valeur à utiliser pour la variable d'instance <code>this.dtoClass</code>
     */
    private void setDtoClass(Class<? extends DTO> dtoClass) {
        this.dtoClass = dtoClass;
    }
}
