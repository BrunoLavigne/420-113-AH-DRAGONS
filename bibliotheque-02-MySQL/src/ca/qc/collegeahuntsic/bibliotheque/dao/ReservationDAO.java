// Fichier ReservationDAO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        + "values (?,?,?,str_to_date(?, '%Y-%m-%d'))";

    private static final String DELETE_REQUEST = "DELETE FROM reservation "
        + "WHERE idReservation = ?";

    private final static String UPDATE_REQUEST = "UPDATE reservation set dateReservation = ?"
        + "WHERE idReservation = ?";

    /**
     *
     * TODO Auto-generated constructor javadoc
     *
     * @param connexion
     */
    public ReservationDAO(Connexion connexion) {
        super(connexion);
    }

    /**
     *
     * Ajouter une reservation
     *
     * @param reservationDTO
     * @throws DAOException En cas d'erreur de connction ou d'ajout de reservation, l'erreur est traitée.
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
     * Lecture de la reéervation
     *
     * @param idReservation
     * @return
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
     * Mettre à jour une reservation
     *
     * @param idReservation
     * @throws DAOException En cas d'erreur de connction ou d'ajout de reservation, l'erreur est traitée.
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
     * Effacer une reservation
     *
     * @param reservationDTO
     * @throws DAOException
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

    // TODO fix warning
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

    // TODO fix warning
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

    // TODO fix warning
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

}
