
package ca.qc.collegeahuntsic.bibliotheque;

import java.sql.Date;
import java.sql.SQLException;

/**
 * Gestion des transactions de reliées aux réservations de livres
 * par les membres dans une bibliothèque.
 *
 * Ce programme permet de gérer les transactions réserver,
 * prendre et annuler.
 *
 * Pré-condition
 *   la base de données de la bibliothèque doit exister
 *
 * Post-condition
 *   le programme effectue les maj associées à chaque
 *   transaction
 * </pre>
 */

public class GestionReservation {

    private Livre livre;

    private Membre membre;

    private Reservation reservation;

    private Connexion cx;

    /**
     * Creation d'une instance.
     * La connection de l'instance de livre et de membre doit être la même que cx,
     * afin d'assurer l'intégrité des transactions.
     */
    public GestionReservation(Livre livre,
        Membre membre,
        Reservation reservation) throws BiblioException {
        if(livre.getConnexion() != membre.getConnexion()
            || reservation.getConnexion() != membre.getConnexion()) {
            throw new BiblioException("Les instances de livre, de membre et de reservation n'utilisent pas la même connexion au serveur");
        }
        this.cx = livre.getConnexion();
        this.livre = livre;
        this.membre = membre;
        this.reservation = reservation;
    }

    /**
     * Réservation d'un livre par un membre.
     * Le livre doit être prêté.
     */
    public void reserver(int idReservation,
        int idLivre,
        int idMembre,
        String dateReservation) throws SQLException,
        BiblioException,
        Exception {
        try {
            /* Verifier que le livre est pret� */
            TupleLivre tupleLivre = this.livre.getLivre(idLivre);
            if(tupleLivre == null) {
                throw new BiblioException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.idMembre == 0) {
                throw new BiblioException("Livre "
                    + idLivre
                    + " n'est pas prete");
            }
            if(tupleLivre.idMembre == idMembre) {
                throw new BiblioException("Livre "
                    + idLivre
                    + " deja prete a ce membre");
            }

            /* V�rifier que le membre existe */
            TupleMembre tupleMembre = this.membre.getMembre(idMembre);
            if(tupleMembre == null) {
                throw new BiblioException("Membre inexistant: "
                    + idMembre);
            }

            /* Verifier si date reservation >= datePret */
            if(Date.valueOf(dateReservation).before(tupleLivre.datePret)) {
                throw new BiblioException("Date de reservation inferieure à la date de pret");
            }

            /* V�rifier que la r�servation n'existe pas */
            if(this.reservation.existe(idReservation)) {
                throw new BiblioException("Réservation "
                    + idReservation
                    + " existe deja");
            }

            /* Creation de la reservation */
            this.reservation.reserver(idReservation,
                idLivre,
                idMembre,
                dateReservation);
            this.cx.commit();
        } catch(Exception e) {
            this.cx.rollback();
            throw e;
        }
    }

    /**
     * Prise d'une réservation.
     * Le livre ne doit pas être prêté.
     * Le membre ne doit pas avoir dépassé sa limite de pret.
     * La réservation doit la être la première en liste.
     */
    public void prendreRes(int idReservation,
        String datePret) throws SQLException,
        BiblioException,
        Exception {
        try {
            /* Vérifie s'il existe une réservation pour le livre */
            TupleReservation tupleReservation = this.reservation.getReservation(idReservation);
            if(tupleReservation == null) {
                throw new BiblioException("Réservation inexistante : "
                    + idReservation);
            }

            /* Vérifie que c'est la première réservation pour le livre */
            TupleReservation tupleReservationPremiere = this.reservation.getReservationLivre(tupleReservation.idLivre);
            if(tupleReservation.idReservation != tupleReservationPremiere.idReservation) {
                throw new BiblioException("La réservation n'est pas la première de la liste "
                    + "pour ce livre; la premiere est "
                    + tupleReservationPremiere.idReservation);
            }

            /* Verifier si le livre est disponible */
            TupleLivre tupleLivre = this.livre.getLivre(tupleReservation.idLivre);
            if(tupleLivre == null) {
                throw new BiblioException("Livre inexistant: "
                    + tupleReservation.idLivre);
            }
            if(tupleLivre.idMembre != 0) {
                throw new BiblioException("Livre "
                    + tupleLivre.idLivre
                    + " deja prêté à "
                    + tupleLivre.idMembre);
            }

            /* Vérifie si le membre existe et sa limite de pret */
            TupleMembre tupleMembre = this.membre.getMembre(tupleReservation.idMembre);
            if(tupleMembre == null) {
                throw new BiblioException("Membre inexistant: "
                    + tupleReservation.idMembre);
            }
            if(tupleMembre.nbPret >= tupleMembre.limitePret) {
                throw new BiblioException("Limite de prêt du membre "
                    + tupleReservation.idMembre
                    + " atteinte");
            }

            /* Verifier si datePret >= tupleReservation.dateReservation */
            if(Date.valueOf(datePret).before(tupleReservation.dateReservation)) {
                throw new BiblioException("Date de prêt inférieure à la date de réservation");
            }

            /* Enregistrement du pret. */
            if(this.livre.preter(tupleReservation.idLivre,
                tupleReservation.idMembre,
                datePret) == 0) {
                throw new BiblioException("Livre supprimé par une autre transaction");
            }
            if(this.membre.preter(tupleReservation.idMembre) == 0) {
                throw new BiblioException("Membre supprimé par une autre transaction");
            }
            /* Eliminer la réservation */
            this.reservation.annulerRes(idReservation);
            this.cx.commit();
        } catch(Exception e) {
            this.cx.rollback();
            throw e;
        }
    }

    /**
     * Annulation d'une réservation.
     * La réservation doit exister.
     */
    public void annulerRes(int idReservation) throws SQLException,
    BiblioException,
    Exception {
        try {

            /* Vérifier que la réservation existe */
            if(this.reservation.annulerRes(idReservation) == 0) {
                throw new BiblioException("Réservation "
                    + idReservation
                    + " n'existe pas");
            }

            this.cx.commit();
        } catch(Exception e) {
            this.cx.rollback();
            throw e;
        }
    }
}