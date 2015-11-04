// Fichier LivreDAO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.dao.implementations;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.ILivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.DTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOClassException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;

/**
 *
 * DAO pour effectuer des CRUDs avec la table <code>livre</code>
 *
 * @author Dragons Vicieux
 */
public class LivreDAO extends DAO implements ILivreDAO {

    private final static String READ_REQUEST = "SELECT idlivre, titre, auteur, dateAcquisition "
        + "FROM livre "
        + "WHERE idlivre = ?";

    private final static String ADD_REQUEST = "INSERT into livre (idLivre, titre, auteur, dateAcquisition) "
        + "VALUES (SEQ_ID_LIVRE.NEXTVAL, ?,?,?)";

    private final static String UPDATE_REQUEST = "UPDATE livre set titre = ?, auteur = ?, dateAcquisition = ? "
        + "WHERE idLivre = ?";

    private final static String DELETE_REQUEST = "DELETE from livre "
        + "WHERE idlivre = ?";

    private final static String GET_ALL_REQUEST = "SELECT idLivre, titre, auteur "
        + "FROM livre";

    private final static String FIND_BY_TITRE = "SELECT idLivre, titre, auteur "
        + "FROM LIVRE "
        + "WHERE LOWER(titre) LIKE LOWER(?)";

    /**
     *
     * Crée le DAO de la table <code>livre</code>
     *
     * @param livreDTOClass - La classe de livre DTO à utiliser
     * @throws InvalidDTOClassException - Si la classe de DTO est <code>null</code>
     */
    public LivreDAO(Class<LivreDTO> livreDTOClass) throws InvalidDTOClassException { // TODO changer la visibilité a package quand nous aurons la version avec Spring
        super(livreDTOClass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(Connexion connexion,
        DTO dto) throws DAOException,
        InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(dto == null) {
            throw new InvalidDTOException("Le DTO ne peut être null");
        }
        if(!dto.getClass().equals(getDtoClass())) {
            throw new InvalidDTOClassException("Le DTO doit être de la classe "
                + getDtoClass().getName());
        }
        LivreDTO livreDTO = (LivreDTO) dto;
        try(
            PreparedStatement stmtAdd = (connexion.getConnection().prepareStatement(LivreDAO.ADD_REQUEST))) {

            stmtAdd.setString(1,
                livreDTO.getTitre());
            stmtAdd.setString(2,
                livreDTO.getAuteur());
            stmtAdd.setTimestamp(3,
                livreDTO.getDateAcquisition());
            stmtAdd.executeUpdate();

        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode())
                + " "
                + sqlException.getMessage(),
                sqlException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LivreDTO get(Connexion connexion,
        Serializable primaryKey) throws DAOException,
        InvalidHibernateSessionException,
        InvalidPrimaryKeyException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException();
        }
        if(primaryKey == null) {
            throw new InvalidPrimaryKeyException();
        }
        String idLivre = (String) primaryKey;
        LivreDTO tempLivre = null;
        try(
            PreparedStatement statementRead = (connexion.getConnection().prepareStatement(LivreDAO.READ_REQUEST))) {
            statementRead.setString(1,
                idLivre);
            try(
                ResultSet rset = statementRead.executeQuery()) {
                if(rset.next()) {
                    tempLivre = new LivreDTO();
                    tempLivre.setIdLivre(rset.getString(1));
                    tempLivre.setTitre(rset.getString(2));
                    tempLivre.setAuteur(rset.getString(3));
                    tempLivre.setDateAcquisition(rset.getTimestamp(4));
                    rset.close();
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode())
                + " "
                + sqlException.getMessage(),
                sqlException);
        }
        return tempLivre;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Connexion connexion,
        DTO dto) throws DAOException,
        InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(dto == null) {
            throw new InvalidDTOException("Le DTO ne peut être null");
        }
        if(!dto.getClass().equals(getDtoClass())) {
            throw new InvalidDTOClassException("Le DTO doit être de la classe "
                + getDtoClass().getName());
        }
        LivreDTO livreDTO = (LivreDTO) dto;
        try(
            PreparedStatement updatePreparedStatement = connexion.getConnection().prepareStatement(LivreDAO.UPDATE_REQUEST)) {

            updatePreparedStatement.setString(1,
                livreDTO.getIdLivre());
            updatePreparedStatement.setString(2,
                livreDTO.getTitre());
            updatePreparedStatement.setString(3,
                livreDTO.getAuteur());
            updatePreparedStatement.setTimestamp(4,
                livreDTO.getDateAcquisition());

            updatePreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode())
                + " "
                + sqlException.getMessage(),
                sqlException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Connexion connexion,
        DTO dto) throws DAOException,
        InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(dto == null) {
            throw new InvalidDTOException("Le DTO ne peut être null");
        }
        if(!dto.getClass().equals(getDtoClass())) {
            throw new InvalidDTOClassException("Le DTO doit être de la classe "
                + getDtoClass().getName());
        }
        LivreDTO livreDTO = (LivreDTO) dto;
        try(
            PreparedStatement stmtDelete = (connexion.getConnection().prepareStatement(LivreDAO.DELETE_REQUEST))) {
            stmtDelete.setString(1,
                livreDTO.getIdLivre());
            stmtDelete.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode())
                + " "
                + sqlException.getMessage(),
                sqlException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LivreDTO> getAll(Connexion connexion,
        String sortByPropertyName) throws DAOException,
        InvalidHibernateSessionException,
        InvalidSortByPropertyException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété ne peut être null");
        }
        List<LivreDTO> livres = Collections.<LivreDTO> emptyList();

        try(
            PreparedStatement statementGetAllLivres = (connexion.getConnection().prepareStatement(LivreDAO.GET_ALL_REQUEST))) {
            try(
                ResultSet resultSet = statementGetAllLivres.executeQuery()) {
                LivreDTO livreDTO = null;
                if(resultSet.next()) {
                    livres = new ArrayList<>();
                    do {
                        livreDTO = new LivreDTO();
                        livreDTO.setIdLivre(resultSet.getString(1));
                        livreDTO.setTitre(resultSet.getString(2));
                        livreDTO.setAuteur(resultSet.getString(3));
                        livreDTO.setDateAcquisition(resultSet.getTimestamp(4));
                        livres.add(livreDTO);
                    } while(resultSet.next());
                }
                return livres;
            } catch(SQLException sqlException) {
                throw new DAOException(Integer.toString(sqlException.getErrorCode())
                    + " "
                    + sqlException.getMessage(),
                    sqlException);
            }
        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode())
                + " "
                + sqlException.getMessage(),
                sqlException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LivreDTO> findByTitre(Connexion connexion,
        String titre,
        String sortByPropertyName) throws DAOException,
        InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(titre == null) {
            throw new InvalidCriterionException("Le titre ne peut être null");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété ne peut être null");
        }
        List<LivreDTO> liste = Collections.<LivreDTO> emptyList();
        try(
            PreparedStatement stmtGetLivresByTitre = (connexion.getConnection().prepareStatement(LivreDAO.FIND_BY_TITRE));) {
            stmtGetLivresByTitre.setString(1,
                "%"
                    + titre
                    + "%");
            try(
                ResultSet rset = stmtGetLivresByTitre.executeQuery()) {
                liste = new ArrayList<>();
                while(rset.next()) {
                    LivreDTO tempLivre = new LivreDTO();
                    tempLivre.setIdLivre(rset.getString(1));
                    tempLivre.setTitre(rset.getString(2));
                    tempLivre.setAuteur(rset.getString(3));
                    tempLivre.setDateAcquisition(rset.getTimestamp(4));
                    liste.add(tempLivre);
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode())
                + " "
                + sqlException.getMessage(),
                sqlException);
        }
        return liste;
    }
}