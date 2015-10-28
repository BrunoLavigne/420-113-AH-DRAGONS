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
 * Interface DAO pour manipuler les réservations dans la base de données.
 *
 * @author Dragons Vicieux
 */
public interface IReservationDAO extends IDAO {

    /**
     * @param connexion - La connexion à utiliser
     * @param idLivre - L'ID du livre à trouver
     * @param SortByPropretyName - Le nom de la propriété à utiliser pour classer
     * @return - La liste des réservations correspondantes ; une liste vide sinon
     * @throws InvalidHibernateSessionException - Si la connexion est <code>null</code>
     * @throws InvalidCriterionException - Si l'ID du livre est <code>null</code>
     * @throws InvalidSortByPropertyException - Si la propriété à utiliser pour classer est <code>null</code>
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    List<ReservationDTO> findByLivre(Connexion connexion,
        String idLivre,
        String SortByPropretyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        DAOException;

    /**
     * @param connexion - La connexion à utiliser
     * @param idLivre - L'ID du membre à trouver
     * @param SortByPropretyName - Le nom de la propriété à utiliser pour classer
     * @return - La liste des réservations correspondantes ; une liste vide sinon
     * @throws InvalidHibernateSessionException - Si la connexion est <code>null</code>
     * @throws InvalidCriterionException - Si l'ID du livre est <code>null</code>
     * @throws InvalidSortByPropertyException - Si la propriété à utiliser pour classer est <code>null</code>
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    List<ReservationDTO> findByMembre(Connexion connexion,
        String idMembre,
        String SortByPropretyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        DAOException;

}