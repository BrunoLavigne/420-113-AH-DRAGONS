
package ca.qc.collegeahuntsic.bibliotheque.dao;

import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;

public class PretDAO extends DAO {

    private static final long serialVersionUID = 1L;

    private final static String ADD_REQUEST = "INSERT INTO pret (idPret, idMembre, idLivre, datePret, dateRetour) "
        + "VALUES (?,?,?,?)";

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
}
