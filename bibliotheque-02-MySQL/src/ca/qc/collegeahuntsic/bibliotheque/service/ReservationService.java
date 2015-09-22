// Fichier ReservationService.java
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

public class ReservationService extends Services {

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
    public ReservationService(LivreDAO livre,
        MembreDAO membre,
        ReservationDAO reservation) throws BibliothequeException {
        if(livre.getConnexion() != membre.getConnexion()
            || reservation.getConnexion() != membre.getConnexion()) {
            throw new BibliothequeException("Les instances de livre, de membre et de réservation n'utilisent pas la même connexion au serveur");
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
     * @throws BibliothequeException
     * @throws Exception
     */
    public void reserver(int idReservation,
        int idLivre,
        int idMembre,
        String dateReservation) throws SQLException,
        BibliothequeException,
        Exception {
        try {
            // Vérifie que le livre est prêté
            LivreDTO tupleLivre = this.livre.getLivre(idLivre);
            if(tupleLivre == null) {
                throw new BibliothequeException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.idMembre == 0) {
                throw new BibliothequeException("Livre "
                    + idLivre
                    + " n'est pas prêté");
            }
            if(tupleLivre.idMembre == idMembre) {
                throw new BibliothequeException("Livre "
                    + idLivre
                    + " déjà prêté à ce membre");
            }

            // Vérifie que le membre existe
            MembreDTO tupleMembre = this.membre.getMembre(idMembre);
            if(tupleMembre == null) {
                throw new BibliothequeException("Membre inexistant: "
                    + idMembre);
            }

            // Vérifie si date reservation >= datePret
            if(Date.valueOf(dateReservation).before(tupleLivre.datePret)) {
                throw new BibliothequeException("Date de réservation inférieure à la date de prêt");
            }

            // Vérifie que la réservation n'existe pas
            if(this.reservation.existe(idReservation)) {
                throw new BibliothequeException("Réservation "
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
     * @throws BibliothequeException
     * @throws Exception
     */
    public void prendreRes(int idReservation,
        String datePret) throws SQLException,
        BibliothequeException,
        Exception {
        try {
            // Vérifie s'il y existe déjà une réservation pour le livre
            ReservationDTO tupleReservation = this.reservation.getReservation(idReservation);
            if(tupleReservation == null) {
                throw new BibliothequeException("Réservation inexistante : "
                    + idReservation);
            }

            // Vérifie que c'est la première réservation pour le livre
            ReservationDTO tupleReservationPremiere = this.reservation.getReservationLivre(tupleReservation.idLivre);
            if(tupleReservation.idReservation != tupleReservationPremiere.idReservation) {
                throw new BibliothequeException("La réservation n'est pas la première de la liste "
                    + "pour ce livre; la première est "
                    + tupleReservationPremiere.idReservation);
            }

            // Vérifie si le livre est disponible
            LivreDTO tupleLivre = this.livre.getLivre(tupleReservation.idLivre);
            if(tupleLivre == null) {
                throw new BibliothequeException("Livre inexistant: "
                    + tupleReservation.idLivre);
            }
            if(tupleLivre.idMembre != 0) {
                throw new BibliothequeException("Livre "
                    + tupleLivre.idLivre
                    + " déjà prêté à "
                    + tupleLivre.idMembre);
            }

            // Vérifie si le membre existe et sa limite de prêt
            MembreDTO tupleMembre = this.membre.getMembre(tupleReservation.idMembre);
            if(tupleMembre == null) {
                throw new BibliothequeException("Membre inexistant: "
                    + tupleReservation.idMembre);
            }
            if(tupleMembre.nbPret >= tupleMembre.limitePret) {
                throw new BibliothequeException("Limite de prêt du membre "
                    + tupleReservation.idMembre
                    + " atteinte");
            }

            // Vérifie si datePret >= tupleReservation.dateReservation
            if(Date.valueOf(datePret).before(tupleReservation.dateReservation)) {
                throw new BibliothequeException("Date de prêt inférieure à la date de réservation");
            }

            // Enregistrement du prêt.
            if(this.livre.preter(tupleReservation.idLivre,
                tupleReservation.idMembre,
                datePret) == 0) {
                throw new BibliothequeException("Livre suprimé par une autre transaction");
            }
            if(this.membre.preter(tupleReservation.idMembre) == 0) {
                throw new BibliothequeException("Membre suprimé par une autre transaction");
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
     * @throws BibliothequeException
     * @throws Exception
     */
    public void annulerRes(int idReservation) throws SQLException,
    BibliothequeException,
    Exception {
        try {

            // Vérifie que la réservation existe.
            if(this.reservation.annulerRes(idReservation) == 0) {
                throw new BibliothequeException("Réservation "
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
