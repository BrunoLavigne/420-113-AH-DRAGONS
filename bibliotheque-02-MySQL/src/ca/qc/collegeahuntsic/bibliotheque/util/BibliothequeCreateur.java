// Fichier BibliothequeCreateur.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.util;

import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.exception.BibliothequeException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ConnexionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;
import ca.qc.collegeahuntsic.bibliotheque.service.LivreService;
import ca.qc.collegeahuntsic.bibliotheque.service.MembreService;
import ca.qc.collegeahuntsic.bibliotheque.service.PretService;
import ca.qc.collegeahuntsic.bibliotheque.service.ReservationService;

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
    private Connexion cx;

    private LivreDAO livre;

    private MembreDAO membre;

    private ReservationDAO reservation;

    private LivreService gestionLivre;

    private MembreService gestionMembre;

    private PretService gestionPret;

    private ReservationService gestionReservation;

    /**
     * Crée les services nécessaires à l'application bibliothèque.
     *
     * @param typeServeur Type de serveur SQL de la BD
     * @param schema Nom du schéma de la base de données
     * @param nomUtilisateur Nom d'utilisateur sur le serveur SQL
     * @param motPasse Mot de passe sur le serveur SQL
     * @throws BibliothequeException S'il y a une erreur avec la base de données
     *
     */
    public BibliothequeCreateur(String typeServeur,
        String schema,
        String nomUtilisateur,
        String motPasse) throws BibliothequeException {
        // allocation des objets pour le traitement des transaction

        try(
            Connexion connexion = new Connexion(typeServeur,
                schema,
                nomUtilisateur,
                motPasse)) {

            setCx(connexion);

            LivreDAO livreDAO = new LivreDAO(getCx());
            MembreDAO membreDAO = new MembreDAO(getCx());
            ReservationDAO reservationDAO = new ReservationDAO(getCx());

            setLivre(livreDAO);
            setMembre(membreDAO);
            setReservation(reservationDAO);

            setGestionLivre(new LivreService(getLivre(),
                getMembre(),
                getReservation()));
            setGestionMembre(new MembreService(getMembre(),
                getLivre(),
                getReservation()));
            setGestionPret(new PretService(getLivre(),
                getMembre(),
                getReservation()));
            setGestionReservation(new ReservationService(getLivre(),
                getMembre(),
                getReservation()));

        } catch(ServiceException serviceException) {
            throw new BibliothequeException(serviceException);

        } catch(ConnexionException connexionException) {
            throw new BibliothequeException(connexionException);

        } catch(DAOException daoException) {
            throw new BibliothequeException(daoException);
        } catch(Exception exception) {
            throw new BibliothequeException(exception);
        }

    }

    /**
     * Ferme la connexion
     *
     * @throws BibliothequeException S'il y a une erreur avec la base de données
     */
    public void fermer() throws BibliothequeException {
        // fermeture de la connexion
        try {
            getCx().fermer();
        } catch(ConnexionException connexionException) {
            throw new BibliothequeException(connexionException);
        }
    }

    /**
     * Getter de la variable d'instance <code>this.cx</code>.
     *
     * @return La variable d'instance <code>this.cx</code>
     */
    private Connexion getCx() {
        return this.cx;
    }

    /**
     * Setter de la variable d'instance <code>this.cx</code>.
     *
     * @param cx La valeur à utiliser pour la variable d'instance <code>this.cx</code>
     */
    private void setCx(Connexion cx) {
        this.cx = cx;
    }

    /**
     * Getter de la variable d'instance <code>this.livre</code>.
     *
     * @return La variable d'instance <code>this.livre</code>
     */
    public LivreDAO getLivre() {
        return this.livre;
    }

    /**
     * Setter de la variable d'instance <code>this.livre</code>.
     *
     * @param livre La valeur à utiliser pour la variable d'instance <code>this.livre</code>
     */
    private void setLivre(LivreDAO livre) {
        this.livre = livre;
    }

    /**
     * Getter de la variable d'instance <code>this.membre</code>.
     *
     * @return La variable d'instance <code>this.membre</code>
     */
    private MembreDAO getMembre() {
        return this.membre;
    }

    /**
     * Setter de la variable d'instance <code>this.membre</code>.
     *
     * @param membre La valeur à utiliser pour la variable d'instance <code>this.membre</code>
     */
    private void setMembre(MembreDAO membre) {
        this.membre = membre;
    }

    /**
     * Getter de la variable d'instance <code>this.reservation</code>.
     *
     * @return La variable d'instance <code>this.reservation</code>
     */
    private ReservationDAO getReservation() {
        return this.reservation;
    }

    /**
     * Setter de la variable d'instance <code>this.reservation</code>.
     *
     * @param reservation La valeur à utiliser pour la variable d'instance <code>this.reservation</code>
     */
    private void setReservation(ReservationDAO reservation) {
        this.reservation = reservation;
    }

    /**
     * Getter de la variable d'instance <code>this.gestionLivre</code>.
     *
     * @return La variable d'instance <code>this.gestionLivre</code>
     */

    public LivreService getGestionLivre() {
        return this.gestionLivre;
    }

    /**
     * Setter de la variable d'instance <code>this.gestionLivre</code>.
     *
     * @param gestionLivre La valeur à utiliser pour la variable d'instance <code>this.gestionLivre</code>
     */
    private void setGestionLivre(LivreService gestionLivre) {
        this.gestionLivre = gestionLivre;
    }

    /**
     * Getter de la variable d'instance <code>this.gestionMembre</code>.
     *
     * @return La variable d'instance <code>this.gestionMembre</code>
     */
    public MembreService getGestionMembre() {
        return this.gestionMembre;
    }

    /**
     * Setter de la variable d'instance <code>this.gestionMembre</code>.
     *
     * @param gestionMembre La valeur à utiliser pour la variable d'instance <code>this.gestionMembre</code>
     */
    private void setGestionMembre(MembreService gestionMembre) {
        this.gestionMembre = gestionMembre;
    }

    /**
     * Getter de la variable d'instance <code>this.gestionPret</code>.
     *
     * @return La variable d'instance <code>this.gestionPret</code>
     */
    public PretService getGestionPret() {
        return this.gestionPret;
    }

    /**
     * Setter de la variable d'instance <code>this.gestionPret</code>.
     *
     * @param gestionPret La valeur à utiliser pour la variable d'instance <code>this.gestionPret</code>
     */
    private void setGestionPret(PretService gestionPret) {
        this.gestionPret = gestionPret;
    }

    /**
     * Getter de la variable d'instance <code>this.gestionReservation</code>.
     *
     * @return La variable d'instance <code>this.gestionReservation</code>
     */
    public ReservationService getGestionReservation() {
        return this.gestionReservation;
    }

    /**
     * Setter de la variable d'instance <code>this.gestionReservation</code>.
     *
     * @param gestionReservation La valeur à utiliser pour la variable d'instance <code>this.gestionReservation</code>
     */
    private void setGestionReservation(ReservationService gestionReservation) {
        this.gestionReservation = gestionReservation;
    }

}
