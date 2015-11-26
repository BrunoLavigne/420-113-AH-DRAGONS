// Fichier IDAO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-10-28

package ca.qc.collegeahuntsic.bibliothequeBackEnd.dao.interfaces;

import java.util.List;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidCriterionValueException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidSortByPropertyException;
import org.hibernate.Session;

/**
 * Interface DAO pour manipuler les réservations dans la base de données
 *
 * @author Dragons Vicieux
 */
public interface IReservationDAO extends IDAO {

    /**
     * Trouve les réservations d'un livre. La liste est classée par ordre croissant sur <code>sortByPropertyName</code>. Si aucune réservation n'est trouvée, une {@link List} vide est retournée.
     *
     * @param session - La session Hibernate à utiliser
     * @param idLivre - L'ID du livre à trouver
     * @param sortByPropertyName - Le nom de la propriété à utiliser pour classer
     * @return La liste des réservations correspondantes ; une liste vide sinon
     * @throws InvalidHibernateSessionException - Si la connexion est <code>null</code>
     * @throws InvalidCriterionException - Si l'ID du livre est <code>null</code>
     * @throws InvalidCriterionValueException - Si la valeur à trouver est <code>null</code>
     * @throws InvalidSortByPropertyException - Si la propriété à utiliser pour classer est <code>null</code>
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    List<ReservationDTO> findByLivre(Session session,
        String idLivre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        DAOException;

    /**
     * Trouve les réservations d'un membre. La liste est classée par ordre croissant sur <code>sortByPropertyName</code>. Si aucune réservation n'est trouvée, une {@link List} vide est retournée.
     *
     * @param session - La session Hibernate à utiliser
     * @param idMembre - Le membre à utiliser
     * @param sortByPropertyName - Le nom de la propriété à utiliser pour classer
     * @return La liste des réservations correspondantes ; une liste vide sinon
     * @throws InvalidHibernateSessionException - Si la connexion est <code>null</code>
     * @throws InvalidCriterionException - Si l'ID du livre est <code>null</code>
     * @throws InvalidCriterionValueException - Si la valeur à trouver est <code>null</code>
     * @throws InvalidSortByPropertyException - Si la propriété à utiliser pour classer est <code>null</code>
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    List<ReservationDTO> findByMembre(Session session,
        String idMembre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        DAOException;

}