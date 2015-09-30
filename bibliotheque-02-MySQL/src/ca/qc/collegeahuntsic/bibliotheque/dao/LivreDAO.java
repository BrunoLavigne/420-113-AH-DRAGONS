// Fichier LivreDAO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;

/**
 * Permet d'effectuer les accès à la table livre.
 */

public class LivreDAO extends DAO {

    private static final long serialVersionUID = 1L;

    private PreparedStatement stmtLivresTitreMot;

    private PreparedStatement stmtListeTousLivres;

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

    private final static String FIND_BY_MEMBRE = "select idMembre, nom, telephone, limitePret, nbpret from membre where idmembre = ?";

    /**
     *
     * Creation d'une instance. Des énoncés SQL pour chaque requête sont précompilés.
     *
     * @param cx
     * @throws DAOException
     */
    public LivreDAO(Connexion connexion) throws DAOException {
        super(connexion);

        try {

            // MERGE
            setStmtLivresTitreMot(getConnection().prepareStatement(FIND_BY_TITRE));

        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Met à jour un livre.
     *
     * @param livreDTO
     * @throws DAOException
     * @return livreDTO
     */
    public void update(LivreDTO livreDTO) throws DAOException {
        try(
            PreparedStatement updatePreparedStatement = getConnection().prepareStatement(UPDATE_REQUEST)) {

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
     * Lit un livre.
     *
     * @param livreDTO
     * @throws DAOException
     */
    public LivreDTO read(LivreDTO livreDTO) throws DAOException {
        try(
            PreparedStatement readPreparedStatement = getConnection().prepareStatement(READ_REQUEST)) {
            readPreparedStatement.setInt(1,
                livreDTO.getIdLivre());
            readPreparedStatement.execute();
            return livreDTO;
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
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
     * Méthode retournant une chaîne de caractères formatée comprenant les informations 
     * de base de tous les livres contenus dans la base de données.
     *
     * @return outputText un <code>String</code> contenant les informations sur les livres 
     * de la base de données organisées en colonnes.
     * @throws DAOException en cas d'erreur de connexion.
     */
    public String listerTousLesLivres() throws DAOException {
        String outputText = "";
        try(
            PreparedStatement stmtGetAllLivres = (getConnection().prepareStatement(LivreDAO.GET_ALL_REQUEST));
            ResultSet results = stmtGetAllLivres.executeQuery()) {
            ResultSetMetaData rsMeta = results.getMetaData();
            for(int i = 0 ; i < rsMeta.getColumnCount() ; i++) {
                outputText += rsMeta.getColumnName(i + 1)
                    + "\t";
            }
            outputText += "\n";
            while(results.next()) {
                for(int i = 0 ; i < rsMeta.getColumnCount() ; i++) {
                    outputText += results.getString(i + 1)
                        + "\t";
                }
                outputText += "\n";
            }
            return outputText;
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     *
     * Vérifie si un livre existe.
     *
     * @param idLivre
     * @return boolean existe
     * @throws DAOException
     */
    public boolean existe(int idLivre) throws DAOException {

        try {
            getStmtExiste().setInt(1,
                idLivre);

            try(
                ResultSet rset = getStmtExiste().executeQuery()) {
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
     * Lecture d'un livre
     *
     * @param idLivre
     * @return TupleLivre
     * @throws DAOException
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
     * Affiche les livres contenant un mot dans le titre
     *
     * @param motTitre la chaîne de caractères à rechercher dans les
     * titres des livres enregistrés dans la base de données
     * @throws DAOException en cas d'erreur de connexion ou de format d'objets ou d'enregistrements incompatibles.
     */
    public List<Object> listerLivresParTitre(String motTitre) throws DAOException {
        try(
            PreparedStatement stmtGetLivresByTitre = (getConnection().prepareStatement(LivreDAO.FIND_BY_TITRE));) {
            stmtGetLivresByTitre.setString(1,
                motTitre);
            try(
                ResultSet rset = getStmtLivresTitreMot().executeQuery()) {
                List<Object> liste = new ArrayList<>();
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
     * Ajout d'un nouveau livre dans la base de données.
     *
     * @param idLivre
     * @param titre
     * @param auteur
     * @param dateAcquisition
     * @throws DAOException
     */
    /*
    public void acquerir(int idLivre,
        String titre,
        String auteur,
        String dateAcquisition) throws DAOException {
        try {
            getStmtInsert().setInt(1,
                idLivre);
            getStmtInsert().setString(2,
                titre);
            getStmtInsert().setString(3,
                auteur);
            getStmtInsert().setDate(4,
                Date.valueOf(dateAcquisition));
            getStmtInsert().executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }
     */

    /**
     *
     * Enregistrement de l'emprunteur d'un livre.
     *
     * @param idLivre
     * @param idMembre
     * @param datePret
     * @return int preter
     * @throws DAOException
     */
    /*
    public int preter(int idLivre,
        int idMembre,
        String datePret) throws DAOException {
        try {
            getStmtUpdate().setInt(1,
                idMembre);
            getStmtUpdate().setDate(2,
                Date.valueOf(datePret));
            getStmtUpdate().setInt(3,
                idLivre);
            return getStmtUpdate().executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }
     */

    /**
     *
     * Rendre le livre disponible (non-prêté)
     *
     * @param idLivre
     * @return int retourner
     * @throws DAOException
     */
    /*
    public int retourner(int idLivre) throws DAOException {
        try {
            getStmtUpdate().setNull(1,
                Types.INTEGER);
            getStmtUpdate().setNull(2,
                Types.DATE);
            getStmtUpdate().setInt(3,
                idLivre);
            return getStmtUpdate().executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }
     */

    /**
     *
     * Suppression d'un livre
     *
     * @param idLivre
     * @return int vendre
     * @throws DAOException
     */
    /*
    public int vendre(int idLivre) throws DAOException {
        try {
            getStmtDelete().setInt(1,
                idLivre);
            return getStmtDelete().executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }
     */

    // MERGE GESTION INTERROGATION SERVICE
    /**
     *
     * Affiche tous les livres de la BD
     *
     * @throws DAOException
     */
    public void listerLivres() throws DAOException {

        try {
            try(
                ResultSet rset = getStmtListeTousLivres().executeQuery()) {
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

                getConnection().commit();
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    //Getter et Setter

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
}
