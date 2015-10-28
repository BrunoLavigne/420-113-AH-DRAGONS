// Fichier MembreDAO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.implementations.DAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IMembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.DTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOClassException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;

/**
 * Permet d'effectuer les accès à la table membre. Cette classe gère tous les
 * accès à la table membre.
 */

public class MembreDAO extends DAO implements IMembreDAO {

    private static final String ADD_REQUEST = "INSERT INTO membre (nom, telephone, limitePret, nbPret) "
        + "VALUES (?, ?, ?, 0)";

    private static final String READ_REQUEST = "SELECT idMembre, nom, telephone, limitePret, nbPret "
        + "FROM membre "
        + "WHERE idMembre = ?";

    private static final String UPDATE_REQUEST = "UPDATE membre SET nom = ?, telephone = ?, limitePret = ?, nbPret = ? "
        + "WHERE idMembre = ?";

    private static final String DELETE_REQUEST = "DELETE "
        + "FROM membre "
        + "WHERE idMembre = ?";

    private final static String GET_ALL_REQUEST = "SELECT idMembre, nom, telephone, limitePret, nbPret "
        + "FROM livre";

    /**
     * Crée le DAO de la table <code>membre</code>.
     *
     * @param livreDTOClass La classe de membre DTO à utiliser
     * @throws InvalidDTOClassException Si la classe de DTO est <code>null</code>
     */
    public MembreDAO(Class<MembreDTO> membreDTOClass) throws InvalidDTOClassException { // TODO changer la visibilité a package quand nous aurons la version avec Spring
        super(membreDTOClass);
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
        MembreDTO membreDTO = (MembreDTO) dto;
        try(
            PreparedStatement addPreparedStatement = (connexion.getConnection().prepareStatement(MembreDAO.ADD_REQUEST))) {
            addPreparedStatement.setString(1,
                membreDTO.getNom());
            addPreparedStatement.setLong(2,
                membreDTO.getTelephone());
            addPreparedStatement.setInt(3,
                membreDTO.getLimitePret());
            addPreparedStatement.execute();

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
    public MembreDTO get(Connexion connexion,
        Serializable primaryKey) throws DAOException,
        InvalidHibernateSessionException,
        InvalidPrimaryKeyException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException();
        }
        if(primaryKey == null) {
            throw new InvalidPrimaryKeyException();
        }
        String idMembre = (String) primaryKey;
        MembreDTO tempMembre = null;
        try(
            PreparedStatement statementRead = (connexion.getConnection().prepareStatement(MembreDAO.READ_REQUEST))) {
            statementRead.setString(1,
                idMembre);
            try(
                ResultSet resultSet = statementRead.executeQuery()) {
                if(resultSet.next()) {
                    tempMembre = new MembreDTO();
                    tempMembre.setIdMembre(resultSet.getString(1));
                    tempMembre.setNom(resultSet.getString(2));
                    tempMembre.setTelephone(resultSet.getLong(3));
                    tempMembre.setLimitePret(resultSet.getInt(4));
                    tempMembre.setNbPret(resultSet.getInt(5));

                    resultSet.close();
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode())
                + " "
                + sqlException.getMessage(),
                sqlException);
        }
        return tempMembre;
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
        MembreDTO membreDTO = (MembreDTO) dto;
        try(

            PreparedStatement updatePreparedStatement = (connexion.getConnection().prepareStatement(MembreDAO.UPDATE_REQUEST))) {

            updatePreparedStatement.setString(1,
                membreDTO.getNom());
            updatePreparedStatement.setLong(2,
                membreDTO.getTelephone());
            updatePreparedStatement.setInt(3,
                membreDTO.getLimitePret());
            updatePreparedStatement.setInt(4,
                membreDTO.getNbPret());
            updatePreparedStatement.setString(5,
                membreDTO.getIdMembre());

            updatePreparedStatement.execute();

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
        MembreDTO membreDTO = (MembreDTO) dto;
        try(
            PreparedStatement deletePreparedStatement = (connexion.getConnection().prepareStatement(MembreDAO.DELETE_REQUEST))) {

            deletePreparedStatement.setString(1,
                membreDTO.getIdMembre());
            deletePreparedStatement.execute();
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
    public List<MembreDTO> getAll(Connexion connexion,
        String sortByPropertyName) throws DAOException,
        InvalidHibernateSessionException,
        InvalidSortByPropertyException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété ne peut être null");
        }
        List<MembreDTO> membres = Collections.<MembreDTO> emptyList();

        try(
            PreparedStatement statementGetAllLivres = (connexion.getConnection().prepareStatement(MembreDAO.GET_ALL_REQUEST))) {
            try(
                ResultSet resultSet = statementGetAllLivres.executeQuery()) {
                MembreDTO membreDTO = null;
                if(resultSet.next()) {
                    membres = new ArrayList<>();
                    do {
                        membreDTO = new MembreDTO();
                        membreDTO.setIdMembre(resultSet.getString(1));
                        membreDTO.setNom(resultSet.getString(2));
                        membreDTO.setTelephone(resultSet.getLong(3));
                        membreDTO.setLimitePret(resultSet.getInt(4));
                        membreDTO.setNbPret(resultSet.getInt(5));
                        membres.add(membreDTO);
                    } while(resultSet.next());
                }
                return membres;
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
    public List<MembreDTO> findByNom(Connexion connexion,
        String nom,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        DAOException {
        // TODO Auto-generated method stub
        return null;
    }
}
