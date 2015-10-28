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
 *
 * TODO Auto-generated class javadoc
 *
 * @author Gilles Bénichou
 */
public interface IDAO {

    /**
     *
     * ajoute un nouveau DTO dans la base de donnée
     *
     * @param connexion
     * @param dto
     * @throws InvalidHibernateSessionException
     * @throws InvalidDTOException
     * @throws InvalidDTOClassException
     * @throws DAOException
     */
    void add(Connexion connexion,
        DTO dto) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        DAOException;

    /**
     *
     * TODO Auto-generated method javadoc
     *
     * @param connexion
     * @param primaryKey
     * @return
     * @throws InvalidHibernateSessionException
     * @throws InvalidPrimaryKeyException
     * @throws DAOException
     */
    DTO get(Connexion connexion,
        Serializable primaryKey) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        DAOException;

    /**
     *
     * TODO Auto-generated method javadoc
     *
     * @param connexion
     * @param dto
     * @throws InvalidHibernateSessionException
     * @throws InvalidDTOException
     * @throws InvalidDTOClassException
     * @throws InvalidDTOClassException
     * @throws DAOException
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
     *
     * TODO Auto-generated method javadoc
     *
     * @param connexion
     * @param dto
     * @throws InvalidHibernateSessionException
     * @throws InvalidDTOException
     * @throws InvalidDTOClassException
     * @throws DAOException
     */
    void delete(Connexion connexion,
        DTO dto) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        DAOException;

    /**
     *
     * Auto-generated method javadoc
     *
     * @param connexion
     * @param sortByPropertyName
     * @return
     * @throws InvalidHibernateSessionException
     * @throws InvalidSortByPropertyException
     * @throws DAOException
     */
    List<? extends DTO> getAll(Connexion connexion,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidSortByPropertyException,
        DAOException;

}
