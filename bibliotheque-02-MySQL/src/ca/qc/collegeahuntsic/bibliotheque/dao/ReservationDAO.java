// Fichier ReservationDAO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;

/**
 * <pre>
 * Permet d'effectuer les accès à la table reservation.
 *
 * Cette classe gère tous les accès à la table reservation.
 *
 *</pre>
 */

public class ReservationDAO extends DAO {

    private static final long serialVersionUID = 1L;

    private static final String READ_REQUEST = "SELECT idReservation, idLivre, idMembre, dateReservation "
        + "FROM reservation "
        + "WHERE idReservation = ?";

    private final static String ADD_REQUEST = "INSERT INTO reservation (idReservation, idlivre, idMembre, dateReservation) "
        + "VALUES (?,?,?,str_to_date(?, '%Y-%m-%d'))";

    private static final String DELETE_REQUEST = "DELETE FROM reservation "
        + "WHERE idReservation = ?";

    private final static String UPDATE_REQUEST = "UPDATE reservation set dateReservation = ?"
        + "WHERE idReservation = ?";

    private final static String GET_ALL_REQUEST = "SELECT idReservation, idLivre, idMembre, dateReservation "
        + "FROM reservation";

    private final static String FIND_BY_LIVRE = "SELECT idReservation, idLivre, idMembre, dateReservation "
        + "FROM reservation "
        + "WHERE idLivre = ?";

    private final static String FIND_BY_MEMBRE = "SELECT idReservation, idLivre, idMembre, dateReservation "
        + "FROM reservation "
        + "WHERE idMembre = ?";

    /**
     *
     * Création d'une instance. Pré-compilation d'énoncés SQL
     *
     * @param connexion La connexion à la base de données
     */
    public ReservationDAO(Connexion connexion) {
        super(connexion);
    }

    /**
     *
     * Ajouter une réservation à la base de données
     *
     * @param reservationDTO La réservation qui doit être ajoutée.
     * @throws DAOException En cas d'erreur de connexion ou d'ajout de reservation.
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
            addPreparedStatement.setDate(4,
                reservationDTO.getDateReservation());
            addPreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Lecture d'une réservation de la base de données
     *
     * @param idReservation Le id de la réservation qui est recherchée
     * @return La réservation voulue
     * @throws DAOException En cas d'erreur de connction ou d'ajout de reservation, l'erreur est traitée.
     */
    public ReservationDTO read(int idReservation) throws DAOException {
        ReservationDTO reservationDTO = null;
        try(
            PreparedStatement readPreparedStatement = getConnection().prepareStatement(ReservationDAO.READ_REQUEST)) {
            readPreparedStatement.setInt(1,
                idReservation);
            try(
                ResultSet resultSet = readPreparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    reservationDTO = new ReservationDTO();
                    reservationDTO.setIdReservation(resultSet.getInt(1));
                    reservationDTO.setIdLivre(resultSet.getInt(2));
                    reservationDTO.setIdMembre(resultSet.getInt(3));
                    reservationDTO.setDateReservation(resultSet.getDate(4));
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return reservationDTO;

    }

    /**
     *
     * Mise à jour d'une réservation qui se trouve dans la base de données
     *
     * @param idReservation Le ID de la réservation à mettre à jour
     * @throws DAOException Si il-y-a une erreur lors de l'exécution de la requête SQL, celle-ci est traitée
     */
    public void update(ReservationDTO reservationDTO,
        Date dateReservation) throws DAOException {
        try(
            PreparedStatement updatePreparedStatement = getConnection().prepareStatement(ReservationDAO.UPDATE_REQUEST)) {
            updatePreparedStatement.setDate(1,
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
     * Effacer une réservation de la base de données
     *
     * @param reservationDTO La réservation à effacer de la base de données
     * @throws DAOException Si il-y-a une erreur lors de l'exécution de la requête SQL, celle-ci est traitée
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
     * Méthode retournant une liste de type <code>List</code> contenant des objets <code>ReservationDTO</code>.
     * La liste contient toutes les réservations enregistrées dans la base de données
     *
     * @return liste une liste d'objets de type <code>ReservationDTO</code> représentant
     * les réservation enregistrées dans la base de données.
     *
     * @throws DAOException Si il-y-a une erreur lors de l'exécution de la requête SQL, celle-ci est traitée
     */
    public List<ReservationDTO> getAll() throws DAOException {

        List<ReservationDTO> liste = Collections.EMPTY_LIST;

        try(
            PreparedStatement stmtGetAllReservation = (getConnection().prepareStatement(ReservationDAO.GET_ALL_REQUEST));
            ResultSet results = stmtGetAllReservation.executeQuery()) {

            liste = new ArrayList<>();

            try(
                ResultSet resultSet = stmtGetAllReservation.executeQuery()) {
                while(resultSet.next()) {
                    ReservationDTO reservationDTO = new ReservationDTO();
                    reservationDTO.setIdReservation(resultSet.getInt(1));
                    reservationDTO.setIdLivre(resultSet.getInt(2));
                    reservationDTO.setIdMembre(resultSet.getInt(3));
                    reservationDTO.setDateReservation(resultSet.getDate(4));
                    liste.add(reservationDTO);
                }
            }
            return liste;
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Recherche des réservations associées à un livre
     *
     * @param idLivre Le ID du livre pour lequel nous cherchons les réservations
     * @return liste La liste qui contient les réservations selon le livre
     * @throws DAOException Si il-y-a une erreur lors de l'exécution de la requête SQL, celle-ci est traitée
     */

    public List<ReservationDTO> getByLivre(int idLivre) throws DAOException {

        List<ReservationDTO> liste = Collections.EMPTY_LIST;

        try(
            PreparedStatement stmtGetByLivre = (getConnection().prepareStatement(ReservationDAO.FIND_BY_LIVRE));) {

            stmtGetByLivre.setInt(1,
                idLivre);

            liste = new ArrayList<>();

            try(
                ResultSet rset = stmtGetByLivre.executeQuery();) {

                if(rset.next()) {

                    ReservationDTO reservationDTO = new ReservationDTO();
                    reservationDTO.setIdReservation(rset.getInt(1));
                    reservationDTO.setIdLivre(rset.getInt(2));
                    reservationDTO.setIdMembre(rset.getInt(3));
                    reservationDTO.setDateReservation(rset.getDate(4));
                    liste.add(reservationDTO);

                }

                return liste;

            } catch(Exception exception) {
                throw new DAOException(exception);
            }

        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Recherche des réservations associées à un membre
     *
     * @param idMembre Le ID du membre pour lequel nous cherchons les réservations
     * @return liste La liste qui contient toutes les réservation associées à un membre
     * @throws DAOException Si il-y-a une erreur lors de l'exécution de la requête SQL, celle-ci est traitée
     */

    public List<ReservationDTO> getByMembre(int idMembre) throws DAOException {

        List<ReservationDTO> liste = Collections.EMPTY_LIST;

        try(
            PreparedStatement stmtGetByMembre = (getConnection().prepareStatement(ReservationDAO.FIND_BY_MEMBRE));) {

            stmtGetByMembre.setInt(1,
                idMembre);

            liste = new ArrayList<>();

            try(
                ResultSet rset = stmtGetByMembre.executeQuery();) {

                if(rset.next()) {
                    ReservationDTO reservationDTO = new ReservationDTO();
                    reservationDTO.setIdReservation(rset.getInt(1));
                    reservationDTO.setIdLivre(rset.getInt(2));
                    reservationDTO.setIdMembre(rset.getInt(3));
                    reservationDTO.setDateReservation(rset.getDate(4));
                    liste.add(reservationDTO);
                }
                return liste;

            } catch(Exception exception) {
                throw new DAOException(exception);
            }

        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    // Region OLD STUFF

    /**
     *
     * Vérifie si une réservation existe.
     *
     * @param idReservation
     * @return boolean Si la réservation existe
     * @throws DAOException
     */
    /*public boolean existe(int idReservation) throws DAOException {

        try {
            getStmtExiste().setInt(1,
                idReservation);
            try(
                ResultSet rset = getStmtExiste().executeQuery()) {
                boolean reservationExiste = rset.next();
                rset.close();
                return reservationExiste;
            }

        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }

    }*/

    /**
     *
     * Lecture d'une réservation.
     *
     * @param idReservation
     * @return TupleReservation La réservation
     * @throws DAOException
     */

    /* public ReservationDTO getReservation(int idReservation) throws DAOException {

         try {
             getStmtExiste().setInt(1,

                 idReservation);
             try(
                 ResultSet rset = getStmtExiste().executeQuery()) {
                 if(rset.next()) {
                     ReservationDTO tupleReservation = new ReservationDTO();
                     tupleReservation.setIdReservation(rset.getInt(1));
                     tupleReservation.setIdLivre(rset.getInt(2));

                     tupleReservation.setIdMembre(rset.getInt(3));
                     tupleReservation.setDateReservation(rset.getDate(4));
                     return tupleReservation;
                 }
             }
         } catch(SQLException sqlException) {
             throw new DAOException(sqlException);
         }

         return null;

     }*/

    /**
     *
     * Lecture de la première réservation d'un livre.
     *
     * @param idLivre
     * @return TupleReservation La réservation du livre
     * @throws DAOException
     */

    /*  public ReservationDTO getReservationLivre(int idLivre) throws DAOException {

          try {
              getStmtExisteLivre().setInt(1,
                  idLivre);
              try(
                  ResultSet rset = getStmtExisteLivre().executeQuery()) {
                  if(rset.next()) {
                      ReservationDTO tupleReservation = new ReservationDTO();
                      tupleReservation.setIdReservation(rset.getInt(1));
                      tupleReservation.setIdLivre(rset.getInt(2));

                      tupleReservation.setIdMembre(rset.getInt(3));
                      tupleReservation.setDateReservation(rset.getDate(4));
                      return tupleReservation;
                  }
              }

          } catch(SQLException sqlException) {
              throw new DAOException(sqlException);
          }
          return null;
      }*/

    /**
     *
     * Lecture de la première réservation d'un livre.
     *
     * @param idMembre
     * @return TupleReservation réservation membre
     * @throws DAOException
     */

    /* public ReservationDTO getReservationMembre(int idMembre) throws DAOException {

         try {
             getStmtExisteMembre().setInt(1,
                 idMembre);
             try(
                 ResultSet rset = getStmtExisteMembre().executeQuery()) {
                 if(rset.next()) {
                     ReservationDTO tupleReservation = new ReservationDTO();
                     tupleReservation.setIdReservation(rset.getInt(1));
                     tupleReservation.setIdLivre(rset.getInt(2));

                     tupleReservation.setIdMembre(rset.getInt(3));
                     tupleReservation.setDateReservation(rset.getDate(4));
                     return tupleReservation;
                 }
             }
         } catch(SQLException sqlException) {
             throw new DAOException(sqlException);
         }
         return null;
     }*/

    /**
     *
     * Réservation d'un livre
     *
     * @param idReservation
     * @param idLivre
     * @param idMembre
     * @param dateReservation
     * @throws DAOException
     */
    /*public void reserver(int idReservation,
        int idLivre,
        int idMembre,
        String dateReservation) throws DAOException {
        try {
            getStmtInsert().setInt(1,
                idReservation);
            getStmtInsert().setInt(2,
                idLivre);
            getStmtInsert().setInt(3,
                idMembre);
            getStmtInsert().setDate(4,
                Date.valueOf(dateReservation));
            getStmtInsert().executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }*/

    /**
     *
     * Suppression d'une réservation.
     *
     * @param idReservation
     * @return int annulerRes
     * @throws DAOException
     */
    /*public int annulerRes(int idReservation) throws DAOException {
        try {
            getStmtDelete().setInt(1,
                idReservation);
            return getStmtDelete().executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }*/

    // EndRegion OLD STUFF
}
