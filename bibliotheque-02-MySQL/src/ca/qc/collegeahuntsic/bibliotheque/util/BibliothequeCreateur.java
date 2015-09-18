
package ca.qc.collegeahuntsic.bibliotheque.util;

import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.exception.BiblioException;
import ca.qc.collegeahuntsic.bibliotheque.service.GestionInterrogationService;
import ca.qc.collegeahuntsic.bibliotheque.service.ReservationService;
import ca.qc.collegeahuntsic.bibliotheque.service.LivreService;
import ca.qc.collegeahuntsic.bibliotheque.service.MembreService;
import ca.qc.collegeahuntsic.bibliotheque.service.PretService;

/**
 * Système de gestion d'une bibliothèque
 *
 * Ce programme permet de gérer les transaction de base d'une
 * bibliothèque.  Il gère des livres, des membres et des
 * réservations. Les données sont conservées dans une base de
 * données relationnelles accédé avec JDBC.
 *
 *  <pre>
 * Pré-condition
 *   la base de données de la bibliothèque doit exister
 *  </pre>
 *
 *  <post>
 * Post-condition
 *   le programme effectue les majuscules associées à chaque transaction
 *  </post>
 */
public class BibliothequeCreateur {
    public Connexion cx;

    public LivreDAO livre;

    public MembreDAO membre;

    public ReservationDAO reservation;

    public LivreService gestionLivre;

    public MembreService gestionMembre;

    public PretService gestionPret;

    public ReservationService gestionReservation;

    public GestionInterrogationService gestionInterrogation;

    /**
     * Ouvre une connexion avec la BD relationnelle et
     * alloue les gestionnaires de transactions et des tables.
     *
     * @param serveur SQL
     * @param bd nom de la base de données
     * @param user id pour établir une connexion avec le serveur SQL
     * @param password mot de passe pour le user id
     *
     */
    public BibliothequeCreateur(String serveur,
        String bd,
        String user,
        String password) throws BiblioException,
        SQLException {
        // allocation des objets pour le traitement des transactions
        this.cx = new Connexion(serveur,
            bd,
            user,
            password);
        this.livre = new LivreDAO(this.cx);
        this.membre = new MembreDAO(this.cx);
        this.reservation = new ReservationDAO(this.cx);
        this.gestionLivre = new LivreService(this.livre,
            this.reservation);
        this.gestionMembre = new MembreService(this.membre,
            this.reservation);
        this.gestionPret = new PretService(this.livre,
            this.membre,
            this.reservation);
        this.gestionReservation = new ReservationService(this.livre,
            this.membre,
            this.reservation);
        this.gestionInterrogation = new GestionInterrogationService(this.cx);
    }

    /**
     * @throws SQLException
     */
    public void fermer() throws SQLException {
        // fermeture de la connexion
        this.cx.fermer();
    }
}
