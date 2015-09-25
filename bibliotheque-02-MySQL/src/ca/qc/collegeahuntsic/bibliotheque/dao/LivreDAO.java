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
import ca.qc.collegeahuntsic.bibliotheque.exception.ConnexionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 * Permet d'effectuer les accès à la table livre.
 */

public class LivreDAO extends DAO {

    private static final long serialVersionUID = 1L;

    private PreparedStatement stmtExiste;

    private PreparedStatement stmtInsert;

    private PreparedStatement stmtUpdate;

    private PreparedStatement stmtDelete;

    private PreparedStatement stmtLivresTitreMot;

    private PreparedStatement stmtListeTousLivres;

    private final static String READ_REQUEST = "select idlivre, titre, auteur, dateAcquisition, idMembre, datePret from livre where idlivre = ?";

    private final static String ADD_REQUEST = "insert into livre (idLivre, titre, auteur, dateAcquisition, idMembre, datePret) "
        + "values (?,?,?,?,null,null)";

    private final static String UPDATE_REQUEST = "update livre set idMembre = ?, datePret = ? "
        + "where idLivre = ?";

    private final static String DELETE_REQUEST = "delete from livre where idlivre = ?";

    private final static String GET_ALL_REQUESTS = "select t1.idLivre, t1.titre, t1.auteur, t1.idmembre, t1.datePret "
        + "from livre t1";

    private final static String FIND_BY_TITRE = "SELECT t1.idLivre, t1.titre, t1.auteur, t1.idmembre, t1.datePret + 14 "
        + "FROM LIVRE t1 "
        + "WHERE LOWER(titre) LIKE %?%";

    private final static String FIND_BY_MEMBRE = "select idMembre, nom, telephone, limitePret, nbpret from membre where idmembre = ?";

    private Connexion cx;

    /**
     *
     * Creation d'une instance. Des énoncés SQL pour chaque requête sont précompilés.
     *
     * @param cx
     * @throws DAOException
     */
    public LivreDAO(Connexion cx) throws DAOException {

        setCx(cx);

        try {
            setStmtExiste(getCx().getConnection().prepareStatement(READ_REQUEST));

            setStmtInsert(getCx().getConnection().prepareStatement(ADD_REQUEST));
            setStmtUpdate(getCx().getConnection().prepareStatement(UPDATE_REQUEST));
            setStmtDelete(getCx().getConnection().prepareStatement(DELETE_REQUEST));

            // MERGE
            setStmtLivresTitreMot(getCx().getConnection().prepareStatement(FIND_BY_TITRE));
            setStmtListeTousLivres(getCx().getConnection().prepareStatement(GET_ALL_REQUESTS));

        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
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

        getStmtExiste().setInt(1,
            idLivre);

        @SuppressWarnings("resource")
        // TODO Fix warning
        ResultSet rset = getStmtExiste().executeQuery();
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
    public LivreDTO getLivre(int idLivre) throws DAOException {

        try {
            getStmtExiste().setInt(1,
                idLivre);
            try(
                ResultSet rset = getStmtExiste().executeQuery()) {
                if(rset.next()) {
                    LivreDTO tupleLivre = new LivreDTO();
                    tupleLivre.setIdLivre(idLivre);
                    tupleLivre.setTitre(rset.getString(2));
                    tupleLivre.setAuteur(rset.getString(3));
                    tupleLivre.setDateAcquisition(rset.getDate(4));
                    tupleLivre.setIdMembre(rset.getInt(5));
                    tupleLivre.setDatePret(rset.getDate(6));
                    rset.close();
                    return tupleLivre;
                }
            }
        } catch(SQLException e) {
            throw new DAOException();
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
        getStmtInsert().setInt(1,
            idLivre);
        getStmtInsert().setString(2,
            titre);
        getStmtInsert().setString(3,
            auteur);
        getStmtInsert().setDate(4,
            Date.valueOf(dateAcquisition));
        getStmtInsert().executeUpdate();
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
        getStmtUpdate().setInt(1,
            idMembre);
        getStmtUpdate().setDate(2,
            Date.valueOf(datePret));
        getStmtUpdate().setInt(3,
            idLivre);
        return getStmtUpdate().executeUpdate();
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
        getStmtUpdate().setNull(1,
            Types.INTEGER);
        getStmtUpdate().setNull(2,
            Types.DATE);
        getStmtUpdate().setInt(3,
            idLivre);
        return getStmtUpdate().executeUpdate();
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
        getStmtDelete().setInt(1,
            idLivre);
        return getStmtDelete().executeUpdate();
    }

    // MERGE GESTION INTERROGATION SERVICE

    /**
     *
     * Affiche les livres contenant un mot dans le titre
     *
     * @param mot
     * @throws ServiceException
     */
    public void listerLivresTitre(String mot) throws ServiceException {

        try {
            getStmtLivresTitreMot().setString(1,
                mot);

            @SuppressWarnings("resource")
            // TODO fix warning
            ResultSet rset = getStmtLivresTitreMot().executeQuery();

            int idMembre;
            System.out.println("idLivre titre auteur idMembre dateRetour");
            while(rset.next()) {
                System.out.print(rset.getInt(1)
                    + " "
                    + rset.getString(2)
                    + " "
                    + rset.getString(3));
                idMembre = rset.getInt(4);
                if(!rset.wasNull()) {
                    System.out.print(" "
                        + idMembre
                        + " "
                        + rset.getDate(5));
                }
                System.out.println();
            }
            getCx().commit();
        } catch(ConnexionException connexionException) {
            throw new ServiceException(connexionException);
        } catch(SQLException sqlException) {
            throw new ServiceException(sqlException);
        }
    }

    /**
     *
     * Affiche tous les livres de la BD
     *
     * @throws ServiceException
     */
    @SuppressWarnings("resource")
    public void listerLivres() throws ServiceException {

        try {
            ResultSet rset;
            rset = getStmtListeTousLivres().executeQuery();

            System.out.println("idLivre titre auteur idMembre datePret");
            int idMembre;
            while(rset.next()) {
                System.out.print(rset.getInt("idLivre")
                    + " "
                    + rset.getString("titre")
                    + " "
                    + rset.getString("auteur"));
                idMembre = rset.getInt("idMembre");
                if(!rset.wasNull()) {
                    System.out.print(" "
                        + idMembre
                        + " "
                        + rset.getDate("datePret"));
                }
                System.out.println();
            }

            // TODO REMOVE THIS
            System.out.println(GET_ALL_REQUESTS
                + " "
                + FIND_BY_MEMBRE);

            getCx().commit();
        } catch(ConnexionException connexionException) {
            throw new ServiceException(connexionException);
        } catch(SQLException sqlException) {
            throw new ServiceException(sqlException);
        }
    }

    //Getter et Setter

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
     * Getter de la variable d'instance <code>this.stmtUpdate</code>.
     *
     * @return La variable d'instance <code>this.stmtUpdate</code>
     */
    private PreparedStatement getStmtUpdate() {
        return this.stmtUpdate;
    }

    /**
     * Setter de la variable d'instance <code>this.stmtUpdate</code>.
     *
     * @param stmtUpdate La valeur à utiliser pour la variable d'instance <code>this.stmtUpdate</code>
     */
    private void setStmtUpdate(PreparedStatement stmtUpdate) {
        this.stmtUpdate = stmtUpdate;
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

    /**
     * Getter de la variable d'instance <code>this.stmtLivresTitreMot</code>.
     *
     * @return La variable d'instance <code>this.stmtLivresTitreMot</code>
     */
    public PreparedStatement getStmtLivresTitreMot() {
        return this.stmtLivresTitreMot;
    }

    /**
     * Getter de la variable d'instance <code>this.stmtListeTousLivres</code>.
     *
     * @return La variable d'instance <code>this.stmtListeTousLivres</code>
     */
    public PreparedStatement getStmtListeTousLivres() {
        return this.stmtListeTousLivres;
    }

    /**
     * Setter de la variable d'instance <code>this.stmtLivresTitreMot</code>.
     *
     * @param stmtLivresTitreMot La valeur à utiliser pour la variable d'instance <code>this.stmtLivresTitreMot</code>
     */
    private void setStmtLivresTitreMot(PreparedStatement stmtLivresTitreMot) {
        this.stmtLivresTitreMot = stmtLivresTitreMot;
    }

    /**
     * Setter de la variable d'instance <code>this.stmtListeTousLivres</code>.
     *
     * @param stmtListeTousLivres La valeur à utiliser pour la variable d'instance <code>this.stmtListeTousLivres</code>
     */
    private void setStmtListeTousLivres(PreparedStatement stmtListeTousLivres) {
        this.stmtListeTousLivres = stmtListeTousLivres;
    }
}
