// Fichier MembreService.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.BibliothequeException;

/**
 * Gestion des transactions reliées à la création et
 * la suppression des membres dans une bibliothèque.
 *
 * Ce programme permet de gérer les transactions reliées à la
 * création et la suppression des membres.
 *
 *<pre>
 * Pré-condition:
 *   la base de données de la bibliothèque doit exister
 *</pre>
 *
 *<post>
 * Post-condition:
 *   le programme effectue les *mises à jour* associées à chaque transaction
 * </post>
 */
public class MembreService extends Services {

    private static final long serialVersionUID = 1L;

    private Connexion cx;

    private MembreDAO membre;

    private ReservationDAO reservation;

    /**
     * Création d'une instance
     *
     * @param membre
     * @param reservation
     */
    public MembreService(MembreDAO membre,
        ReservationDAO reservation) {

        setCx(membre.getConnexion());
        setMembre(membre);
        setReservation(reservation);
    }

    /**
     * Ajout d'un nouveau membre dans la base de données.
     * Si elle existe déjà, une exception est levée.
     *
     * @param idMembre
     * @param nom
     * @param telephone
     * @param limitePret
     * @throws SQLException
     * @throws BibliothequeException
     * @throws Exception
     */
    public void inscrire(int idMembre,
        String nom,
        long telephone,
        int limitePret) throws SQLException,
        BibliothequeException,
        Exception {
        try {
            // Vérifie si le membre existe déjà
            if(getMembre().existe(idMembre)) {
                throw new BibliothequeException("Membre existe deja: "
                    + idMembre);
            }

            // Ajout du membre
            getMembre().inscrire(idMembre,
                nom,
                telephone,
                limitePret);
            getCx().commit();
        } catch(Exception e) {
            getCx().rollback();
            throw e;
        }
    }

    /**
     * Suppression d'un membre dans la base de données.
     *
     * @param idMembre
     * @throws SQLException
     * @throws BibliothequeException
     * @throws Exception
     */
    public void desinscrire(int idMembre) throws SQLException,
        BibliothequeException,
        Exception {
        try {
            // Vérifie si le membre existe et s'il a encore des prêts en cours
            MembreDTO tupleMembre = getMembre().getMembre(idMembre);
            if(tupleMembre == null) {
                throw new BibliothequeException("Membre inexistant: "
                    + idMembre);
            }
            if(tupleMembre.nbPret > 0) {
                throw new BibliothequeException("Le membre "
                    + idMembre
                    + " a encore des prêts.");
            }
            if(getReservation().getReservationMembre(idMembre) != null) {
                throw new BibliothequeException("Membre "
                    + idMembre
                    + " a des réservations");
            }

            /* Suppression du membre */
            int nb = getMembre().desinscrire(idMembre);
            if(nb == 0) {
                throw new BibliothequeException("Membre "
                    + idMembre
                    + " inexistant");
            }
            getCx().commit();
        } catch(Exception e) {
            getCx().rollback();
            throw e;
        }

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
