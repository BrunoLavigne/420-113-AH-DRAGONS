// Fichier LivreDAO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;

/**
 * Permet d'effectuer les accès à la table livre.
 */

public class LivreDAO extends DAO {

    private static final long serialVersionUID = 1L;

    private PreparedStatement stmtExiste;

    private PreparedStatement stmtInsert;

    private PreparedStatement stmtUpdate;

    private PreparedStatement stmtDelete;

    private Connexion cx;

    /**
     *
     * Creation d'une instance. Des énoncés SQL pour chaque requête sont précompilés.
     *
     * @param cx
     * @throws SQLException
     */
    public LivreDAO(Connexion cx) throws SQLException {

        this.cx = cx;
        this.stmtExiste = cx.getConnection()
            .prepareStatement("select idlivre, titre, auteur, dateAcquisition, idMembre, datePret from livre where idlivre = ?");
        this.stmtInsert = cx.getConnection().prepareStatement("insert into livre (idLivre, titre, auteur, dateAcquisition, idMembre, datePret) "
            + "values (?,?,?,?,null,null)");
        this.stmtUpdate = cx.getConnection().prepareStatement("update livre set idMembre = ?, datePret = ? "
            + "where idLivre = ?");
        this.stmtDelete = cx.getConnection().prepareStatement("delete from livre where idlivre = ?");
    }

    /**
     *
     * Retourner la connexion associée.
     *
     * @return la connexion
     */
    public Connexion getConnexion() {

        return this.cx;
    }

    /**
     *
     * Vérifie si un livre existe.
     *
     * @param idLivre
     * @return boolean existe
     * @throws SQLException
     */
    public boolean existe(int idLivre) throws SQLException {

        this.stmtExiste.setInt(1,
            idLivre);

        @SuppressWarnings("resource")
        // TODO Fix warning
        ResultSet rset = this.stmtExiste.executeQuery();
        boolean livreExiste = rset.next();
        rset.close();
        return livreExiste;
    }

    /**
     *
     * Lecture d'un livre
     *
     * @param idLivre
     * @return TupleLivre
     * @throws SQLException
     */
    @SuppressWarnings("resource")
    public LivreDTO getLivre(int idLivre) throws SQLException {

        this.stmtExiste.setInt(1,
            idLivre);
        ResultSet rset = this.stmtExiste.executeQuery();
        if(rset.next()) {
            LivreDTO tupleLivre = new LivreDTO();
            tupleLivre.setIdLivre(idLivre);
            tupleLivre.setTitre(rset.getString(2));
            tupleLivre.setAuteur(rset.getString(3));
            tupleLivre.setDateAcquisition(rset.getDate(4));
            tupleLivre.setIdMembre(rset.getInt(5));
            tupleLivre.setDatePret(rset.getDate(6));
            return tupleLivre;
        }
        return null;
    }

    /**
     *
     * Ajout d'un nouveau livre dans la base de données.
     *
     * @param idLivre
     * @param titre
     * @param auteur
     * @param dateAcquisition
     * @throws SQLException
     */
    public void acquerir(int idLivre,
        String titre,
        String auteur,
        String dateAcquisition) throws SQLException {
        /* Ajout du livre. */
        this.stmtInsert.setInt(1,
            idLivre);
        this.stmtInsert.setString(2,
            titre);
        this.stmtInsert.setString(3,
            auteur);
        this.stmtInsert.setDate(4,
            Date.valueOf(dateAcquisition));
        this.stmtInsert.executeUpdate();
    }

    /**
     *
     * Enregistrement de l'emprunteur d'un livre.
     *
     * @param idLivre
     * @param idMembre
     * @param datePret
     * @return int preter
     * @throws SQLException
     */
    public int preter(int idLivre,
        int idMembre,
        String datePret) throws SQLException {
        /* Enregistrement du pret. */
        this.stmtUpdate.setInt(1,
            idMembre);
        this.stmtUpdate.setDate(2,
            Date.valueOf(datePret));
        this.stmtUpdate.setInt(3,
            idLivre);
        return this.stmtUpdate.executeUpdate();
    }

    /**
     *
     * Rendre le livre disponible (non-prêté)
     *
     * @param idLivre
     * @return int retourner
     * @throws SQLException
     */
    public int retourner(int idLivre) throws SQLException {
        /* Enregistrement du pret. */
        this.stmtUpdate.setNull(1,
            Types.INTEGER);
        this.stmtUpdate.setNull(2,
            Types.DATE);
        this.stmtUpdate.setInt(3,
            idLivre);
        return this.stmtUpdate.executeUpdate();
    }

    /**
     *
     * Suppression d'un livre
     *
     * @param idLivre
     * @return int vendre
     * @throws SQLException
     */
    public int vendre(int idLivre) throws SQLException {
        /* Suppression du livre. */
        this.stmtDelete.setInt(1,
            idLivre);
        return this.stmtDelete.executeUpdate();
    }
}
