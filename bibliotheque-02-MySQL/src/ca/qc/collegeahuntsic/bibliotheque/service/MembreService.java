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

        this.cx = membre.getConnexion();
        this.membre = membre;
        this.reservation = reservation;
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
            if(this.membre.existe(idMembre)) {
                throw new BibliothequeException("Membre existe deja: "
                    + idMembre);
            }

            // Ajout du membre
            this.membre.inscrire(idMembre,
                nom,
                telephone,
                limitePret);
            this.cx.commit();
        } catch(Exception e) {
            this.cx.rollback();
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
            MembreDTO tupleMembre = this.membre.getMembre(idMembre);
            if(tupleMembre == null) {
                throw new BibliothequeException("Membre inexistant: "
                    + idMembre);
            }
            if(tupleMembre.nbPret > 0) {
                throw new BibliothequeException("Le membre "
                    + idMembre
                    + " a encore des prêts.");
            }
            if(this.reservation.getReservationMembre(idMembre) != null) {
                throw new BibliothequeException("Membre "
                    + idMembre
                    + " a des réservations");
            }

            /* Suppression du membre */
            int nb = this.membre.desinscrire(idMembre);
            if(nb == 0) {
                throw new BibliothequeException("Membre "
                    + idMembre
                    + " inexistant");
            }
            this.cx.commit();
        } catch(Exception e) {
            this.cx.rollback();
            throw e;
        }
    }
}
