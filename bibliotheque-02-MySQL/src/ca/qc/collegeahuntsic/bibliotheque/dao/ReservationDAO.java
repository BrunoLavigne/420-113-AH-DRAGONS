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

    private PreparedStatement stmtExiste;

    private PreparedStatement stmtExisteLivre;

    private PreparedStatement stmtExisteMembre;

    private PreparedStatement stmtInsert;

    private PreparedStatement stmtDelete;

    private Connexion cx;

    private static final String READ_REQUEST = "SELECT idReservation, idLivre, idMembre, dateReservation "
        + "FROM reservation "
        + "WHERE idReservation = ?";

    private final static String ADD_REQUEST = "INSERT INTO reservation (idReservation, idlivre, idMembre, dateReservation) "
        + "values (?,?,?,str_to_date(?, '%Y-%m-%d'))";

    private static final String DELETE_REQUEST = "DELETE FROM reservation "
        + "WHERE idReservation = ?";

    private final static String FIND_BY_MEMBRE = "SELECT idReservation, idLivre, idMembre, dateReservation "
        + "FROM reservation "
        + "WHERE idMembre = ? ";

    private final static String FIND_BY_LIVRE = "SELECT idReservation, idLivre, idMembre, dateReservation "
        + "FROM reservation "
        + "WHERE idLivre = ? "
        + "ORDER BY dateReservation";

    /**
     * Creation d'une instance.
     *
     * @param cx
     * @throws DAOException
     */
    public ReservationDAO(Connexion cx) throws DAOException {

        setCx(cx);

        try {
            setStmtExiste(getCx().getConnection().prepareStatement(READ_REQUEST));
            setStmtExisteLivre(getCx().getConnection().prepareStatement(FIND_BY_LIVRE));
            setStmtExisteMembre(getCx().getConnection().prepareStatement(FIND_BY_MEMBRE));
            setStmtInsert(getCx().getConnection().prepareStatement(ADD_REQUEST));
            setStmtDelete(getCx().getConnection().prepareStatement(DELETE_REQUEST));
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Retourner la connexion associée.
     *
     * @return La connexion
     */
    public Connexion getConnexion() {

        return this.cx;
    }

    /**
     *
     * Vérifie si une réservation existe.
     *
     * @param idReservation
     * @return boolean Si la réservation existe
     * @throws DAOException
     */
    public boolean existe(int idReservation) throws DAOException {

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

    }

    /**
     *
     * Lecture d'une réservation.
     *
     * @param idReservation
     * @return TupleReservation La réservation
     * @throws DAOException
     */

    // TODO fix warning
    public ReservationDTO getReservation(int idReservation) throws DAOException {

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

    }

    /**
     *
     * Lecture de la première réservation d'un livre.
     *
     * @param idLivre
     * @return TupleReservation La réservation du livre
     * @throws DAOException
     */

    // TODO fix warning
    public ReservationDTO getReservationLivre(int idLivre) throws DAOException {

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
    }

    /**
     *
     * Lecture de la première réservation d'un livre.
     *
     * @param idMembre
     * @return TupleReservation réservation membre
     * @throws DAOException
     */

    // TODO fix warning
    public ReservationDTO getReservationMembre(int idMembre) throws DAOException {

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
    }

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
    public void reserver(int idReservation,
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
    }

    /**
     *
     * Suppression d'une réservation.
     *
     * @param idReservation
     * @return int annulerRes
     * @throws DAOException
     */
    public int annulerRes(int idReservation) throws DAOException {
        try {
            getStmtDelete().setInt(1,
                idReservation);
            return getStmtDelete().executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    // GETTER ET SETTER

    /**
     * Getter de la variable d'instance <code>this.stmtExiste</code>.
     *
     * @return La variable d'instance <code>this.stmtExiste</code>
     */
    private PreparedStatement getStmtExiste() {
        return this.stmtExiste;
    }

    /**
     * Setter de la variable d'instance <code>this.stmtExiste</code>.
     *
     * @param stmtExiste La valeur à utiliser pour la variable d'instance <code>this.stmtExiste</code>
     */
    private void setStmtExiste(PreparedStatement stmtExiste) {
        this.stmtExiste = stmtExiste;
    }

    /**
     * Getter de la variable d'instance <code>this.stmtExisteLivre</code>.
     *
     * @return La variable d'instance <code>this.stmtExisteLivre</code>
     */
    private PreparedStatement getStmtExisteLivre() {
        return this.stmtExisteLivre;
    }

    /**
     * Setter de la variable d'instance <code>this.stmtExisteLivre</code>.
     *
     * @param stmtExisteLivre La valeur à utiliser pour la variable d'instance <code>this.stmtExisteLivre</code>
     */
    private void setStmtExisteLivre(PreparedStatement stmtExisteLivre) {
        this.stmtExisteLivre = stmtExisteLivre;
    }

    /**
     * Getter de la variable d'instance <code>this.stmtExisteMembre</code>.
     *
     * @return La variable d'instance <code>this.stmtExisteMembre</code>
     */
    private PreparedStatement getStmtExisteMembre() {
        return this.stmtExisteMembre;
    }

    /**
     * Setter de la variable d'instance <code>this.stmtExisteMembre</code>.
     *
     * @param stmtExisteMembre La valeur à utiliser pour la variable d'instance <code>this.stmtExisteMembre</code>
     */
    private void setStmtExisteMembre(PreparedStatement stmtExisteMembre) {
        this.stmtExisteMembre = stmtExisteMembre;
    }

    /**
     * Getter de la variable d'instance <code>this.stmtInsert</code>.
     *
     * @return La variable d'instance <code>this.stmtInsert</code>
     */
    private PreparedStatement getStmtInsert() {
        return this.stmtInsert;
    }

    /**
     * Setter de la variable d'instance <code>this.stmtInsert</code>.
     *
     * @param stmtInsert La valeur à utiliser pour la variable d'instance <code>this.stmtInsert</code>
     */
    private void setStmtInsert(PreparedStatement stmtInsert) {
        this.stmtInsert = stmtInsert;
    }

    /**
     * Getter de la variable d'instance <code>this.stmtDelete</code>.
     *
     * @return La variable d'instance <code>this.stmtDelete</code>
     */
    private PreparedStatement getStmtDelete() {
        return this.stmtDelete;
    }

    /**
     * Setter de la variable d'instance <code>this.stmtDelete</code>.
     *
     * @param stmtDelete La valeur à utiliser pour la variable d'instance <code>this.stmtDelete</code>
     */
    private void setStmtDelete(PreparedStatement stmtDelete) {
        this.stmtDelete = stmtDelete;
    }

    /**
     * Getter de la variable d'instance <code>this.cx</code>.
     *
     * @return La variable d'instance <code>this.cx</code>
     */
    private Connexion getCx() {
        return this.cx;
    }

    /**
     * Setter de la variable d'instance <code>this.cx</code>.
     *
     * @param cx La valeur à utiliser pour la variable d'instance <code>this.cx</code>
     */
    private void setCx(Connexion cx) {
        this.cx = cx;
    }
}
