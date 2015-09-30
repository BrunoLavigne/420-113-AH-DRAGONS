// Fichier MembreDAO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.PreparedStatement;
//import java.sql.ResultSet;
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

    private static final String ADD_REQUEST = "INSERT INTO membre (idMembre, nom, telephone, limitePret, nbPret) VALUES (?, ?, ?, ?, ?)";

    //private static final String READ_REQUEST = "SELECT idMembre, nom, telephone, limitePret, nbPret FROM membre WHERE idMembre = ?";

    //private static final String UDPATE_REQUEST = "UPDATE membre";

    private static final String DELETE_REQUEST = "DELETE FROM membre WHERE idMembre = ?";

    // private final static String UPDATE_REQUEST_ADD_PRET = "update membre set nbpret = nbPret + 1 where idMembre = ?";

    /**
     *
     * Création d'une instance. Pré-compilation d'énoncés SQL
     *
     * @param connexion
     * @throws DAOException s'il y a une erreur avec la base de données
     */
    public MembreDAO(Connexion connexion) throws DAOException {
        super(connexion);
    }

    /**
     *
     * Vérifie si un membre existe.
     *
     * @param idMembre
     * @return boolean existe
     * @throws DAOException
     */
    /*public boolean existe(int idMembre) throws DAOException {

        try {
            getStmtExiste().setInt(1,
                idMembre);

            try(
                ResultSet rset = getStmtExiste().executeQuery()) {
                boolean membreExiste = rset.next();
                rset.close();
                return membreExiste;
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }*/

    /**
     *
     * Lecture d'un membre.
     *
     * @param idMembre
     * @return TupleMembre le membre
     * @throws DAOException
     */

    /*public MembreDTO getMembre(int idMembre) throws DAOException {

        try {
            getStmtExiste().setInt(1,
                idMembre);
            try(
                ResultSet rset = getStmtExiste().executeQuery()) {
                if(rset.next()) {
                    MembreDTO tupleMembre = new MembreDTO();
                    tupleMembre.setIdMembre(idMembre);
                    tupleMembre.setNom(rset.getString(2));
                    tupleMembre.setTelephone(rset.getLong(3));
                    tupleMembre.setLimitePret(rset.getInt(4));
                    tupleMembre.setNbPret(rset.getInt(5));
                    rset.close();
                    return tupleMembre;
                }
            }
            return null;
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }

    }*/

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
    /*public void inscrire(int idMembre,
        String nom,
        long telephone,
        int limitePret) throws DAOException {

        try {

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
    }*/

    /**
     *
     * Incrémenter le nombre de prêts d'un membre.
     *
     * @param idMembre
     * @return int preter
     * @throws DAOException
     */
    /*public int preter(int idMembre) throws DAOException {

        try {
            getStmtUpdateIncrNbPret().setInt(1,
                idMembre);
            return getStmtUpdateIncrNbPret().executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }*/

    /**
     *
     * Décrémenter le nombre de prêts d'un membre.
     *
     * @param idMembre
     * @return int retourner
     * @throws DAOException
     */
    /*public int retourner(int idMembre) throws DAOException {

        try {
            getStmtUpdateDecNbPret().setInt(1,
                idMembre);
            return getStmtUpdateDecNbPret().executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }

    }*/

    /**
     *
     * Suppression d'un membre.
     *
     * @param idMembre
     * @return int desinscrire
     * @throws DAOException
     */
    /*public int desinscrire(int idMembre) throws DAOException {

        try {
            getStmtDelete().setInt(1,
                idMembre);
            return getStmtDelete().executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }*/

    /**
     *
     * Ajoute un membre à la table membre
     *
     * @param membreDTO
     * @throws DAOException S'il y a un problème avec la base de données
     */
    public void add(MembreDTO membreDTO) throws DAOException {

        try(
            PreparedStatement addPreparedStatement = getConnection().prepareStatement(ADD_REQUEST)) {
            addPreparedStatement.setInt(1,
                membreDTO.getIdMembre());
            addPreparedStatement.setString(2,
                membreDTO.getNom());
            addPreparedStatement.setLong(3,
                membreDTO.getTelephone());
            addPreparedStatement.setInt(4,
                membreDTO.getLimitePret());
            addPreparedStatement.setInt(5,
                membreDTO.getNbPret());
            addPreparedStatement.execute();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Supprime un membre de la table membre
     *
     * @param idMembre
     * @throws DAOException S'il y a un problème avec la base de données
     */
    public void delete(int idMembre) throws DAOException {

        try(
            PreparedStatement deletePreparedStatement = getConnection().prepareStatement(DELETE_REQUEST)) {
            deletePreparedStatement.setInt(1,
                idMembre);
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

}
