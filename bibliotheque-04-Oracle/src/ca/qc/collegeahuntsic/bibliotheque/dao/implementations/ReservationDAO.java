// Fichier ReservationDAO.java
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
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.DTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOClassException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;

/**
 *
 * DAO pour effectuer des CRUDs avec la table <code>reservation</code>.
 *
 * @author Dragons Vicieux
 */

public class ReservationDAO extends DAO implements IReservationDAO {

    private static final String READ_REQUEST = "SELECT idReservation, idLivre, idMembre, dateReservation "
        + "FROM reservation "
        + "WHERE idReservation = ?";

    private final static String ADD_REQUEST = "INSERT INTO RESERVATION (idReservation, idlivre, idMembre, dateReservation) "
        + "VALUES (SEQ_ID_RESERVATION.NEXTVAL, ?, ?, ?)";

    private static final String DELETE_REQUEST = "DELETE FROM RESERVATION "
        + "WHERE idReservation = ?";

    private final static String UPDATE_REQUEST = "UPDATE RESERVATION "
        + "SET idLivre = ?, idMembre = ?, dateReservation = ?"
        + "WHERE idReservation = ?";

    private final static String GET_ALL_REQUEST = "SELECT idReservation, idLivre, idMembre, dateReservation "
        + "FROM reservation";

    private final static String FIND_BY_LIVRE = "SELECT idReservation, idLivre, idMembre, dateReservation "
        + "FROM reservation "
        + "WHERE idLivre = ? "
        + "ORDER BY dateReservation ASC";

    private final static String FIND_BY_MEMBRE = "SELECT idReservation, idLivre, idMembre, dateReservation "
        + "FROM reservation "
        + "WHERE idMembre = ?";

    /**
     * Crée le DAO de la table <code>reservation</code>.
     *
     * @param livreDTOClass La classe de réservation DTO à utiliser
     * @throws InvalidDTOClassException Si la classe de DTO est <code>null</code>
     */
    public ReservationDAO(Class<ReservationDTO> reservationDTOClass) throws InvalidDTOClassException { // TODO changer la visibilité a package quand nous aurons la version avec Spring
        super(reservationDTOClass);
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
        ReservationDTO reservationDTO = (ReservationDTO) dto;
        try(
            PreparedStatement addPreparedStatement = connexion.getConnection().prepareStatement(ReservationDAO.ADD_REQUEST)) {

            addPreparedStatement.setString(1,
                reservationDTO.getLivreDTO().getIdLivre());
            addPreparedStatement.setString(2,
                reservationDTO.getMembreDTO().getIdMembre());
            addPreparedStatement.setTimestamp(3,
                reservationDTO.getDateReservation());

            addPreparedStatement.executeUpdate();

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
    public ReservationDTO get(Connexion connexion,
        Serializable primaryKey) throws DAOException,
        InvalidHibernateSessionException,
        InvalidPrimaryKeyException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(primaryKey == null) {
            throw new InvalidPrimaryKeyException("La clef primaire ne peut pas être null");
        }

        String idReservation = (String) primaryKey;
        ReservationDTO readReservationDTO = null;

        try(
            PreparedStatement readPreparedStatement = connexion.getConnection().prepareStatement(ReservationDAO.READ_REQUEST)) {
            readPreparedStatement.setString(1,
                idReservation);

            try(
                ResultSet resultSet = readPreparedStatement.executeQuery()) {

                if(resultSet.next()) {
                    readReservationDTO = new ReservationDTO();
                    readReservationDTO.setIdReservation(resultSet.getString(1));

                    LivreDTO livreDTO = new LivreDTO();
                    livreDTO.setIdLivre(resultSet.getString(2));

                    MembreDTO membreDTO = new MembreDTO();
                    membreDTO.setIdMembre(resultSet.getString(3));

                    readReservationDTO.setLivreDTO(livreDTO);
                    readReservationDTO.setMembreDTO(membreDTO);
                    readReservationDTO.setDateReservation(resultSet.getTimestamp(4));
                }

            }

        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode())
                + " "
                + sqlException.getMessage(),
                sqlException);
        }

        return readReservationDTO;

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
        ReservationDTO reservationDTO = (ReservationDTO) dto;
        try(
            PreparedStatement updatePreparedStatement = connexion.getConnection().prepareStatement(ReservationDAO.UPDATE_REQUEST)) {

            updatePreparedStatement.setString(1,
                reservationDTO.getLivreDTO().getIdLivre());
            updatePreparedStatement.setString(2,
                reservationDTO.getMembreDTO().getIdMembre());
            updatePreparedStatement.setTimestamp(3,
                reservationDTO.getDateReservation());
            updatePreparedStatement.setString(4,
                reservationDTO.getLivreDTO().getIdLivre());
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
        ReservationDTO reservationDTO = (ReservationDTO) dto;

        try(
            PreparedStatement deletePreparedStatement = connexion.getConnection().prepareStatement(ReservationDAO.DELETE_REQUEST)) {

            deletePreparedStatement.setString(1,
                reservationDTO.getIdReservation());
            deletePreparedStatement.executeUpdate();

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
    public List<? extends DTO> getAll(Connexion connexion,
        String sortByPropertyName) throws DAOException,
        InvalidHibernateSessionException,
        InvalidSortByPropertyException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété ne peut être null");
        }

        List<ReservationDTO> listeReservations = Collections.<ReservationDTO> emptyList();

        try(
            PreparedStatement stmtGetAllReservation = connexion.getConnection().prepareStatement(ReservationDAO.GET_ALL_REQUEST)) {

            try(
                ResultSet resultSet = stmtGetAllReservation.executeQuery()) {

                listeReservations = new ArrayList<>();

                while(resultSet.next()) {

                    ReservationDTO reservationDTO = new ReservationDTO();
                    reservationDTO.setIdReservation(resultSet.getString(1));

                    LivreDTO livre = new LivreDTO();
                    livre.setIdLivre(resultSet.getString(2));

                    MembreDTO membre = new MembreDTO();
                    membre.setIdMembre(resultSet.getString(3));

                    reservationDTO.setLivreDTO(livre);
                    reservationDTO.setMembreDTO(membre);
                    reservationDTO.setDateReservation(resultSet.getTimestamp(4));

                    listeReservations.add(reservationDTO);
                }

            }
            return listeReservations;

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
    public List<ReservationDTO> findByLivre(Connexion connexion,
        String idLivre,
        String SortByPropretyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        DAOException {

        List<ReservationDTO> listeReservations = Collections.<ReservationDTO> emptyList();

        try(
            PreparedStatement findByLivreStmt = connexion.getConnection().prepareStatement(ReservationDAO.FIND_BY_LIVRE);) {

            findByLivreStmt.setString(1,
                idLivre);
            try(
                ResultSet rset = findByLivreStmt.executeQuery()) {

                listeReservations = new ArrayList<>();

                while(rset.next()) {

                    ReservationDTO reservationDTO = new ReservationDTO();
                    reservationDTO.setIdReservation(rset.getString(1));

                    LivreDTO unlivreDTO = new LivreDTO();
                    unlivreDTO.setIdLivre(rset.getString(2));

                    MembreDTO unMembreDTO = new MembreDTO();
                    unMembreDTO.setIdMembre(rset.getString(3));

                    reservationDTO.setLivreDTO(unlivreDTO);
                    reservationDTO.setMembreDTO(unMembreDTO);
                    reservationDTO.setDateReservation(rset.getTimestamp(4));
                    listeReservations.add(reservationDTO);
                }
                return listeReservations;

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
    public List<ReservationDTO> findByMembre(Connexion connexion,
        String idMembre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        DAOException {

        List<ReservationDTO> listeReservations = Collections.<ReservationDTO> emptyList();

        try(
            PreparedStatement findByMembreStmt = connexion.getConnection().prepareStatement(ReservationDAO.FIND_BY_MEMBRE);) {

            findByMembreStmt.setString(1,
                idMembre);

            try(
                ResultSet rset = findByMembreStmt.executeQuery();) {

                listeReservations = new ArrayList<>();

                while(rset.next()) {

                    ReservationDTO reservationDTO = new ReservationDTO();
                    reservationDTO.setIdReservation(rset.getString(1));

                    LivreDTO unLivreDTO = new LivreDTO();
                    unLivreDTO.setIdLivre(rset.getString(2));

                    MembreDTO unMembreDTO = new MembreDTO();
                    unMembreDTO.setIdMembre(rset.getString(3));

                    reservationDTO.setLivreDTO(unLivreDTO);
                    reservationDTO.setMembreDTO(unMembreDTO);
                    reservationDTO.setDateReservation(rset.getTimestamp(4));

                    listeReservations.add(reservationDTO);
                }
                return listeReservations;
            }

        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode())
                + " "
                + sqlException.getMessage(),
                sqlException);
        }
    }

}
