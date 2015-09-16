
package ca.qc.collegeahuntsic.bibliotheque;

import java.sql.Date;
import java.sql.SQLException;

/**
 * Gestion des transactions de reliées aux réservations de livres
 * par les membres dans une bibliothèque.
 *
 * Ce programme permet de gérer les transactions réservée,
 * prendre et annuler.
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

public class GestionReservation {

    private Livre livre;

    private Membre membre;

    private Reservation reservation;

    private Connexion cx;

    /**
     * Création d'une instance.
     * La connection de l'instance de livre et de membre doit être la même que cx,
     * afin d'assurer l'intégrité des transactions.
     *
     * @param livre
     * @param membre
     * @param reservation
     * @throws BiblioException
     */
    public GestionReservation(Livre livre,
        Membre membre,
        Reservation reservation) throws BiblioException {
        if(livre.getConnexion() != membre.getConnexion()
            || reservation.getConnexion() != membre.getConnexion()) {
            throw new BiblioException("Les instances de livre, de membre et de réservation n'utilisent pas la même connexion au serveur");
        }
        this.cx = livre.getConnexion();
        this.livre = livre;
        this.membre = membre;
        this.reservation = reservation;
    }

    /**
     * Réservation d'un livre par un membre.
     * Le livre doit être prêté.
     *
     * @param idReservation
     * @param idLivre
     * @param idMembre
     * @param dateReservation
     * @throws SQLException
     * @throws BiblioException
     * @throws Exception
     */
    public void reserver(int idReservation,
        int idLivre,
        int idMembre,
        String dateReservation) throws SQLException,
        BiblioException,
        Exception {
        try {
            // Vérifie que le livre est prêté
            TupleLivre tupleLivre = this.livre.getLivre(idLivre);
            if(tupleLivre == null) {
                throw new BiblioException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.idMembre == 0) {
                throw new BiblioException("Livre "
                    + idLivre
                    + " n'est pas prêté");
            }
            if(tupleLivre.idMembre == idMembre) {
                throw new BiblioException("Livre "
                    + idLivre
                    + " déjà prêté à ce membre");
            }

            // Vérifie que le membre existe
            TupleMembre tupleMembre = this.membre.getMembre(idMembre);
            if(tupleMembre == null) {
                throw new BiblioException("Membre inexistant: "
                    + idMembre);
            }

            // Vérifie si date reservation >= datePret
            if(Date.valueOf(dateReservation).before(tupleLivre.datePret)) {
                throw new BiblioException("Date de réservation inférieure à la date de prêt");
            }

            // Vérifie que la réservation n'existe pas
            if(this.reservation.existe(idReservation)) {
                throw new BiblioException("Réservation "
                    + idReservation
                    + " existe déjà");
            }

            //Création de la réservation
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
     * Le membre ne doit pas avoir dépassé sa limite de prêt.
     * La réservation doit être la première en liste.
     *
     * @param idReservation
     * @param datePret
     * @throws SQLException
     * @throws BiblioException
     * @throws Exception
     */
    public void prendreRes(int idReservation,
        String datePret) throws SQLException,
        BiblioException,
        Exception {
        try {
            // Vérifie s'il y existe déjà une réservation pour le livre
            TupleReservation tupleReservation = this.reservation.getReservation(idReservation);
            if(tupleReservation == null) {
                throw new BiblioException("Réservation inexistante : "
                    + idReservation);
            }

            // Vérifie que c'est la première réservation pour le livre
            TupleReservation tupleReservationPremiere = this.reservation.getReservationLivre(tupleReservation.idLivre);
            if(tupleReservation.idReservation != tupleReservationPremiere.idReservation) {
                throw new BiblioException("La réservation n'est pas la première de la liste "
                    + "pour ce livre; la première est "
                    + tupleReservationPremiere.idReservation);
            }

            // Vérifie si le livre est disponible
            TupleLivre tupleLivre = this.livre.getLivre(tupleReservation.idLivre);
            if(tupleLivre == null) {
                throw new BiblioException("Livre inexistant: "
                    + tupleReservation.idLivre);
            }
            if(tupleLivre.idMembre != 0) {
                throw new BiblioException("Livre "
                    + tupleLivre.idLivre
                    + " déjà prêté à "
                    + tupleLivre.idMembre);
            }

            // Vérifie si le membre existe et sa limite de prêt
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

            // Vérifie si datePret >= tupleReservation.dateReservation
            if(Date.valueOf(datePret).before(tupleReservation.dateReservation)) {
                throw new BiblioException("Date de prêt inférieure à la date de réservation");
            }

            // Enregistrement du prêt.
            if(this.livre.preter(tupleReservation.idLivre,
                tupleReservation.idMembre,
                datePret) == 0) {
                throw new BiblioException("Livre suprimé par une autre transaction");
            }
            if(this.membre.preter(tupleReservation.idMembre) == 0) {
                throw new BiblioException("Membre suprimé par une autre transaction");
            }
            // Éliminer la réservation.
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
     *
     * @param idReservation
     * @throws SQLException
     * @throws BiblioException
     * @throws Exception
     */
    public void annulerRes(int idReservation) throws SQLException,
        BiblioException,
        Exception {
        try {

            // Vérifie que la réservation existe.
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
