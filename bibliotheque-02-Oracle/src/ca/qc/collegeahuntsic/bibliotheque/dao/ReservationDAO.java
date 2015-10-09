// Fichier ReservationDAO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;

/**
 *
 * DAO pour effectuer des CRUDs avec la table <code>reservation</code>.
 *
 * @author Dragons Vicieux
 */

public class ReservationDAO extends DAO {

    private static final long serialVersionUID = 1L;

    private static final String READ_REQUEST = "SELECT idReservation, idLivre, idMembre, dateReservation "
        + "FROM reservation "
        + "WHERE idReservation = ?";

    private final static String ADD_REQUEST = "INSERT INTO RESERVATION (idReservation, idlivre, idMembre, dateReservation) "
        + "VALUES (?,?,?,CURRENT_TIMESTAMP)";

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
     *
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion La connexion à utiliser
     */
    public ReservationDAO(Connexion connexion) {
        super(connexion);
    }

    /**
     *
     * Ajoute une nouvelle réservation.
     *
     * @param reservationDTO La réservation à ajouter
     * @throws DAOException S'il y a une erreur avec la base de données
     */

    public void add(ReservationDTO reservationDTO) throws DAOException {
        try(
            PreparedStatement addPreparedStatement = getConnection().prepareStatement(ReservationDAO.ADD_REQUEST)) {
            addPreparedStatement.setInt(1,
                reservationDTO.getIdReservation());
            addPreparedStatement.setInt(2,
                reservationDTO.getIdLivre());
            addPreparedStatement.setInt(3,
                reservationDTO.getIdMembre());
            addPreparedStatement.executeUpdate();

        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Lit une réservation.
     *
     * @param idReservation La réservation à lire
     * @return La réservation voulue
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public ReservationDTO read(int idReservation) throws DAOException {

        ReservationDTO readReservationDTO = null;

        try(
            PreparedStatement readPreparedStatement = getConnection().prepareStatement(ReservationDAO.READ_REQUEST)) {
            readPreparedStatement.setInt(1,
                idReservation);

            try(
                ResultSet resultSet = readPreparedStatement.executeQuery()) {

                if(resultSet.next()) {
                    readReservationDTO = new ReservationDTO();
                    readReservationDTO.setIdReservation(resultSet.getInt(1));
                    readReservationDTO.setIdLivre(resultSet.getInt(2));
                    readReservationDTO.setIdMembre(resultSet.getInt(3));
                    readReservationDTO.setDateReservation(resultSet.getTimestamp(4));
                }

            }

        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }

        return readReservationDTO;

    }

    /**
     *
     * Met à jour une réservation.
     *
     * @param reservationDTO La réservation à mettre à jour
     * @param dateReservation La date de la réservation
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public void update(ReservationDTO reservationDTO,
        Timestamp dateReservation) throws DAOException {

        try(
            PreparedStatement updatePreparedStatement = getConnection().prepareStatement(ReservationDAO.UPDATE_REQUEST)) {

            updatePreparedStatement.setInt(1,
                reservationDTO.getIdLivre());
            updatePreparedStatement.setInt(2,
                reservationDTO.getIdMembre());
            updatePreparedStatement.setTimestamp(3,
                dateReservation);
            updatePreparedStatement.setInt(2,
                reservationDTO.getIdLivre());
            updatePreparedStatement.executeUpdate();

        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Supprime une réservation.
     *
     * @param reservationDTO La réservation à supprimer
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public void delete(ReservationDTO reservationDTO) throws DAOException {

        try(
            PreparedStatement deletePreparedStatement = getConnection().prepareStatement(ReservationDAO.DELETE_REQUEST)) {

            deletePreparedStatement.setInt(1,
                reservationDTO.getIdReservation());
            deletePreparedStatement.executeUpdate();

        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }

    }

    /**
     *
     * Trouve toutes les réservations.
     *
     * @return La liste des réservations ; une liste vide sinon
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public List<ReservationDTO> getAll() throws DAOException {

        List<ReservationDTO> listeReservations = Collections.<ReservationDTO> emptyList();

        try(
            PreparedStatement stmtGetAllReservation = (getConnection().prepareStatement(ReservationDAO.GET_ALL_REQUEST));
            ResultSet results = stmtGetAllReservation.executeQuery()) {

            listeReservations = new ArrayList<>();

            try(
                ResultSet resultSet = stmtGetAllReservation.executeQuery()) {

                while(resultSet.next()) {
                    ReservationDTO reservationDTO = new ReservationDTO();
                    reservationDTO.setIdReservation(resultSet.getInt(1));
                    reservationDTO.setIdLivre(resultSet.getInt(2));
                    reservationDTO.setIdMembre(resultSet.getInt(3));
                    reservationDTO.setDateReservation(resultSet.getTimestamp(4));
                    listeReservations.add(reservationDTO);
                }

            }
            return listeReservations;

        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Trouve les réservations à partir d'un livre.
     *
     * @param livreDTO Le livre à utiliser
     * @return liste La liste des réservations correspondantes, triée par date de réservation croissante ; une liste vide sinon
     * @throws DAOException S'il y a une erreur avec la base de données
     */

    public List<ReservationDTO> findByLivre(LivreDTO livreDTO) throws DAOException {

        List<ReservationDTO> listeReservations = Collections.<ReservationDTO> emptyList();

        try(
            PreparedStatement findByLivreStmt = (getConnection().prepareStatement(ReservationDAO.FIND_BY_LIVRE));) {

            findByLivreStmt.setInt(1,
                livreDTO.getIdLivre());

            listeReservations = new ArrayList<>();

            try(
                ResultSet rset = findByLivreStmt.executeQuery();) {

                if(rset.next()) {

                    ReservationDTO reservationDTO = new ReservationDTO();
                    reservationDTO.setIdReservation(rset.getInt(1));
                    reservationDTO.setIdLivre(rset.getInt(2));
                    reservationDTO.setIdMembre(rset.getInt(3));
                    reservationDTO.setDateReservation(rset.getTimestamp(4));
                    listeReservations.add(reservationDTO);

                }

                return listeReservations;

            } catch(Exception exception) {
                throw new DAOException(exception);
            }

        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Trouve les réservations à partir d'un membre.
     *
     * @param membreDTO Le membre à utiliser
     * @return La liste des réservations correspondantes ; une liste vide sinon
     * @throws DAOException S'il y a une erreur avec la base de données
     */

    public List<ReservationDTO> findByMembre(MembreDTO membreDTO) throws DAOException {

        List<ReservationDTO> listeReservations = Collections.<ReservationDTO> emptyList();

        try(
            PreparedStatement findByMembreStmt = (getConnection().prepareStatement(ReservationDAO.FIND_BY_MEMBRE));) {

            findByMembreStmt.setInt(1,
                membreDTO.getIdMembre());

            listeReservations = new ArrayList<>();

            try(
                ResultSet rset = findByMembreStmt.executeQuery();) {

                if(rset.next()) {
                    ReservationDTO reservationDTO = new ReservationDTO();
                    reservationDTO.setIdReservation(rset.getInt(1));
                    reservationDTO.setIdLivre(rset.getInt(2));
                    reservationDTO.setIdMembre(rset.getInt(3));
                    reservationDTO.setDateReservation(rset.getTimestamp(4));
                    listeReservations.add(reservationDTO);
                }
                return listeReservations;

            } catch(Exception exception) {
                throw new DAOException(exception);
            }

        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

}
