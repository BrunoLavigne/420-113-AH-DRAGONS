// Fichier IPretDAO.java
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
     * Trouve les prêts non retournés d'un membre. La liste est classée par ordre croissant sur <code>sortByPropertyName</code>. Si aucun prêt n'est trouvé, une {@link List} vide est retournée.
     *
     * @param connexion La connexion à utiliser
     * @param idMembre L'ID du membre à trouver
     * @param SortByPropretyName Le nom de la propriété à utiliser pour classer
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidCriterionException Si l'ID du membre est <code>null</code>
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est <code>null</code>
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByMembre(Connexion connexion,
        String idMembre,
        String SortByPropretyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        DAOException;

    /**
     * Trouve les livres en cours d'emprunt. La liste est classée par ordre croissant sur <code>sortByPropertyName</code>. Si aucun prêt n'est trouvé, une {@link List} vide est retournée.
     *
     * @param connexion La connexion à utiliser
     * @param idLivre L'ID du livre à trouver
     * @param SortByPropretyName Le nom de la propriété à utiliser pour classer
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidCriterionException Si l'ID du livre est <code>null</code>
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est <code>null</code>
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByLivre(Connexion connexion,
        String idLivre,
        String SortByPropretyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        DAOException;

    /**
     * Trouve les prêts à partir d'une date de prêt. La liste est classée par ordre croissant sur <code>sortByPropertyName</code>. Si aucun prêt n'est trouvé, une {@link List} vide est retournée.
     *
     * @param connexion La connexion à utiliser
     * @param datePret La date de prêt à trouver
     * @param SortByPropretyName Le nom de la propriété à utiliser pour classer
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidCriterionException Si la date de prêt est <code>null</code>
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est <code>null</code>
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByDatePret(Connexion connexion,
        Timestamp datePret,
        String SortByPropretyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        DAOException;

    /**
     * Trouve les prêts à partir d'une date de retour. La liste est classée par ordre croissant sur <code>sortByPropertyName</code>. Si aucun prêt n'est trouvé, une {@link List} vide est retournée.
     *
     * @param connexion La connexion à utiliser
     * @param dateRetour La date de retour à trouver
     * @param SortByPropretyName Le nom de la propriété à utiliser pour classer
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidCriterionException Si la date de retour est <code>null</code>
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est <code>null</code>
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByDateRetour(Connexion connexion,
        Timestamp dateRetour,
        String SortByPropretyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        DAOException;

}