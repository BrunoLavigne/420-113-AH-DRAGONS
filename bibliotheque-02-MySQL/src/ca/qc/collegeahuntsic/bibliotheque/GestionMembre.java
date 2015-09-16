
package ca.qc.collegeahuntsic.bibliotheque;

import java.sql.SQLException;

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
 *   le programme effectue les majuscules associées à chaque transaction
 * </post>
 */

public class GestionMembre {

    private Connexion cx;

    private Membre membre;

    private Reservation reservation;

    /**
     * Creation d'une instance
     */
    public GestionMembre(Membre membre,
        Reservation reservation) {

        this.cx = membre.getConnexion();
        this.membre = membre;
        this.reservation = reservation;
    }

    /**
     * Ajout d'un nouveau membre dans la base de données.
     * Si elle existe déjà, une exception est levée.
     */
    public void inscrire(int idMembre,
        String nom,
        long telephone,
        int limitePret) throws SQLException,
        BiblioException,
        Exception {
        try {
            // Vérifie si le membre existe déjà
            if(this.membre.existe(idMembre)) {
                throw new BiblioException("Membre existe deja: "
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
     */
    public void desinscrire(int idMembre) throws SQLException,
        BiblioException,
        Exception {
        try {
            // Vérifie si le membre existe et s'il a encore des prêts en cours
            TupleMembre tupleMembre = this.membre.getMembre(idMembre);
            if(tupleMembre == null) {
                throw new BiblioException("Membre inexistant: "
                    + idMembre);
            }
            if(tupleMembre.nbPret > 0) {
                throw new BiblioException("Le membre "
                    + idMembre
                    + " a encore des prêts.");
            }
            if(this.reservation.getReservationMembre(idMembre) != null) {
                throw new BiblioException("Membre "
                    + idMembre
                    + " a des réservations");
            }

            /* Suppression du membre */
            int nb = this.membre.desinscrire(idMembre);
            if(nb == 0) {
                throw new BiblioException("Membre "
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
