
package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.implementations.DAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IPretDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.DTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOClassException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;

public class PretDAO extends DAO implements IPretDAO {

    private final static String ADD_REQUEST = "INSERT INTO pret (idMembre, idLivre, datePret, dateRetour) "
        + "VALUES (?,?,?, NULL)";

    private final static String READ_REQUEST = "SELECT idPret, idMembre, idLivre, datePret, dateRetour "
        + "FROM pret "
        + "WHERE idPret = ?";

    private final static String UPDATE_REQUEST = "UPDATE pret SET idMembre = ?, idLivre = ?, datePret = ?, dateRetour = ? "
        + "WHERE idPret = ?";

    private final static String DELETE_REQUEST = "DELETE FROM pret "
        + "WHERE idPret = ?";

    private final static String GET_ALL_REQUEST = "SELECT idPret, idMembre, idLivre, datePret, dateRetour FROM pret ORDER BY datePret ASC";

    private final static String FIND_BY_MEMBRE = "SELECT idPret, idMembre, idLivre, datePret, dateRetour FROM pret WHERE idMembre = ? AND dateRetour IS NULL ORDER BY datePret ASC";

    private final static String FIND_BY_LIVRE = "SELECT idPret, idMembre, idLivre, datePret, dateRetour FROM pret WHERE idLivre = ? AND dateRetour IS NULL ORDER BY datePret ASC";

    private final static String FIND_BY_DATE_PRET = "SELECT idPret, idMembre, idLivre, datePret, dateRetour FROM pret WHERE datePret = ? AND dateRetour IS NULL ORDER BY datePret ASC";

    private final static String FIND_BY_DATE_RETOUR = "SELECT idPret, idMembre, idLivre, datePret, dateRetour FROM pret WHERE dateRetour = ? AND dateRetour IS NULL ORDER BY datePret ASC";

    /**
     *
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion - La connexion à utiliser
     */
    public PretDAO(Class<PretDTO> pretDTOClass) throws InvalidDTOClassException { // TODO changer la visibilité a package quand nous aurons la version avec Spring
        super(pretDTOClass);
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
        PretDTO pretDTO = (PretDTO) dto;

        try(
            PreparedStatement statementAdd = (connexion.getConnection().prepareStatement(PretDAO.ADD_REQUEST))) {

            statementAdd.setString(1,
                pretDTO.getMembreDTO().getIdMembre());
            statementAdd.setString(2,
                pretDTO.getLivreDTO().getIdLivre());
            statementAdd.setTimestamp(3,
                pretDTO.getDatePret());
            statementAdd.executeUpdate();

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
    public PretDTO get(Connexion connexion,
        Serializable primaryKey) throws DAOException,
        InvalidHibernateSessionException,
        InvalidPrimaryKeyException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException();
        }
        if(primaryKey == null) {
            throw new InvalidPrimaryKeyException();
        }
        String idPret = (String) primaryKey;
        PretDTO tempPret = null;
        try(
            PreparedStatement statementRead = (connexion.getConnection().prepareStatement(PretDAO.READ_REQUEST))) {
            statementRead.setString(1,
                idPret);

            try(
                ResultSet rset = statementRead.executeQuery()) {
                if(rset.next()) {

                    tempPret = new PretDTO();
                    tempPret.setIdPret(rset.getString(1));
                    MembreDTO membre = new MembreDTO();
                    membre.setIdMembre(rset.getString(2));
                    tempPret.setMembreDTO(membre);
                    LivreDTO livre = new LivreDTO();
                    livre.setIdLivre(rset.getString(3));
                    tempPret.setLivreDTO(livre);
                    tempPret.setDatePret(rset.getTimestamp(4));
                    tempPret.setDateRetour(rset.getTimestamp(5));
                    rset.close();
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode())
                + " "
                + sqlException.getMessage(),
                sqlException);
        }
        return tempPret;
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
        PretDTO pretDTO = (PretDTO) dto;
        try(
            PreparedStatement statementRead = (connexion.getConnection().prepareStatement(PretDAO.UPDATE_REQUEST))) {
            statementRead.setString(1,
                pretDTO.getMembreDTO().getIdMembre());
            statementRead.setString(2,
                pretDTO.getLivreDTO().getIdLivre());
            statementRead.setTimestamp(3,
                pretDTO.getDatePret());
            statementRead.setTimestamp(4,
                pretDTO.getDateRetour());
            statementRead.setString(5,
                pretDTO.getIdPret());

            statementRead.executeUpdate();
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
        PretDTO pretDTO = (PretDTO) dto;
        try(
            PreparedStatement statementRead = (connexion.getConnection().prepareStatement(PretDAO.DELETE_REQUEST))) {
            statementRead.setString(1,
                pretDTO.getIdPret());
            statementRead.executeUpdate();
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
    public List<PretDTO> getAll(Connexion connexion,
        String sortByPropertyName) throws DAOException,
        InvalidHibernateSessionException,
        InvalidSortByPropertyException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété ne peut être null");
        }
        List<PretDTO> prets = Collections.<PretDTO> emptyList();

        try(
            PreparedStatement statementGetAllLivres = (connexion.getConnection().prepareStatement(PretDAO.GET_ALL_REQUEST))) {
            try(
                ResultSet resultSet = statementGetAllLivres.executeQuery()) {
                PretDTO pretDTO = null;
                if(resultSet.next()) {
                    prets = new ArrayList<>();
                    do {

                        pretDTO = new PretDTO();
                        pretDTO.setIdPret(resultSet.getString(1));

                        MembreDTO membre = new MembreDTO();
                        membre.setIdMembre(resultSet.getString(2));

                        LivreDTO livre = new LivreDTO();
                        livre.setIdLivre(resultSet.getString(3));

                        pretDTO.setMembreDTO(membre);
                        pretDTO.setLivreDTO(livre);
                        pretDTO.setDatePret(resultSet.getTimestamp(4));
                        pretDTO.setDateRetour(resultSet.getTimestamp(5));

                        prets.add(pretDTO);

                    } while(resultSet.next());
                }
                return prets;
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
    public List<PretDTO> findByMembre(Connexion connexion,
        String idMembre,
        String sortByPropertyName) throws DAOException,
        InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(idMembre == null) {
            throw new InvalidCriterionException("Le membre ne peut être null");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété ne peut être null");
        }
        List<PretDTO> pret = Collections.<PretDTO> emptyList();
        try(
            PreparedStatement stmtGetPretsByMembre = (connexion.getConnection().prepareStatement(PretDAO.FIND_BY_MEMBRE));) {
            stmtGetPretsByMembre.setString(1,
                idMembre);
            try(
                ResultSet rset = stmtGetPretsByMembre.executeQuery()) {
                pret = new ArrayList<>();

                while(rset.next()) {

                    PretDTO tempPret = new PretDTO();
                    tempPret.setIdPret(rset.getString(1));

                    MembreDTO membre = new MembreDTO();
                    membre.setIdMembre(rset.getString(2));

                    LivreDTO livre = new LivreDTO();
                    livre.setIdLivre(rset.getString(3));

                    tempPret.setMembreDTO(membre);
                    tempPret.setLivreDTO(livre);
                    tempPret.setDatePret(rset.getTimestamp(4));
                    tempPret.setDateRetour(rset.getTimestamp(5));

                    pret.add(tempPret);
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode())
                + " "
                + sqlException.getMessage(),
                sqlException);
        }
        return pret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> findByLivre(Connexion connexion,
        String idLivre,
        String sortByPropertyName) throws DAOException,
        InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(idLivre == null) {
            throw new InvalidCriterionException("Le livre ne peut être null");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété ne peut être null");
        }
        List<PretDTO> pret = Collections.<PretDTO> emptyList();
        try(
            PreparedStatement stmtGetPretsByMembre = (connexion.getConnection().prepareStatement(PretDAO.FIND_BY_LIVRE));) {
            stmtGetPretsByMembre.setString(1,
                idLivre);
            try(
                ResultSet rset = stmtGetPretsByMembre.executeQuery()) {

                pret = new ArrayList<>();

                while(rset.next()) {

                    PretDTO tempPret = new PretDTO();
                    tempPret.setIdPret(rset.getString(1));

                    MembreDTO membre = new MembreDTO();
                    membre.setIdMembre(rset.getString(2));

                    LivreDTO livre = new LivreDTO();
                    livre.setIdLivre(rset.getString(3));

                    tempPret.setMembreDTO(membre);
                    tempPret.setLivreDTO(livre);
                    tempPret.setDatePret(rset.getTimestamp(4));
                    tempPret.setDateRetour(rset.getTimestamp(5));
                    pret.add(tempPret);
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode())
                + " "
                + sqlException.getMessage(),
                sqlException);
        }
        return pret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> findByDatePret(Connexion connexion,
        Timestamp datePret,
        String sortByPropertyName) throws DAOException,
        InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(datePret == null) {
            throw new InvalidCriterionException("La date de pret ne peut être null");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété ne peut être null");
        }
        List<PretDTO> pret = Collections.<PretDTO> emptyList();
        try(
            // TODO passer d'un timestamp exact à une date???
            PreparedStatement stmtGetPretsByMembre = (connexion.getConnection().prepareStatement(PretDAO.FIND_BY_DATE_PRET));) {
            stmtGetPretsByMembre.setTimestamp(1,
                datePret);
            try(
                ResultSet rset = stmtGetPretsByMembre.executeQuery()) {

                pret = new ArrayList<>();

                while(rset.next()) {

                    PretDTO tempPret = new PretDTO();
                    tempPret.setIdPret(rset.getString(1));

                    MembreDTO membre = new MembreDTO();
                    membre.setIdMembre(rset.getString(2));

                    LivreDTO livre = new LivreDTO();
                    livre.setIdLivre(rset.getString(3));

                    tempPret.setMembreDTO(membre);
                    tempPret.setLivreDTO(livre);
                    tempPret.setDatePret(rset.getTimestamp(4));
                    tempPret.setDateRetour(rset.getTimestamp(5));

                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode())
                + " "
                + sqlException.getMessage(),
                sqlException);
        }
        return pret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> findByDateRetour(Connexion connexion,
        Timestamp dateRetour,
        String sortByPropertyName) throws DAOException,
        InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(dateRetour == null) {
            throw new InvalidCriterionException("La date de retour ne peut être null");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété ne peut être null");
        }
        List<PretDTO> pret = Collections.<PretDTO> emptyList();
        try(
            // TODO passer d'un timestamp exact à une date???
            PreparedStatement stmtGetPretsByMembre = (connexion.getConnection().prepareStatement(PretDAO.FIND_BY_DATE_RETOUR));) {
            stmtGetPretsByMembre.setTimestamp(1,
                dateRetour);
            try(
                ResultSet rset = stmtGetPretsByMembre.executeQuery()) {
                pret = new ArrayList<>();
                while(rset.next()) {

                    PretDTO tempPret = new PretDTO();
                    tempPret.setIdPret(rset.getString(1));

                    MembreDTO membre = new MembreDTO();
                    membre.setIdMembre(rset.getString(2));

                    LivreDTO livre = new LivreDTO();
                    livre.setIdLivre(rset.getString(3));

                    tempPret.setMembreDTO(membre);
                    tempPret.setLivreDTO(livre);
                    tempPret.setDatePret(rset.getTimestamp(4));
                    tempPret.setDateRetour(rset.getTimestamp(5));

                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode())
                + " "
                + sqlException.getMessage(),
                sqlException);
        }
        return pret;
    }
}
