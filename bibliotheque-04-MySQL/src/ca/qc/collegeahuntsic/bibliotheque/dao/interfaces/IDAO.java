// Fichier IDAO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-10-28

package ca.qc.collegeahuntsic.bibliotheque.dao.interfaces;

import java.io.Serializable;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.DTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOClassException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;

/**
 * Interface de base pour les DAOs.
 * Toutes les interfaces de DAO devraient en hériter.
 *
 * @author Dragons Vicieux
 */
public interface IDAO {

    /**
     * Ajoute un nouveau DTO dans la base de données
     *
     * @param connexion - La connexion à utiliser
     * @param dto - Le DTO à ajouter
     * @throws InvalidHibernateSessionException - Si la connexion est <code>null</code>
     * @throws InvalidDTOException - Si le DTO est <code>null</code>
     * @throws InvalidDTOClassException - Si la classe du DTO n'est pas celle que prend en charge le DAO
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    void add(Connexion connexion,
        DTO dto) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        DAOException;

    /**
     *Lit un DTO à partir de la base de données.
     *
     * @param connexion - La connexion à utiliser
     * @param primaryKey - La clef primaire du DTO à lire
     * @return Le livre
     * @throws InvalidHibernateSessionException - Si la connexion est <code>null</code>
     * @throws InvalidPrimaryKeyException - Si la clef primaire du DTO est <code>null</code>
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    DTO get(Connexion connexion,
        Serializable primaryKey) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        DAOException;

    /**
     * Met à jour un DTO dans la base de données.
     *
     * @param connexion - La connexion à utiliser
     * @param dto - Le DTO à mettre à jour
     * @throws InvalidHibernateSessionException - Si la connexion est <code>null</code>
     * @throws InvalidDTOException - Si le DTO est <code>null</code>
     * @throws InvalidDTOClassException - Si la classe du DTO n'est pas celle que prend en charge le DAO
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    void update(Connexion connexion,
        DTO dto) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        InvalidDTOClassException,
        DAOException;

    /* void save(Connexion connexion, DTO dto) throws InvalidHibernateSessionException,
         InvalidDTOException,
             InvalidDTOClassException,
             DAOException;
     */

    /**
     * Supprime un DTO de la base de données.
     *
     * @param connexion - La connexion à utiliser
     * @param dto - Le DTO à supprimer
     * @throws InvalidHibernateSessionException - Si la connexion est <code>null</code>
     * @throws InvalidDTOException - Si le DTO est <code>null</code>
     * @throws InvalidDTOClassException - Si la classe du DTO n'est pas celle que prend en charge le DAO
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    void delete(Connexion connexion,
        DTO dto) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        DAOException;

    /**
     * Trouve tous les DTOs de la base de données. La liste est classée par ordre croissant sur <code>sortByPropertyName</code>. Si aucun DTO n'est trouvé, une {@link List} vide est retournée.
     *
     * @param connexion - La connexion à utiliser
     * @param sortByPropertyName - Le nom de la propriété à utiliser pour classer
     * @return La liste de tous les DTOs ; une liste vide sinon
     * @throws InvalidHibernateSessionException - Si la connexion est <code>null</code>
     * @throws InvalidSortByPropertyException - Si la propriété à utiliser pour classer est <code>null</code>
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    List<? extends DTO> getAll(Connexion connexion,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidSortByPropertyException,
        DAOException;

}
