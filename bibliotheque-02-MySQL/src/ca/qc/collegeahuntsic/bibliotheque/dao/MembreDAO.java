// Fichier MembreDAO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;

/**
 * Permet d'effectuer les accès à la table membre. Cette classe gère tous les
 * accès à la table membre.
 */

public class MembreDAO extends DAO {

    private static final long serialVersionUID = 1L;

    private static final String INSERT_REQUEST = "INSERT INTO membre (idMembre, nom, telephone, limitePret, nbPret) VALUES (?, ?, ?, ?, ?)";

    private static final String READ_REQUEST = "SELECT idMembre, nom, telephone, limitePret, nbPret FROM membre WHERE idMembre = ?";

    private static final String UPDATE_REQUEST = "UPDATE membre SET nom = ?, telephone = ?, limitePret = ?, nbPret = ? WHERE idMembre = ?";

    private static final String DELETE_REQUEST = "DELETE FROM membre WHERE idMembre = ?";

    private final static String GET_ALL_REQUEST = "select idMembre, nom, telephone, limitePret, nbPret "
        + "from livre";

    /**
     *
     * Création d'une instance. Pré-compilation d'énoncés SQL
     *
     * @param connexion La connexion à la BD
     * @throws DAOException S'il y a un problème avec la base de données
     */
    public MembreDAO(Connexion connexion) throws DAOException {
        super(connexion);
    }

    /**
     *
     * Ajoute un membre à la table membre
     *
     * @param membreDTO Le membre à ajouter
     * @throws DAOException S'il y a un problème avec la base de données
     */
    public void add(MembreDTO membreDTO) throws DAOException {

        try(
            PreparedStatement addPreparedStatement = getConnection().prepareStatement(INSERT_REQUEST)) {
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
     * Lit et retourne un membre de la table membre
     *
     * @param idMembre L'ID du membre recherché
     * @return MembreDTO Le membre en objet <code>MembreDTO</code> dont L'ID est spécifié, null si rien de trouvé
     * @throws DAOException S'il y a un problème avec la base de données
     */
    public MembreDTO read(int idMembre) throws DAOException {
        MembreDTO membreDTO = null;
        try(
            PreparedStatement readPreparedStatement = getConnection().prepareStatement(MembreDAO.READ_REQUEST)) {
            readPreparedStatement.setInt(1,
                idMembre);
            try(
                ResultSet resultSet = readPreparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    membreDTO = new MembreDTO();
                    membreDTO.setIdMembre(resultSet.getInt(1));
                    membreDTO.setNom(resultSet.getString(2));
                    membreDTO.setTelephone(resultSet.getLong(3));
                    membreDTO.setLimitePret(resultSet.getInt(4));
                    membreDTO.setNbPret(resultSet.getInt(6));

                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return membreDTO;
    }

    /**
     *
     * Met les informations d'un membre à jour (en se basant sur son ID)
     *
     * @param membreDTO Le membre à mettre à jour
     * @throws DAOException S'il y a un problème avec la base de données
     */
    public void update(MembreDTO membreDTO) throws DAOException {

        try(
            PreparedStatement updatePreparedStatement = getConnection().prepareStatement(UPDATE_REQUEST)) {
            updatePreparedStatement.setString(1,
                membreDTO.getNom());
            updatePreparedStatement.setLong(2,
                membreDTO.getTelephone());
            updatePreparedStatement.setInt(3,
                membreDTO.getLimitePret());
            updatePreparedStatement.setInt(4,
                membreDTO.getNbPret());
            updatePreparedStatement.setInt(5,
                membreDTO.getIdMembre());
            updatePreparedStatement.execute();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Supprime un membre de la table membre
     *
     * @param idMembre L'ID du membre à supprimer
     * @throws DAOException S'il y a un problème avec la base de données
     */
    public void delete(int idMembre) throws DAOException {

        try(
            PreparedStatement deletePreparedStatement = getConnection().prepareStatement(DELETE_REQUEST)) {
            deletePreparedStatement.setInt(1,
                idMembre);
            deletePreparedStatement.execute();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Méthode retournant une liste de type <code>List</code> contenant des
     * objets <code>MembreDTO</code>. La liste contient tous les membres
     * enregistrés dans la base de données.
     *
     * @return liste une liste d'objets de type <code>MembreDTO</code> représentant les membres enregistrés dans la table
     * @throws DAOException S'il y a un problème avec la base de données
     */
    public List<MembreDTO> getAll() throws DAOException {

        List<MembreDTO> liste = new ArrayList<>();
        try(
            PreparedStatement stmtGetAllMembres = (getConnection().prepareStatement(MembreDAO.GET_ALL_REQUEST));
            ResultSet results = stmtGetAllMembres.executeQuery()) {
            try(
                ResultSet resultSet = stmtGetAllMembres.executeQuery()) {
                while(resultSet.next()) {
                    MembreDTO tempMembre = new MembreDTO();
                    tempMembre.setIdMembre(resultSet.getInt(1));
                    tempMembre.setNom(resultSet.getString(2));
                    tempMembre.setTelephone(resultSet.getLong(3));
                    tempMembre.setLimitePret(resultSet.getInt(4));
                    tempMembre.setNbPret(resultSet.getInt(5));
                    liste.add(tempMembre);
                }
            }
            return liste;
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }
}
