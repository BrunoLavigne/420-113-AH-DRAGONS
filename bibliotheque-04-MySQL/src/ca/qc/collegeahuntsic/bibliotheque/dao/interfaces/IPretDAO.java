// Fichier IDAO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-10-28

package ca.qc.collegeahuntsic.bibliotheque.dao.interfaces;

import java.sql.Timestamp;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
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
public interface IPretDAO extends IDAO {

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
    List<PretDTO> findByMembre(Connexion connexion,
        String idMembre,
        String SortByPropretyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        DAOException;

    /**
     *
     * @param connexion
     * @param idLivre
     * @param SortByPropretyName
     * @return
     * @throws InvalidHibernateSessionException
     * @throws InvalidCriterionException
     * @throws InvalidSortByPropertyException
     * @throws DAOException
     */
    List<PretDTO> findByLivre(Connexion connexion,
        String idLivre,
        String SortByPropretyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        DAOException;

    /**
     *
     * @param connexion
     * @param datePret
     * @param SortByPropretyName
     * @return
     * @throws InvalidHibernateSessionException
     * @throws InvalidCriterionException
     * @throws InvalidSortByPropertyException
     * @throws DAOException
     */
    List<PretDTO> findByDatePret(Connexion connexion,
        Timestamp datePret,
        String SortByPropretyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        DAOException;

    /**
     *
     * @param connexion
     * @param dateRetour
     * @param SortByPropretyName
     * @return
     * @throws InvalidHibernateSessionException
     * @throws InvalidCriterionException
     * @throws InvalidSortByPropertyException
     * @throws DAOException
     */
    List<PretDTO> findByDateRetour(Connexion connexion,
        Timestamp dateRetour,
        String SortByPropretyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        DAOException;

}