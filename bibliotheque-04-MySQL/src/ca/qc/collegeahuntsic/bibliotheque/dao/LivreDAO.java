// Fichier LivreDAO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;

/**
 *
 * DAO pour effectuer des CRUDs avec la table <code>livre</code>
 *
 * @author Dragons Vicieux
 */
public class LivreDAO extends DAO {

    private static final long serialVersionUID = 1L;

    private static final int NOMBRE_SEMAINES_EN_RETARD = 4;

    private final static String READ_REQUEST = "SELECT idlivre, titre, auteur, dateAcquisition "
        + "FROM livre "
        + "WHERE idlivre = ?";

    private final static String ADD_REQUEST = "INSERT into livre (titre, auteur, dateAcquisition) "
        + "VALUES (?,?,?)";

    private final static String UPDATE_REQUEST = "UPDATE livre set titre = ?, auteur = ?, dateAcquisition = ? "
        + "WHERE idLivre = ?";

    private final static String DELETE_REQUEST = "DELETE from livre "
        + "WHERE idlivre = ?";

    private final static String GET_ALL_REQUEST = "SELECT idLivre, titre, auteur "
        + "FROM livre";

    private final static String FIND_BY_TITRE = "SELECT idLivre, titre, auteur "
        + "FROM LIVRE "
        + "WHERE LOWER(titre) LIKE LOWER(?)";

    private final static String FIND_PRETS_EN_RETARD = "SELECT idLivre, titre, auteur, idmembre, datePret "
        + "FROM livre "
        + "WHERE    datePret IS NOT NULL "
        + "AND      datePret < ? "
        + "ORDER BY datePret ASC";

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

            stmtAdd.setString(1,
                livreDTO.getTitre());
            stmtAdd.setString(2,
                livreDTO.getAuteur());
            stmtAdd.setTimestamp(3,
                livreDTO.getDateAcquisition());
            stmtAdd.executeUpdate();

        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode())
                + " "
                + sqlException.getMessage(),
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
                    rset.close();
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode())
                + " "
                + sqlException.getMessage(),
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

            updatePreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode())
                + " "
                + sqlException.getMessage(),
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
            throw new DAOException(Integer.toString(sqlException.getErrorCode())
                + " "
                + sqlException.getMessage(),
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
                        livres.add(livreDTO);
                    } while(resultSet.next());
                }
                return livres;
            } catch(SQLException sqlException) {
                throw new DAOException(Integer.toString(sqlException.getErrorCode())
                    + " "
                    + sqlException.getMessage(),
                    sqlException);
            }
        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode())
                + " "
                + sqlException.getMessage(),
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
                while(rset.next()) {
                    LivreDTO tempLivre = new LivreDTO();
                    tempLivre.setIdLivre(rset.getInt(1));
                    tempLivre.setTitre(rset.getString(2));
                    tempLivre.setAuteur(rset.getString(3));
                    liste.add(tempLivre);
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode())
                + " "
                + sqlException.getMessage(),
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
     * Trouve les livres dont le prêt est en retard
     *
     * @param dateJour - La date du jour
     * @return List<LivreDTO> - La liste des livres en retard
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public List<LivreDTO> findPretsEnRetard(Date dateJour) throws DAOException {

        // La liste contenant les livres en retard
        List<LivreDTO> livresRetard = Collections.<LivreDTO> emptyList();

        try(
            PreparedStatement statementGetAllLivresRetard = (getConnection().prepareStatement(LivreDAO.FIND_PRETS_EN_RETARD));) {

            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(dateJour);
            gregorianCalendar.add(Calendar.WEEK_OF_MONTH,
                -LivreDAO.NOMBRE_SEMAINES_EN_RETARD);

            Timestamp timestampRetard = new Timestamp(gregorianCalendar.getTimeInMillis());

            statementGetAllLivresRetard.setTimestamp(1,
                timestampRetard);

            try(
                ResultSet resultSet = statementGetAllLivresRetard.executeQuery()) {

                LivreDTO livreDTO = null;

                if(resultSet.next()) {
                    livresRetard = new ArrayList<>();
                    do {
                        livreDTO = new LivreDTO();
                        livreDTO.setIdLivre(resultSet.getInt(1));
                        livreDTO.setTitre(resultSet.getString(2));
                        livreDTO.setAuteur(resultSet.getString(3));
                        livresRetard.add(livreDTO);
                    } while(resultSet.next());
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode())
                + " "
                + sqlException.getMessage(),
                sqlException);
        }

        return livresRetard;

    }
}