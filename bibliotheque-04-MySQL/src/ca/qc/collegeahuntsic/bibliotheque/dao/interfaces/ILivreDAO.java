// Fichier ILivreDAO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-10-28

package ca.qc.collegeahuntsic.bibliotheque.dao.interfaces;

import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;

/**
 * Interface pour LivreDAO
 *
 * @author Dragons Vicieux
 */
public interface ILivreDAO extends IDAO {

    /**
     *
     * TODO Auto-generated method javadoc
     *
     * @param connexion
     * @param titre
     * @param sortByPropertyName
     * @return La listedes livres correspondants ; une liste de livre vide
     * @throws InvalidHibernateSessionException
     * @throws InvalidCriterionException
     * @throws InvalidSortByPropertyException
     * @throws DAOException
     */
    List<LivreDTO> findByTitre(Connexion connexion,
        String titre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        DAOException;

}
