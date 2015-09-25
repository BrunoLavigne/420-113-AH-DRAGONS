// Fichier MembreDAO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;

/**
 * Permet d'effectuer les accès à la table membre.
 * Cette classe gère tous les accès à la table membre.
 */

public class MembreDAO extends DAO {

    private static final long serialVersionUID = 1L;

    private PreparedStatement stmtExiste;

    private PreparedStatement stmtInsert;

    private PreparedStatement stmtUpdateIncrNbPret;

    private PreparedStatement stmtUpdateDecNbPret;

    private PreparedStatement stmtDelete;

    private final static String SELECT_REQUEST = "select idMembre, nom, telephone, limitePret, nbpret from membre where idmembre = ?";

    private final static String INSERT_REQUEST = "insert into membre (idmembre, nom, telephone, limitepret, nbpret) "
        + "values (?,?,?,?,0)";

    private final static String UPDATE_REQUEST = "update membre set nbpret = nbPret + 1 where idMembre = ?";

    private final static String SECOND_UPDATE_REQUEST = "update membre set nbpret = nbPret - 1 where idMembre = ?";

    private final static String DELETE_REQUEST = "delete from membre where idmembre = ?";
    
    // TODO FIX THIS
    @SuppressWarnings("unused")
    private final static String GET_ALL_REQUESTS = "select idMembre, nom, telephone, limitePret, nbpret from membre";

    private Connexion cx;

    /**
     *
     * Création d'une instance. Pré-compilation d'énoncés SQL
     *
     * @param cx
     * @throws DAOException
     */
    public MembreDAO(Connexion cx) throws DAOException {

        try {

            setCx(cx);
            setStmtExiste(getCx().getConnection().prepareStatement(SELECT_REQUEST));
            setStmtInsert(getCx().getConnection().prepareStatement(INSERT_REQUEST));
            setStmtUpdateIncrNbPret(getCx().getConnection().prepareStatement(UPDATE_REQUEST));
            setStmtUpdateDecNbPret(getCx().getConnection().prepareStatement(SECOND_UPDATE_REQUEST));
            setStmtDelete(getCx().getConnection().prepareStatement(DELETE_REQUEST));
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }

    }

    /**
     *
     * Retourner la connexion associée.
     *
     * @return La connexion
     */
    public Connexion getConnexion() {
        return this.cx;
    }

    /**
     *
     * Vérifie si un membre existe.
     *
     * @param idMembre
     * @return boolean existe
     * @throws DAOException
     */
    public boolean existe(int idMembre) throws DAOException {

        try {
            getStmtExiste().setInt(1,
                idMembre);

            @SuppressWarnings("resource")
            // TODO fix warning
            ResultSet rset = getStmtExiste().executeQuery();
            boolean membreExiste = rset.next();
            rset.close();
            return membreExiste;
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Lecture d'un membre.
     *
     * @param idMembre
     * @return TupleMembre le membre
     * @throws DAOException
     */

    // TODO fix warning
    @SuppressWarnings("resource")
    public MembreDTO getMembre(int idMembre) throws DAOException {

        try {
            this.stmtExiste.setInt(1,
                idMembre);
            ResultSet rset = this.stmtExiste.executeQuery();
            if(rset.next()) {
                MembreDTO tupleMembre = new MembreDTO();
                tupleMembre.setIdMembre(idMembre);
                tupleMembre.setNom(rset.getString(2));
                tupleMembre.setTelephone(rset.getLong(3));
                tupleMembre.setLimitePret(rset.getInt(4));
                tupleMembre.setNbPret(rset.getInt(5));
                return tupleMembre;
            }
            return null;
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }

    }

    /**
     *
     * Ajout d'un nouveau membre.
     *
     * @param idMembre
     * @param nom
     * @param telephone
     * @param limitePret
     * @throws DAOException
     */
    public void inscrire(int idMembre,
        String nom,
        long telephone,
        int limitePret) throws DAOException {

        try {
            /* Ajout du membre. */
            getStmtInsert().setInt(1,
                idMembre);
            getStmtInsert().setString(2,
                nom);
            getStmtInsert().setLong(3,
                telephone);
            getStmtInsert().setInt(4,
                limitePret);
            getStmtInsert().executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Incrémenter le nombre de prêts d'un membre.
     *
     * @param idMembre
     * @return int preter
     * @throws DAOException
     */
    public int preter(int idMembre) throws DAOException {

        try {
            getStmtUpdateIncrNbPret().setInt(1,
                idMembre);
            return getStmtUpdateIncrNbPret().executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Décrémenter le nombre de prêts d'un membre.
     *
     * @param idMembre
     * @return int retourner
     * @throws DAOException
     */
    public int retourner(int idMembre) throws DAOException {

        try {
            getStmtUpdateDecNbPret().setInt(1,
                idMembre);
            return getStmtUpdateDecNbPret().executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }

    }

    /**
     *
     * Suppression d'un membre.
     *
     * @param idMembre
     * @return int desinscrire
     * @throws DAOException
     */
    public int desinscrire(int idMembre) throws DAOException {

        try {
            getStmtDelete().setInt(1,
                idMembre);
            return getStmtDelete().executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

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
     * Getter de la variable d'instance <code>this.stmtUpdateIncrNbPret</code>.
     *
     * @return La variable d'instance <code>this.stmtUpdateIncrNbPret</code>
     */
    private PreparedStatement getStmtUpdateIncrNbPret() {
        return this.stmtUpdateIncrNbPret;
    }

    /**
     * Setter de la variable d'instance <code>this.stmtUpdateIncrNbPret</code>.
     *
     * @param stmtUpdateIncrNbPret La valeur à utiliser pour la variable d'instance <code>this.stmtUpdateIncrNbPret</code>
     */
    private void setStmtUpdateIncrNbPret(PreparedStatement stmtUpdateIncrNbPret) {
        this.stmtUpdateIncrNbPret = stmtUpdateIncrNbPret;
    }

    /**
     * Getter de la variable d'instance <code>this.stmtUpdateDecNbPret</code>.
     *
     * @return La variable d'instance <code>this.stmtUpdateDecNbPret</code>
     */
    private PreparedStatement getStmtUpdateDecNbPret() {
        return this.stmtUpdateDecNbPret;
    }

    /**
     * Setter de la variable d'instance <code>this.stmtUpdateDecNbPret</code>.
     *
     * @param stmtUpdateDecNbPret La valeur à utiliser pour la variable d'instance <code>this.stmtUpdateDecNbPret</code>
     */
    private void setStmtUpdateDecNbPret(PreparedStatement stmtUpdateDecNbPret) {
        this.stmtUpdateDecNbPret = stmtUpdateDecNbPret;
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
