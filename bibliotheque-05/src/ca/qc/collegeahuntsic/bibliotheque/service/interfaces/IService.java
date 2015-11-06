// Fichier IServiceDAO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-10-28

package ca.qc.collegeahuntsic.bibliotheque.service.interfaces;

/**
 * Interface de base pour les services.
 * Toutes les interfaces de service devraient en hériter.
 *
 * @author Dragons Vicieux
 */
public interface IService {

    /**
     * Ajoute un nouveau dto dans la base de données.
     *
     * @param connexion La connexion à utiliser
     * @param dto Le DTO à ajouter
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidDTOException Si le livre est <code>null</code>
     * @throws InvalidDTOClassException Si la classe du livre n'est pas celle que prend en charge le DAO
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    /*
    void add(Connexion connexion,
        DTO dto) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException;
     */

    /**
     * Lit un DTO à partir de la base de données.
     *
     * @param connexion La connexion à utiliser
     * @param idDto L'ID du DTO à lire
     * @return Le DTO
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidPrimaryKeyException Si la clef primaire du livre est <code>null</code>
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    /*
    DTO get(Connexion connexion,
        String idDto) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        ServiceException;
     */

    /**
     * Met à jour un DTO dans la base de données.
     *
     * @param connexion La connexion à utiliser
     * @param dto Le DTO à mettre à jour
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidDTOException Si le livre est <code>null</code>
     * @throws InvalidDTOClassException Si la classe du livre n'est pas celle que prend en charge le DAO
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    /*
    void update(Connexion connexion,
        DTO dto) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException;
     */

    /**
     * Supprime un DTO de la base de données.
     *
     * @param connexion La connexion à utiliser
     * @param dto Le DTO à supprimer
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidDTOException Si le livre est <code>null</code>
     * @throws InvalidDTOClassException Si la classe du livre n'est pas celle que prend en charge le DAO
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    /*
    void delete(Connexion connexion,
        DTO dto) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException;
     */
}