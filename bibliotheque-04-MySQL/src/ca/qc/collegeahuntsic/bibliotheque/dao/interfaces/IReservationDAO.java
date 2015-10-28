// Fichier IDAO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-10-28

package ca.qc.collegeahuntsic.bibliotheque.dao.interfaces;

import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;

/**
 * Interface de base pour tous les DAOs.<br/>
 * Toutes les interfaces de DAO devraient en hériter
 *
 * @author Dragons Vicieux
 */
public interface IReservationDAO extends IDAO {

    /**
     *
     * @param connexion
     * @param idMembre
     * @param SortByPropretyName
     * @return
     * @throws InvalidHibernateSessionException
     * @throws InvalidCriterionException
     * @throws InvalidSortByPropertyException
     * @throws DAOException
     */
    List<ReservationDTO> findByLivre(Connexion connexion,
        String idMembre,
        String SortByPropretyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        DAOException;

    /**
     *
     * @param connexion
     * @param idMembre
     * @param SortByPropretyName
     * @return
     * @throws InvalidHibernateSessionException
     * @throws InvalidCriterionException
     * @throws InvalidSortByPropertyException
     * @throws DAOException
     */
    List<ReservationDTO> findByMembre(Connexion connexion,
        String idMembre,
        String SortByPropretyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        DAOException;

}