
package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;

public class PretDAO extends DAO {

    private static final long serialVersionUID = 1L;

    private final static String ADD_REQUEST = "INSERT INTO pret (idPret, idMembre, idLivre, datePret, dateRetour) "
        + "VALUES (?,?,?,?, NULL)";

    private final static String READ_REQUEST = "SELECT idPret, idMembre, idLivre, datePret, dateRetour "
        + "FROM pret "
        + "WHERE idPret = ?";

    private final static String UPDATE_REQUEST = "UPDATE pret SET idMembre = ?, idLivre = ?, datePret = ?, dateRetour = ? "
        + "WHERE idPret = ?";

    private final static String DELETE_REQUEST = "DELETE FROM pret "
        + "WHERE idPret = ?";

    private final static String GET_ALL_REQUEST = "SELECT idPret, idMembre, idLivre, datePret, dateRetour FROM pret ORDER BY datePret ASC";

    private final static String FIND_BY_MEMBRE = "SELECT idPret, idMembre, idLivre, datePret, dateRetour FROM pret WHERE idMembre = ? AND dateRetour IS NULL ORDER BY datePret ASC";

    private final static String FIND_BY_LIVRE = "SELECT idPret, idMembre, idLivre, datePret, dateRetour FROM pret WHERE idLivre = ? AND dateRetour IS NULL ORDER BY datePret ASC";

    private final static String FIND_BY_DATE_PRET = "SELECT idPret, idMembre, idLivre, datePret, dateRetour FROM pret WHERE datePret = ? AND dateRetour IS NULL ORDER BY datePret ASC";

    private final static String FIND_BY_DATE_RETOUR = "SELECT idPret, idMembre, idLivre, datePret, dateRetour FROM pret WHERE dateRetour = ? AND dateRetour IS NULL ORDER BY datePret ASC";

    /**
     *
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion - La connexion à utiliser
     */
    public PretDAO(Connexion connexion) {
        super(connexion);
        System.out.println(ADD_REQUEST
            + READ_REQUEST
            + UPDATE_REQUEST
            + DELETE_REQUEST
            + GET_ALL_REQUEST
            + FIND_BY_MEMBRE
            + FIND_BY_LIVRE
            + FIND_BY_DATE_PRET
            + FIND_BY_DATE_RETOUR);
    }

    /**
     *
     * Ajoute un nouveau prêt.
     *
     * @param pretDTO - Le prêt à ajouter.
     * @throws DAOException - S'il y a une erreur avec la base de données.
     */
    public void add(PretDTO pretDTO) throws DAOException {
        try(
            PreparedStatement statementAdd = (getConnection().prepareStatement(PretDAO.ADD_REQUEST))) {
            statementAdd.setInt(1,
                pretDTO.getIdPret());
            statementAdd.setInt(2,
                pretDTO.getMembreDTO().getIdMembre());
            statementAdd.setInt(3,
                pretDTO.getLivreDTO().getIdLivre());
            statementAdd.setTimestamp(4,
                pretDTO.getDatePret());
            statementAdd.executeUpdate();

        } catch(SQLException sqlException) {
            throw new DAOException(sqlException.getMessage(),
                sqlException);
        }
    }

    /**
     *
     * Lit un prêt.
     *
     * @param idPret - L'ID du prêt à lire.
     * @return Le prêt ou <code>null</code> si le prêt n'existe pas
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    // TODO: check javadoc: retirer le @return ?
    public PretDTO read(int idPret) throws DAOException {
        PretDTO tempPret = null;
        try(
            PreparedStatement statementRead = (getConnection().prepareStatement(PretDAO.READ_REQUEST))) {
            statementRead.setInt(1,
                idPret);
            try(
                ResultSet rset = statementRead.executeQuery()) {
                if(rset.next()) {

                    tempPret = new PretDTO();
                    tempPret.setIdPret(rset.getInt(1));
                    MembreDTO membre = new MembreDTO();
                    membre.setIdMembre(rset.getInt(2));
                    tempPret.setMembreDTO(membre);
                    LivreDTO livre = new LivreDTO();
                    livre.setIdLivre(rset.getInt(3));
                    tempPret.setLivreDTO(livre);
                    tempPret.setDatePret(rset.getTimestamp(4));
                    tempPret.setDateRetour(rset.getTimestamp(5));
                    rset.close();
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode())
                + " "
                + sqlException.getMessage(),
                sqlException);
        }
        return tempPret;
    }

    /**
     *
     * Met à jour un prêt.
     *
     * @param pretDTO - Le prêt à mettre à jour.
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public void update(PretDTO pretDTO) throws DAOException {
        try(
            PreparedStatement updatePreparedStatement = getConnection().prepareStatement(PretDAO.UPDATE_REQUEST)) {
            updatePreparedStatement.setInt(1,
                pretDTO.getMembreDTO().getIdMembre());
            updatePreparedStatement.setInt(2,
                pretDTO.getLivreDTO().getIdLivre());
            updatePreparedStatement.setTimestamp(3,
                pretDTO.getDatePret());
            updatePreparedStatement.setTimestamp(4,
                pretDTO.getDateRetour());
            updatePreparedStatement.setInt(5,
                pretDTO.getIdPret());

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
     * Supprime un prêt.
     *
     * @param pretDTO - Le prêt à supprimer.
     * @throws DAOException - S'il y a une erreur avec la base de données.
     */
    public void delete(PretDTO pretDTO) throws DAOException {
        try(
            PreparedStatement statementDelete = (getConnection().prepareStatement(PretDAO.DELETE_REQUEST))) {
            statementDelete.setInt(1,
                pretDTO.getIdPret());
            statementDelete.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(Integer.toString(sqlException.getErrorCode())
                + " "
                + sqlException.getMessage(),
                sqlException);
        }
    }

    /**
     *
     * Trouve tous les prêts.
     *
     * @return La liste des prêts ; une liste vide sinon.
     *
     * @throws DAOException - S'il y a une erreur avec la base de données.
     */
    public List<PretDTO> getAll() throws DAOException {
        List<PretDTO> prets = Collections.<PretDTO> emptyList();

        try(
            PreparedStatement statementGetAllPrets = (getConnection().prepareStatement(PretDAO.GET_ALL_REQUEST))) {
            try(
                ResultSet resultSet = statementGetAllPrets.executeQuery()) {
                PretDTO pretDTO = null;
                if(resultSet.next()) {
                    prets = new ArrayList<>();
                    do {

                        pretDTO = new PretDTO();
                        pretDTO.setIdPret(resultSet.getInt(1));

                        MembreDTO membre = new MembreDTO();
                        membre.setIdMembre(resultSet.getInt(2));

                        LivreDTO livre = new LivreDTO();
                        livre.setIdLivre(resultSet.getInt(3));

                        pretDTO.setMembreDTO(membre);
                        pretDTO.setLivreDTO(livre);
                        pretDTO.setDatePret(resultSet.getTimestamp(4));
                        pretDTO.setDateRetour(resultSet.getTimestamp(5));

                        prets.add(pretDTO);

                    } while(resultSet.next());
                }
                return prets;
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
     * Trouve les prêts non terminés d'un membre.
     *
     * @param membreDTO - Le membre à utiliser
     * @return La liste des prêts correspondants ; une liste vide sinon.
     * @throws DAOException - S'il y a une erreur avec la base de données.
     */
    public List<PretDTO> findByMembre(MembreDTO membreDTO) throws DAOException {
        List<PretDTO> liste = Collections.<PretDTO> emptyList();
        try(
            PreparedStatement stmtGetPretsByMembre = (getConnection().prepareStatement(PretDAO.FIND_BY_MEMBRE));) {
            stmtGetPretsByMembre.setInt(1,
                membreDTO.getIdMembre());
            try(
                ResultSet rset = stmtGetPretsByMembre.executeQuery()) {
                liste = new ArrayList<>();

                while(rset.next()) {

                    PretDTO tempPret = new PretDTO();
                    tempPret.setIdPret(rset.getInt(1));

                    MembreDTO membre = new MembreDTO();
                    membre.setIdMembre(rset.getInt(2));

                    LivreDTO livre = new LivreDTO();
                    livre.setIdLivre(rset.getInt(3));

                    tempPret.setMembreDTO(membre);
                    tempPret.setLivreDTO(livre);
                    tempPret.setDatePret(rset.getTimestamp(4));
                    tempPret.setDateRetour(rset.getTimestamp(5));

                    liste.add(tempPret);
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
     * Trouve les livres en cours d'emprunt.
     *
     * @param livreDTO - Le livre à utiliser
     * @return La liste des prêts correspondants ; une liste vide sinon.
     * @throws DAOException - S'il y a une erreur avec la base de données.
     */
    public List<PretDTO> findByLivre(LivreDTO livreDTO) throws DAOException {
        List<PretDTO> liste = Collections.<PretDTO> emptyList();
        try(
            PreparedStatement stmtGetPretsByLivre = (getConnection().prepareStatement(PretDAO.FIND_BY_LIVRE));) {
            stmtGetPretsByLivre.setInt(1,
                livreDTO.getIdLivre());
            try(
                ResultSet rset = stmtGetPretsByLivre.executeQuery()) {

                liste = new ArrayList<>();

                while(rset.next()) {

                    PretDTO tempPret = new PretDTO();
                    tempPret.setIdPret(rset.getInt(1));

                    MembreDTO membre = new MembreDTO();
                    membre.setIdMembre(rset.getInt(2));

                    LivreDTO livre = new LivreDTO();
                    livre.setIdLivre(rset.getInt(3));

                    tempPret.setMembreDTO(membre);
                    tempPret.setLivreDTO(livre);
                    tempPret.setDatePret(rset.getTimestamp(4));
                    tempPret.setDateRetour(rset.getTimestamp(5));
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
     * Trouve les prêts à partir d'une date de prêt.
     *
     * @param datePret - La date de prêt à trouver.
     * @return La liste des prêts correspondants ; une liste vide sinon.
     * @throws DAOException - S'il y a une erreur avec la base de données.
     */
    public List<PretDTO> findByDatePret(Timestamp datePret) throws DAOException {
        List<PretDTO> liste = Collections.<PretDTO> emptyList();
        try(
            // TODO passer d'un timestamp exact à une date???
            PreparedStatement statementGetPretsByDatePret = (getConnection().prepareStatement(PretDAO.FIND_BY_DATE_PRET));) {
            statementGetPretsByDatePret.setTimestamp(1,
                datePret);
            try(
                ResultSet rset = statementGetPretsByDatePret.executeQuery()) {

                liste = new ArrayList<>();

                while(rset.next()) {

                    PretDTO tempPret = new PretDTO();
                    tempPret.setIdPret(rset.getInt(1));

                    MembreDTO membre = new MembreDTO();
                    membre.setIdMembre(rset.getInt(2));

                    LivreDTO livre = new LivreDTO();
                    livre.setIdLivre(rset.getInt(3));

                    tempPret.setMembreDTO(membre);
                    tempPret.setLivreDTO(livre);
                    tempPret.setDatePret(rset.getTimestamp(4));
                    tempPret.setDateRetour(rset.getTimestamp(5));

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
     * Trouve les prêts à partir d'une date de retour.
     *
     * @param dateRetour - La date de retour à trouver.
     * @return La liste des prêts correspondants ; une liste vide sinon.
     * @throws DAOException - S'il y a une erreur avec la base de données.
     */
    public List<PretDTO> findByDateRetour(Timestamp dateRetour) throws DAOException {
        List<PretDTO> liste = Collections.<PretDTO> emptyList();
        try(
            // TODO passer d'un timestamp exact à une date???
            PreparedStatement statementGetPretsByDateReour = (getConnection().prepareStatement(PretDAO.FIND_BY_DATE_RETOUR));) {
            statementGetPretsByDateReour.setTimestamp(1,
                dateRetour);
            try(
                ResultSet rset = statementGetPretsByDateReour.executeQuery()) {
                liste = new ArrayList<>();
                while(rset.next()) {

                    PretDTO tempPret = new PretDTO();
                    tempPret.setIdPret(rset.getInt(1));

                    MembreDTO membre = new MembreDTO();
                    membre.setIdMembre(rset.getInt(2));

                    LivreDTO livre = new LivreDTO();
                    livre.setIdLivre(rset.getInt(3));

                    tempPret.setMembreDTO(membre);
                    tempPret.setLivreDTO(livre);
                    tempPret.setDatePret(rset.getTimestamp(4));
                    tempPret.setDateRetour(rset.getTimestamp(5));

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
}
