// Fichier LivreDAO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;

/**
 * Permet d'effectuer les accès à la table livre.
 */

public class LivreDAO extends DAO {

    private static final long serialVersionUID = 1L;

    private final static String READ_REQUEST = "select idlivre, titre, auteur, dateAcquisition, idMembre, datePret from livre where idlivre = ?";

    private final static String ADD_REQUEST = "insert into livre (idLivre, titre, auteur, dateAcquisition, idMembre, datePret) "
        + "values (?,?,?,?,null,null)";

    private final static String UPDATE_REQUEST = "update livre set idMembre = ?, datePret = ? "
        + "where idLivre = ?";

    private final static String DELETE_REQUEST = "delete from livre where idlivre = ?";

    private final static String GET_ALL_REQUEST = "select idLivre, titre, auteur, idmembre, datePret "
        + "from livre";

    private final static String FIND_BY_TITRE = "SELECT idLivre, titre, auteur, idmembre, datePret + 14 "
        + "FROM LIVRE "
        + "WHERE LOWER(titre) LIKE %?%";

    private final static String FIND_BY_MEMBRE = "SELECT idLivre, titre, auteur, idmembre, datePret from livre where idmembre = ?";

    /**
     *
     * Creation d'une instance. Des énoncés SQL pour chaque requête sont précompilés.
     *
     * @param connexion
     * @throws DAOException
     */
    public LivreDAO(Connexion connexion) throws DAOException {
        super(connexion);
    }

    /**
     *
     * Met à jour un livre.
     *
     * @param livreDTO
     * @throws DAOException S'il y a une erreur avec la base de donnée
     * @return livreDTO
     */
    public void update(LivreDTO livreDTO) throws DAOException {
        try(
            PreparedStatement updatePreparedStatement = getConnection().prepareStatement(LivreDAO.UPDATE_REQUEST)) {
            updatePreparedStatement.setInt(1,
                livreDTO.getIdMembre());
            updatePreparedStatement.setDate(2,
                livreDTO.getDatePret());
            updatePreparedStatement.execute();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Lecture d'un livre
     *
     * @param idLivre
     * @return TupleLivre
     * @throws DAOException
     */
    public LivreDTO read(int idLivre) throws DAOException {
        try(
            PreparedStatement stmtExistCheck = (getConnection().prepareStatement(LivreDAO.READ_REQUEST))) {
            stmtExistCheck.setInt(1,
                idLivre);
            try(
                ResultSet rset = stmtExistCheck.executeQuery()) {
                if(rset.next()) {
                    LivreDTO tempLivre = new LivreDTO();
                    tempLivre.setIdLivre(idLivre);
                    tempLivre.setTitre(rset.getString(2));
                    tempLivre.setAuteur(rset.getString(3));
                    tempLivre.setDateAcquisition(rset.getDate(4));
                    tempLivre.setIdMembre(rset.getInt(5));
                    tempLivre.setDatePret(rset.getDate(6));
                    rset.close();
                    return tempLivre;
                }
            }
        } catch(SQLException e) {
            throw new DAOException();
        }
        return null;
    }

    /**
     *
     * Méthode permettant d'ajouter un livre à la base de données.
     *
     * @param livreDTO Le livre à ajouter
     * @throws DAOException en cas d'erreur de connexion ou d'objet <code>LivreDTO</code> incomplet.
     */
    public void add(LivreDTO livreDTO) throws DAOException {
        try(
            PreparedStatement stmtAdd = (getConnection().prepareStatement(LivreDAO.ADD_REQUEST))) {
            stmtAdd.setInt(1,
                livreDTO.getIdLivre());
            stmtAdd.setString(2,
                livreDTO.getTitre());
            stmtAdd.setString(3,
                livreDTO.getAuteur());
            stmtAdd.setDate(4,
                livreDTO.getDateAcquisition());
            stmtAdd.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Méthode permettant de retirer un livre de la base de données.
     *
     * @param livreDTO le livre à effacer
     * @throws DAOException en cas d'erreur de connexion ou d'objet <code>LivreDTO</code> incomplet.
     */
    public void delete(LivreDTO livreDTO) throws DAOException {
        try(
            PreparedStatement stmtDelete = (getConnection().prepareStatement(LivreDAO.DELETE_REQUEST))) {
            stmtDelete.setInt(1,
                livreDTO.getIdLivre());
            stmtDelete.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Méthode retournant une liste de type <code>List</code> contenant des objets <code>LivreDTO</code>.
     * La liste contient tous les livres enregistrés dans la base de données.
     *
     * @return liste une liste d'objets de type <code>LivreDTO</code> représentant
     * les livres enregistrés dans la base de données
     *
     * @throws DAOException en cas d'erreur de connexion ou d'erreur SQL.
     */
    public List<LivreDTO> getAll() throws DAOException {
        List<LivreDTO> livres = Collections.EMPTY_LIST;

        try(
            PreparedStatement stmtGetAllLivres = (getConnection().prepareStatement(LivreDAO.GET_ALL_REQUEST))) {
            try(
                ResultSet resultSet = stmtGetAllLivres.executeQuery()) {
                LivreDTO livreDTO = null;
                if(resultSet.next()) {
                    livres = new ArrayList<>();
                    do {
                        livreDTO = new LivreDTO();
                        livreDTO.setIdLivre(resultSet.getInt(1));
                        livreDTO.setTitre(resultSet.getString(2));
                        livreDTO.setAuteur(resultSet.getString(3));
                        livreDTO.setIdMembre(resultSet.getInt(4));
                        livreDTO.setDatePret(resultSet.getDate(5));
                        livres.add(livreDTO);
                    } while(resultSet.next());
                }

                return livres;
            } catch(SQLException sqlException) {
                throw new DAOException(sqlException);
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Méthode retournant une <code>List</code> de <code>LivreDTO</code> contenant les livres dont le titre contient un mot clé recherché.
     *
     * @param motTitre la chaîne de caractères à rechercher dans les
     * titres des livres enregistrés dans la base de données
     * @throws DAOException en cas d'erreur de connexion ou de format d'objets ou d'enregistrements incompatibles.
     */
    public List<LivreDTO> findByTitre(String motTitre) throws DAOException {
        try(
            PreparedStatement stmtGetLivresByTitre = (getConnection().prepareStatement(LivreDAO.FIND_BY_TITRE));) {
            stmtGetLivresByTitre.setString(1,
                motTitre);
            try(
                ResultSet rset = stmtGetLivresByTitre.executeQuery()) {
                List<LivreDTO> liste = new ArrayList<>();
                while(rset.next()) {
                    LivreDTO tempLivre = new LivreDTO();
                    tempLivre.setIdLivre(rset.getInt(1));
                    tempLivre.setTitre(rset.getString(2));
                    tempLivre.setAuteur(rset.getString(3));
                    tempLivre.setIdMembre(rset.getInt(4));
                    tempLivre.setDatePret(rset.getDate(5));
                    liste.add(tempLivre);
                }
                return liste;
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Méthode retournant une <code>List</code> de <code>LivreDTO</code> contenant les livres associés à un membre particulier.
     *
     * @param idMembre un <code>int</code> représentant le numéro d'identification du membre dont les livres sont recherchés.
     * @return liste un objet <code>List</code> contenant des objets de type <code>LivreDTO</code>.
     * @throws DAOException d'erreur de connexion ou de format d'objets ou d'enregistrements incompatibles.
     */
    public List<LivreDTO> findByMembre(MembreDTO membreDTO) throws DAOException {
        try(
            PreparedStatement stmtGetLivresByMembre = (getConnection().prepareStatement(LivreDAO.FIND_BY_MEMBRE));) {
            stmtGetLivresByMembre.setInt(1,
                membreDTO.getIdMembre());
            try(
                ResultSet rset = stmtGetLivresByMembre.executeQuery()) {
                List<LivreDTO> liste = new ArrayList<>();
                while(rset.next()) {
                    LivreDTO tempLivre = new LivreDTO();
                    tempLivre.setIdLivre(rset.getInt(1));
                    tempLivre.setTitre(rset.getString(2));
                    tempLivre.setAuteur(rset.getString(3));
                    tempLivre.setIdMembre(rset.getInt(4));
                    tempLivre.setDatePret(rset.getDate(5));
                    liste.add(tempLivre);
                }
                return liste;
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Vérifie l'existence d'un enregistrement Livre dans la base de données.
     *
     * @param idLivre un <code>int</code> représentant le numéro d'identification du livre à rechercher.
     * @return boolean livreExiste un bouléen. VRAI si le livre existe, FAUX s'il n'est pas trouvé.
     * @throws DAOException en cas d'erreur de connexion ou si <code>idLivre</code> n'est pas valide.
     */
    public boolean checkLivreExist(int idLivre) throws DAOException {
        try(
            PreparedStatement stmtExistCheck = (getConnection().prepareStatement(LivreDAO.READ_REQUEST))) {
            stmtExistCheck.setInt(1,
                idLivre);
            try(
                ResultSet rset = stmtExistCheck.executeQuery()) {
                boolean livreExiste = rset.next();
                rset.close();
                return livreExiste;
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Méthode permettant de mettre à jour le livre emprunté dans la base de donnée. Appelle la méthode
     * <code>update</code> de la classe <code>LivreDAO</code>.
     *
     * @param LivreDTO L'objet <code>LivreDTO</code> représentant le livre à emprunter.
     * @throws DAOException en cas d'erreur dans la mise à jour du livre dans la base de données.
     */
    public void emprunter(LivreDTO LivreDTO) throws DAOException {
        try {
            update(LivreDTO);
        } catch(DAOException daoexception) {
            throw new DAOException(daoexception);
        }
    }

    /**
     *
     * Méthode permettant de mettre à jour le livre retourné dans la base de donnée. Appelle la méthode
     * <code>update</code> de la classe <code>LivreDAO</code>.
     *
     * @param LivreDTO L'objet <code>LivreDTO</code> représentant le livre à retourner.
     * @throws DAOException en cas d'erreur dans la mise à jour du livre dans la base de données.
     */
    public void retourner(LivreDTO LivreDTO) throws DAOException {
        try {
            // En théorie, si le livre passé en paramètre a comme valeurs de
            // idMembre et datePret null, ça devrait le "setter comme non-prêté, non?
            // TODO check si ça raise pas des NullPointerException dans update(LivreDTO)
            update(LivreDTO);
        } catch(DAOException daoexception) {
            throw new DAOException(daoexception);
        }
    }
}
