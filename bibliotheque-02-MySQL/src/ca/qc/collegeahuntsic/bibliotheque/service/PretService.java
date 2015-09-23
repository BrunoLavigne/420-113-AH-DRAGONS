// Fichier PretService.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.Date;
import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.BibliothequeException;

/**
 * Gestion des transactions reliées aux prêts de livres
 * aux membres dans une bibliothèque.
 *
 * Ce programme permet de gérer les transactions prêtées,
 * rénouveller et retourner.
 *
 * <pre>
 * Pré-condition
 *   la base de données de la bibliothèque doit exister
 * </pre>
 * <pos>
 * Post-condition
 *   le programme effectue les maj associées à chaque
 *   transaction
 * </pos>
 */

public class PretService extends Services {

    private static final long serialVersionUID = 1L;

    private LivreDAO livre;

    private MembreDAO membre;

    private ReservationDAO reservation;

    private Connexion cx;

    /**
     * Création d'une instance.
     * La connection de l'instance de livre et de membre doit être la même que cx,
     * afin d'assurer l'intégrité des transactions.
     *
     * @param livre
     * @param membre
     * @param reservation
     * @throws BibliothequeException
     */
    public PretService(LivreDAO livre,
        MembreDAO membre,
        ReservationDAO reservation) throws BibliothequeException {
        if(livre.getConnexion() != membre.getConnexion()
            || reservation.getConnexion() != membre.getConnexion()) {
            throw new BibliothequeException("Les instances de livre, de membre et de réservation n'utilisent pas la même connexion au serveur");
        }
        setCx(livre.getConnexion());
        setLivre(livre);
        setMembre(membre);
        setReservation(reservation);
    }

    /**
     * Prêt d'un livre à un membre.
     * Le livre ne doit pas être prêté.
     * Le membre ne doit pas avoir dépassé sa limite de prêt.
     *
     * @param idLivre
     * @param idMembre
     * @param datePret
     * @throws SQLException
     * @throws BibliothequeException
     * @throws Exception
     */
    public void preter(int idLivre,
        int idMembre,
        String datePret) throws SQLException,
        BibliothequeException,
        Exception {
        try {
            // Vérfie si le livre est disponible
            LivreDTO tupleLivre = getLivre().getLivre(idLivre);
            if(tupleLivre == null) {
                throw new BibliothequeException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.idMembre != 0) {
                throw new BibliothequeException("Livre "
                    + idLivre
                    + " déjà prêté à "
                    + tupleLivre.idMembre);
            }

            // Vérifie si le membre existe et sa limite de prêt
            MembreDTO tupleMembre = getMembre().getMembre(idMembre);
            if(tupleMembre == null) {
                throw new BibliothequeException("Membre inexistant: "
                    + idMembre);
            }
            if(tupleMembre.nbPret >= tupleMembre.limitePret) {
                throw new BibliothequeException("Limite de prêt du membre "
                    + idMembre
                    + " atteinte");
            }

            // Vérifie s'il existe une réservation pour le livre.
            ReservationDTO tupleReservation = getReservation().getReservationLivre(idLivre);
            if(tupleReservation != null) {
                throw new BibliothequeException("Livre réservé par : "
                    + tupleReservation.idMembre
                    + " idReservation : "
                    + tupleReservation.idReservation);
            }

            // Enregistrement du prêt.
            int nb1 = getLivre().preter(idLivre,
                idMembre,
                datePret);
            if(nb1 == 0) {
                throw new BibliothequeException("Livre suprimé par une autre transaction");
            }
            int nb2 = getMembre().preter(idMembre);
            if(nb2 == 0) {
                throw new BibliothequeException("Membre suprimé par une autre transaction");
            }
            getCx().commit();
        } catch(Exception e) {
            getCx().rollback();
            throw e;
        }
    }

    /**
     * Renouvellement d'un prêt.
     * Le livre doit être prêté.
     * Le livre ne doit pas être réservé.
     *
     * @param idLivre
     * @param datePret
     * @throws SQLException
     * @throws BibliothequeException
     * @throws Exception
     */
    public void renouveler(int idLivre,
        String datePret) throws SQLException,
        BibliothequeException,
        Exception {
        try {
            // Vérifie si le livre est prêté
            LivreDTO tupleLivre = getLivre().getLivre(idLivre);
            if(tupleLivre == null) {
                throw new BibliothequeException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.idMembre == 0) {
                throw new BibliothequeException("Livre "
                    + idLivre
                    + " n'est pas prêté");
            }

            // Vérifie si date renouvellement >= datePret
            if(Date.valueOf(datePret).before(tupleLivre.datePret)) {
                throw new BibliothequeException("Date de renouvellement inférieure à la date de prêt");
            }

            // Vérifie s'il existe une réservation pour le livre.
            ReservationDTO tupleReservation = getReservation().getReservationLivre(idLivre);
            if(tupleReservation != null) {
                throw new BibliothequeException("Livre réservé par : "
                    + tupleReservation.idMembre
                    + " idReservation : "
                    + tupleReservation.idReservation);
            }

            // Enregistrement du prêt.
            int nb1 = getLivre().preter(idLivre,
                tupleLivre.idMembre,
                datePret);
            if(nb1 == 0) {
                throw new BibliothequeException("Livre suprimé par une autre transaction");
            }
            getCx().commit();
        } catch(Exception e) {
            getCx().rollback();
            throw e;
        }
    }

    /**
     * Retourner un livre prêté
     * Le livre doit être prêté.
     *
     * @param idLivre
     * @param dateRetour
     * @throws SQLException
     * @throws BibliothequeException
     * @throws Exception
     */
    public void retourner(int idLivre,
        String dateRetour) throws SQLException,
        BibliothequeException,
        Exception {
        try {
            // Vérifie si le livre est prêté
            LivreDTO tupleLivre = getLivre().getLivre(idLivre);
            if(tupleLivre == null) {
                throw new BibliothequeException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.idMembre == 0) {
                throw new BibliothequeException("Livre "
                    + idLivre
                    + " n'est pas prêté ");
            }

            // Vérifie si date retour >= datePret
            if(Date.valueOf(dateRetour).before(tupleLivre.datePret)) {
                throw new BibliothequeException("Date de retour inférieure à la date de prêt");
            }

            // Retour du prêt
            int nb1 = getLivre().retourner(idLivre);
            if(nb1 == 0) {
                throw new BibliothequeException("Livre suprimé par une autre transaction");
            }

            int nb2 = getMembre().retourner(tupleLivre.idMembre);
            if(nb2 == 0) {
                throw new BibliothequeException("Livre suprimé par une autre transaction");
            }
            getCx().commit();
        } catch(Exception e) {
            getCx().rollback();
            throw e;
        }

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
    public MembreDAO getMembre() {
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
    public ReservationDAO getReservation() {
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
     * Getter de la variable d'instance <code>this.cx</code>.
     *
     * @return La variable d'instance <code>this.cx</code>
     */
    public Connexion getCx() {
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
}
