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
 *
 * DAO pour effectuer des CRUDs avec la table <code>livre</code>
 *
 * @author Dragons Vicieux
 */
public class LivreDAO extends DAO {

    private static final long serialVersionUID = 1L;

    private final static String READ_REQUEST = "SELECT idlivre, titre, auteur, dateAcquisition, idMembre, datePret "
        + "FROM livre "
        + "WHERE idlivre = ?";

    private final static String ADD_REQUEST = "INSERT into livre (idLivre, titre, auteur, dateAcquisition, idMembre, datePret) "
        + "VALUES (?,?,?,?,null,null)";

    private final static String UPDATE_REQUEST = "UPDATE livre set titre = ?, auteur = ?, dateAcquisition = ?, idMembre = ?, datePret = ? "
        + "WHERE idLivre = ?";

    private final static String DELETE_REQUEST = "DELETE from livre "
        + "WHERE idlivre = ?";

    private final static String GET_ALL_REQUEST = "SELECT idLivre, titre, auteur, idmembre, datePret "
        + "FROM livre";

    private final static String FIND_BY_TITRE = "SELECT idLivre, titre, auteur, idmembre, datePret + 14 "
        + "FROM LIVRE "
        + "WHERE LOWER(titre) LIKE LOWER(?)";

    private final static String FIND_BY_MEMBRE = "SELECT idLivre, titre, auteur, idmembre, datePret "
        + "FROM livre "
        + "WHERE idmembre = ?";

    private final static String EMPRUNT_REQUEST = "UPDATE livre "
        + "SET idMembre = ?, datePret = CURRENT_TIMESTAMP "
        + "WHERE idLivre = ?";

    private final static String RETOUR_REQUEST = "UPDATE livre "
        + "SET idMembre = null, datePret = null "
        + "WHERE idLivre = ?";

    /**
     *
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion - La connexion à utiliser.
     */
    public LivreDAO(Connexion connexion) {
        super(connexion);
    }

    /**
     *
     * Ajoute un nouveau livre.
     *
     * @param livreDTO - Le livre à ajouter.
     * @throws DAOException - S'il y a une erreur avec la base de données
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
            stmtAdd.setTimestamp(4,
                livreDTO.getDateAcquisition());
            stmtAdd.executeUpdate();

        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode()),
                sqlException);
        }
    }

    /**
     *
     * Lit un livre.
     *
     * @param idLivre - L'ID du livre à lire.
     * @return Le livre ou <code>null</code> si le livre n'existe pas
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public LivreDTO read(int idLivre) throws DAOException {
        LivreDTO tempLivre = null;
        try(
            PreparedStatement statementRead = (getConnection().prepareStatement(LivreDAO.READ_REQUEST))) {
            statementRead.setInt(1,
                idLivre);
            try(
                ResultSet rset = statementRead.executeQuery()) {
                if(rset.next()) {
                    tempLivre = new LivreDTO();
                    tempLivre.setIdLivre(idLivre);
                    tempLivre.setTitre(rset.getString(2));
                    tempLivre.setAuteur(rset.getString(3));
                    tempLivre.setDateAcquisition(rset.getTimestamp(4));
                    tempLivre.setIdMembre(rset.getInt(5));
                    tempLivre.setDatePret(rset.getTimestamp(6));
                    rset.close();
                }
                /*else {
                    throw new DAOException("DAO-0001");
                }
                 */
            }
        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode()),
                sqlException);
        }
        return tempLivre;
    }

    /**
     *
     * Met à jour un livre.
     *
     * @param livreDTO - Le livre à mettre à jour.
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public void update(LivreDTO livreDTO) throws DAOException {
        try(
            PreparedStatement updatePreparedStatement = getConnection().prepareStatement(LivreDAO.UPDATE_REQUEST)) {

            updatePreparedStatement.setInt(1,
                livreDTO.getIdLivre());
            updatePreparedStatement.setString(2,
                livreDTO.getTitre());
            updatePreparedStatement.setString(3,
                livreDTO.getAuteur());
            updatePreparedStatement.setTimestamp(4,
                livreDTO.getDateAcquisition());
            updatePreparedStatement.setInt(5,
                livreDTO.getIdMembre());
            updatePreparedStatement.setTimestamp(6,
                livreDTO.getDatePret());

            updatePreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode()),
                sqlException);
        }
    }

    /**
     *
     * Supprime un livre.
     *
     * @param livreDTO - Le livre à supprimer.
     * @throws DAOException - S'il y a une erreur avec la base de données.
     */
    public void delete(LivreDTO livreDTO) throws DAOException {
        try(
            PreparedStatement stmtDelete = (getConnection().prepareStatement(LivreDAO.DELETE_REQUEST))) {
            stmtDelete.setInt(1,
                livreDTO.getIdLivre());
            stmtDelete.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode()),
                sqlException);
        }
    }

    /**
     *
     * Trouve tous les livres.
     *
     * @return La liste des livres ; une liste vide sinon.
     *
     * @throws DAOException - S'il y a une erreur avec la base de données.
     */
    public List<LivreDTO> getAll() throws DAOException {
        List<LivreDTO> livres = Collections.<LivreDTO> emptyList();

        try(
            PreparedStatement statementGetAllLivres = (getConnection().prepareStatement(LivreDAO.GET_ALL_REQUEST))) {
            try(
                ResultSet resultSet = statementGetAllLivres.executeQuery()) {
                LivreDTO livreDTO = null;
                if(resultSet.next()) {
                    livres = new ArrayList<>();
                    do {
                        livreDTO = new LivreDTO();
                        livreDTO.setIdLivre(resultSet.getInt(1));
                        livreDTO.setTitre(resultSet.getString(2));
                        livreDTO.setAuteur(resultSet.getString(3));
                        livreDTO.setIdMembre(resultSet.getInt(4));
                        livreDTO.setDatePret(resultSet.getTimestamp(5));
                        livres.add(livreDTO);
                    } while(resultSet.next());
                }
                /*else {
                    throw new DAOException("DAO-0002");
                }
                 */
                return livres;
            } catch(SQLException sqlException) {
                throw new DAOException(Integer.toString(sqlException.getErrorCode()),
                    sqlException);
            }
        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode()),
                sqlException);
        }
    }

    /**
     *
     * Trouve les livres à partir d'un titre.
     *
     * @param titre - Le titre à utiliser
     * @return La liste des livres correspondants ; une liste vide sinon.
     * @throws DAOException - S'il y a une erreur avec la base de données.
     */
    public List<LivreDTO> findByTitre(String titre) throws DAOException {
        List<LivreDTO> liste = Collections.<LivreDTO> emptyList();
        try(
            PreparedStatement stmtGetLivresByTitre = (getConnection().prepareStatement(LivreDAO.FIND_BY_TITRE));) {
            stmtGetLivresByTitre.setString(1,
                "%"
                    + titre
                    + "%");
            try(
                ResultSet rset = stmtGetLivresByTitre.executeQuery()) {
                liste = new ArrayList<>();
                //boolean listIsEmpty = true;
                while(rset.next()) {
                    LivreDTO tempLivre = new LivreDTO();
                    tempLivre.setIdLivre(rset.getInt(1));
                    tempLivre.setTitre(rset.getString(2));
                    tempLivre.setAuteur(rset.getString(3));
                    tempLivre.setIdMembre(rset.getInt(4));
                    tempLivre.setDatePret(rset.getTimestamp(5));
                    liste.add(tempLivre);
                    //listIsEmpty = false;
                }
                /*
                if(listIsEmpty) {
                    throw new DAOException("DAO-0003");
                }
                 */
            }
        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode()),
                sqlException);
        }
        return liste;
    }

    /**
     *
     * Trouve les livres à partir d'un membre.
     *
     * @param membreDTO - Le membre à utiliser.
     * @return La liste des livres correspondants ; une liste vide sinon.
     * @throws DAOException - S'il y a une erreur avec la base de données.
     */
    public List<LivreDTO> findByMembre(MembreDTO membreDTO) throws DAOException {
        List<LivreDTO> liste = Collections.<LivreDTO> emptyList();
        try(
            PreparedStatement stmtGetLivresByMembre = getConnection().prepareStatement(LivreDAO.FIND_BY_MEMBRE);) {
            stmtGetLivresByMembre.setInt(1,
                membreDTO.getIdMembre());
            try(
                ResultSet rset = stmtGetLivresByMembre.executeQuery()) {
                liste = new ArrayList<>();
                //boolean listIsEmpty = true;
                while(rset.next()) {
                    LivreDTO tempLivre = new LivreDTO();
                    tempLivre.setIdLivre(rset.getInt(1));
                    tempLivre.setTitre(rset.getString(2));
                    tempLivre.setAuteur(rset.getString(3));
                    tempLivre.setIdMembre(rset.getInt(4));
                    tempLivre.setDatePret(rset.getTimestamp(5));
                    liste.add(tempLivre);
                    //listIsEmpty = false;
                }
                /*
                if(listIsEmpty) {
                    throw new DAOException("DAO-0003");
                }
                 */
            }
        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode()),
                sqlException);
        }
        return liste;
    }

    /**
     *
     * Vérifie l'existence d'un livre dans la base de données.
     *
     * @param idLivre L'ID du livre à rechercher.
     * @return <code>true</code>si le livre existe, <code>false</code> sinon.
     * @throws DAOException - S'il y a une erreur avec la base de données.
     */
    public boolean checkLivreExist(int idLivre) throws DAOException {
        return read(idLivre) != null;
    }

    /**
     *
     * Emprunte un livre.
     *
     * @param livreDTO - Le livre à emprunter.
     * @throws DAOException - S'il y a une erreur avec la base de données.
     */
    public void emprunter(LivreDTO livreDTO) throws DAOException {
        try(
            PreparedStatement updatePreparedStatement = getConnection().prepareStatement(LivreDAO.EMPRUNT_REQUEST)) {

            updatePreparedStatement.setInt(1,
                livreDTO.getIdMembre());
            updatePreparedStatement.setInt(2,
                livreDTO.getIdLivre());
            updatePreparedStatement.executeUpdate();

        } catch(NullPointerException NPException) {
            throw new DAOException("DAO-0004",
                NPException);
        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode()),
                sqlException);
        }
    }

    /**
     *
     * Retourne un livre.
     *
     * @param livreDTO - Le livre à retourner.
     * @throws DAOException - S'il y a une erreur avec la base de données.
     */
    public void retourner(LivreDTO livreDTO) throws DAOException {
        try(
            PreparedStatement retourPreparedStatement = getConnection().prepareStatement(LivreDAO.RETOUR_REQUEST)) {

            retourPreparedStatement.setInt(1,
                livreDTO.getIdLivre());
            retourPreparedStatement.executeUpdate();

        } catch(NullPointerException NPException) {
            throw new DAOException("DAO-0005",
                NPException);
        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode()),
                sqlException);
        }
    }
}