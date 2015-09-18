
package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.BiblioException;

/**
 * Gestion des transactions reliées à la création et
 * la suppression de livres dans une bibliothèque.
 *
 * Ce programme permet de gérer les transaction reliées à la
 * création et à la suppression de livres.
 *
 *<pre>
 * Pré-condition:
 *   la base de données de la bibliothèque doit exister
 *</pre>
 *
 *<post>
 * Post-condition:
 *   le programme effectue les majuscules associées à chaque
 *   transaction
 * </post>
 *
 */

public class LivreService extends Services {

    private LivreDAO livre;

    private ReservationDAO reservation;

    private Connexion cx;

    //Création d'une instance
    public LivreService(LivreDAO livre,
        ReservationDAO reservation) {
        this.cx = livre.getConnexion();
        this.livre = livre;
        this.reservation = reservation;
    }

    /**
     * Ajout d'un nouveau livre dans la base de données.
     * S'il existe déjà, une exception est levée.
     * @param idLivre
     * @param titre
     * @param auteur
     * @param dateAcquisition
     * @throws SQLException
     * @throws BiblioException
     * @throws Exception
     */
    public void acquerir(int idLivre,
        String titre,
        String auteur,
        String dateAcquisition) throws SQLException,
        BiblioException,
        Exception {
        try {
            //Vérifie si le livre existe  déjà
            if(this.livre.existe(idLivre)) {
                throw new BiblioException("Le livre existe déjà: "
                    + idLivre);
            }

            //Ajout du livre dans la table livre
            this.livre.acquerir(idLivre,
                titre,
                auteur,
                dateAcquisition);
            this.cx.commit();
        } catch(Exception e) {
            //System.out.println(e);
            this.cx.rollback();
            throw e;
        }
    }

    /**
     * Vente d'un livre
     * @param idLivre
     * @throws SQLException
     * @throws BiblioException
     * @throws Exception
     */
    public void vendre(int idLivre) throws SQLException,
    BiblioException,
    Exception {
        try {
            LivreDTO tupleLivre = this.livre.getLivre(idLivre);
            if(tupleLivre == null) {
                throw new BiblioException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.idMembre != 0) {
                throw new BiblioException("Le livre est "
                    + idLivre
                    + " prêté à "
                    + tupleLivre.idMembre);
            }
            if(this.reservation.getReservationLivre(idLivre) != null) {
                throw new BiblioException("Le livre est "
                    + idLivre
                    + " réservé ");
            }

            // Suppression du livre
            int nb = this.livre.vendre(idLivre);
            if(nb == 0) {
                throw new BiblioException("Le livre est "
                    + idLivre
                    + " inexistant");
            }
            this.cx.commit();
        } catch(Exception e) {
            this.cx.rollback();
            throw e;
        }
    }
}
