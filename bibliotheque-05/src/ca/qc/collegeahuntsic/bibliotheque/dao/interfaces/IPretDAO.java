// Fichier IPretDAO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-10-28

package ca.qc.collegeahuntsic.bibliotheque.dao.interfaces;

import java.sql.Timestamp;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionValueException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import org.hibernate.Session;

/**
 * Interface DAO pour manipuler les prêts dans la base de données
 *
 * @author Dragons Vicieux
 */
public interface IPretDAO extends IDAO {

    /**
     * Trouve les prêts non retournés d'un membre. La liste est classée par ordre croissant sur <code>sortByPropertyName</code>. Si aucun prêt n'est trouvé, une {@link List} vide est retournée.
     *
     * @param session La session à utiliser
     * @param idMembre L'ID du membre à trouver
     * @param SortByPropretyName Le nom de la propriété à utiliser pour classer
     * @return La liste des prêts correspondants ; une {@link List} vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidCriterionException Si l'ID du membre est <code>null</code>
     * @throws InvalidCriterionValueException Si la valeur du membre est <code>null</code>
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est <code>null</code>
     * @throws DAOException S'il y a une erreur avec la base de données
     */

    List<PretDTO> findByMembre(Session session,
        String idMembre,
        String SortByPropretyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        DAOException;

    /**
     * Trouve les livres en cours d'emprunt. La liste est classée par ordre croissant sur <code>sortByPropertyName</code>. Si aucun prêt n'est trouvé, une {@link List} vide est retournée.
     *
     * @param session La session à utiliser
     * @param idLivre L'ID du livre à trouver
     * @param SortByPropretyName Le nom de la propriété à utiliser pour classer
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidCriterionException Si l'ID du livre est <code>null</code>
     * @throws InvalidCriterionValueException Si la valeur du livre est <code>null</code>
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est <code>null</code>
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByLivre(Session session,
        String idLivre,
        String SortByPropretyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        DAOException;

    /**
     * Trouve les prêts à partir d'une date de prêt. La liste est classée par ordre croissant sur <code>sortByPropertyName</code>. Si aucun prêt n'est trouvé, une {@link List} vide est retournée.
     *
     * @param session La session à utiliser
     * @param datePret La date de prêt à trouver
     * @param SortByPropretyName Le nom de la propriété à utiliser pour classer
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidCriterionException Si la date de prêt est <code>null</code>
     * @throws InvalidCriterionValueException Si la valeur du prêt est <code>null</code>
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est <code>null</code>
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByDatePret(Session session,
        Timestamp datePret,
        String SortByPropretyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        DAOException;

    /**
     * Trouve les prêts à partir d'une date de retour. La liste est classée par ordre croissant sur <code>sortByPropertyName</code>. Si aucun prêt n'est trouvé, une {@link List} vide est retournée.
     *
     * @param session La session à utiliser
     * @param dateRetour La date de retour à trouver
     * @param SortByPropretyName Le nom de la propriété à utiliser pour classer
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidCriterionException Si la date de retour est <code>null</code>
     * @throws InvalidCriterionValueException Si la valeur de la date est <code>null</code>
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est <code>null</code>
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByDateRetour(Session session,
        Timestamp dateRetour,
        String SortByPropretyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        DAOException;

}