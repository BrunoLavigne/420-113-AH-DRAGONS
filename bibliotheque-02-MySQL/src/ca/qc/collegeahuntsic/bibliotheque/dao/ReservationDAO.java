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

    /**
     * Creation d'une instance.
     *
     * @param cx
     * @throws SQLException
     */
    public ReservationDAO(Connexion cx) throws SQLException {

        setCx(cx);
        setStmtExiste(getCx().getConnection().prepareStatement("select idReservation, idLivre, idMembre, dateReservation "
            + "from reservation where idReservation = ?"));
        setStmtExisteLivre(getCx().getConnection().prepareStatement("select idReservation, idLivre, idMembre, dateReservation "
            + "from reservation where idLivre = ? "
            + "order by dateReservation"));
        setStmtExisteMembre(getCx().getConnection().prepareStatement("select idReservation, idLivre, idMembre, dateReservation "
            + "from reservation where idMembre = ? "));
        setStmtInsert(getCx().getConnection().prepareStatement("insert into reservation (idReservation, idlivre, idMembre, dateReservation) "
            + "values (?,?,?,str_to_date(?, '%Y-%m-%d'))"));
        setStmtDelete(getCx().getConnection().prepareStatement("delete from reservation where idReservation = ?"));
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
     * @throws SQLException
     */
    public boolean existe(int idReservation) throws SQLException {

        getStmtExiste().setInt(1,
            idReservation);

        @SuppressWarnings("resource")
        // TODO fix warning
        ResultSet rset = getStmtExiste().executeQuery();
        boolean reservationExiste = rset.next();
        rset.close();
        return reservationExiste;
    }

    /**
     *
     * Lecture d'une réservation.
     *
     * @param idReservation
     * @return TupleReservation La réservation
     * @throws SQLException
     */
    @SuppressWarnings("resource")
    // TODO fix warning
    public ReservationDTO getReservation(int idReservation) throws SQLException {

        getStmtExiste().setInt(1,
            idReservation);
        ResultSet rset = getStmtExiste().executeQuery();
        if(rset.next()) {
            ReservationDTO tupleReservation = new ReservationDTO();
            tupleReservation.setIdReservation(rset.getInt(1));
            tupleReservation.setIdLivre(rset.getInt(2));

            tupleReservation.setIdMembre(rset.getInt(3));
            tupleReservation.setDateReservation(rset.getDate(4));
            return tupleReservation;
        }
        return null;
    }

    /**
     *
     * Lecture de la première réservation d'un livre.
     *
     * @param idLivre
     * @return TupleReservation La réservation du livre
     * @throws SQLException
     */
    @SuppressWarnings("resource")
    // TODO fix warning
    public ReservationDTO getReservationLivre(int idLivre) throws SQLException {

        getStmtExisteLivre().setInt(1,
            idLivre);
        ResultSet rset = getStmtExisteLivre().executeQuery();
        if(rset.next()) {
            ReservationDTO tupleReservation = new ReservationDTO();
            tupleReservation.setIdReservation(rset.getInt(1));
            tupleReservation.setIdLivre(rset.getInt(2));

            tupleReservation.setIdMembre(rset.getInt(3));
            tupleReservation.setDateReservation(rset.getDate(4));
            return tupleReservation;
        }
        return null;
    }

    /**
     *
     * Lecture de la première réservation d'un livre.
     *
     * @param idMembre
     * @return TupleReservation réservation membre
     * @throws SQLException
     */
    @SuppressWarnings("resource")
    // TODO fix warning
    public ReservationDTO getReservationMembre(int idMembre) throws SQLException {

        getStmtExisteMembre().setInt(1,
            idMembre);
        ResultSet rset = getStmtExisteMembre().executeQuery();
        if(rset.next()) {
            ReservationDTO tupleReservation = new ReservationDTO();
            tupleReservation.setIdReservation(rset.getInt(1));
            tupleReservation.setIdLivre(rset.getInt(2));

            tupleReservation.setIdMembre(rset.getInt(3));
            tupleReservation.setDateReservation(rset.getDate(4));
            return tupleReservation;
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
     * @throws SQLException
     */
    public void reserver(int idReservation,
        int idLivre,
        int idMembre,
        String dateReservation) throws SQLException {
        getStmtInsert().setInt(1,
            idReservation);
        getStmtInsert().setInt(2,
            idLivre);
        getStmtInsert().setInt(3,
            idMembre);
        getStmtInsert().setDate(4,
            Date.valueOf(dateReservation));
        getStmtInsert().executeUpdate();
    }

    /**
     *
     * Suppression d'une réservation.
     *
     * @param idReservation
     * @return int annulerRes
     * @throws SQLException
     */
    public int annulerRes(int idReservation) throws SQLException {
        getStmtDelete().setInt(1,
            idReservation);
        return getStmtDelete().executeUpdate();
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
