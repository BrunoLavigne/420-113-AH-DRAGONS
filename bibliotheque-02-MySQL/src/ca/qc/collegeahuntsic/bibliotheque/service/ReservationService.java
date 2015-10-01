// Fichier ReservationService.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.Date;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.ConnexionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

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
     * @throws ServiceException
     */
    public ReservationService(LivreDAO livre,
        MembreDAO membre,
        ReservationDAO reservation) throws ServiceException {
        if(livre.getConnexion() != membre.getConnexion()
            || reservation.getConnexion() != membre.getConnexion()) {
            throw new ServiceException("Les instances de livre, de membre et de réservation n'utilisent pas la même connexion au serveur");
        }
        setCx(livre.getConnexion());
        setLivre(livre);
        setMembre(membre);
        setReservation(reservation);
    }

    /**
     * Réservation d'un livre par un membre.
     * Le livre doit être prêté.
     *
     * @param idReservation
     * @param idLivre
     * @param idMembre
     * @param dateReservation
     * @param reservationDTO
     * @throws ServiceException
     */
    public void reserver(int idReservation,
        int idLivre,
        int idMembre,
        String dateReservation,
        ReservationDTO reservationDTO) throws ServiceException {
        try {
            // Vérifie que le livre est prêté
            if(getLivre().read(idLivre) == null) {
                throw new ServiceException("Livre inexistant: "
                    + idLivre);
            }
            if(getMembre().read(idMembre).getIdMembre() == 0) {
                throw new ServiceException("Livre "
                    + idLivre
                    + " n'est pas prêté");
            }
            if(getMembre().read(idMembre).getIdMembre() == idMembre) {
                throw new ServiceException("Livre "
                    + idLivre
                    + " déjà prêté à ce membre");
            }

            // Vérifie que le membre existe
            if(getMembre().read(idMembre) == null) {
                throw new ServiceException("Membre inexistant: "
                    + idMembre);
            }

            // Vérifie si date reservation >= datePret
            if(Date.valueOf(dateReservation).before(getLivre().read(idLivre).getDatePret())) {
                throw new ServiceException("Date de réservation inférieure à la date de prêt");
            }

            // Vérifie que la réservation n'existe pas
            if(getReservation().read(idReservation).getIdReservation() == idReservation) {
                throw new ServiceException("Réservation "
                    + idReservation
                    + " existe déjà");
            }

            //Création de la réservation/
            getReservation().add(reservationDTO);
            getCx().commit();

        } catch(Exception exception) {
            try {
                getCx().rollback();
            } catch(ConnexionException connexionException) {
                throw new ServiceException(connexionException);
            }
            throw new ServiceException(exception);
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
     * @param idLivre
     * @param idMembre
     * @throws ServiceException
     */
    public void prendreRes(int idReservation,
        String datePret,
        int idLivre,
        int idMembre) throws ServiceException {
        try {
            // Vérifie s'il y existe déjà une réservation pour le livre
            if(getReservation().read(idReservation) == null) {
                throw new ServiceException("Réservation inexistante : "
                    + idReservation);
            }

            // Vérifie que c'est la première réservation pour le livre
            ReservationDTO tupleReservationPremiere = getReservation().getReservationLivre(tupleReservation.getIdLivre());
            if(tupleReservation.getIdReservation() != tupleReservationPremiere.getIdReservation()) {
                throw new ServiceException("La réservation n'est pas la première de la liste "
                    + "pour ce livre; la première est "
                    + tupleReservationPremiere.getIdReservation());
            }

            // Vérifie si le livre est disponible
            if(getReservation().read(idReservation).getIdLivre() == 0) {
                throw new ServiceException("Livre inexistant: "
                    + getReservation().read(idReservation).getIdLivre());
            }
            if(getReservation().read(idReservation).getIdLivre() != 0) {
                throw new ServiceException("Livre "
                    + getReservation().read(idReservation).getIdLivre()
                    + " déjà prêté à "
                    + getReservation().read(idReservation).getIdMembre());
            }

            // Vérifie si le membre existe et sa limite de prêt
            if(getMembre().read(idMembre).getIdMembre() == 0) {
                throw new ServiceException("Membre inexistant: "
                    + getReservation().read(idReservation).getIdMembre());
            }
            if(getMembre().read(idMembre).getNbPret() >= getMembre().read(idMembre).getLimitePret()) {
                throw new ServiceException("Limite de prêt du membre "
                    + getReservation().read(idReservation).getIdMembre()
                    + " atteinte");
            }

            // Vérifie si datePret >= tupleReservation.dateReservation
            if(Date.valueOf(datePret).before(getReservation().read(idReservation).getDateReservation())) {
                throw new ServiceException("Date de prêt inférieure à la date de réservation");
            }

            // Enregistrement du prêt.
            if(getLivre().preter(getReservation().read(idReservation).getIdLivre(),
                getReservation().read(idReservation).getIdMembre(),
                datePret) == 0) {
                throw new ServiceException("Livre suprimé par une autre transaction");
            }
            if(getMembre().preter(getReservation().read(idReservation).getIdMembre()) == 0) {
                throw new ServiceException("Membre suprimé par une autre transaction");
            }
            // Éliminer la réservation.
            getReservation().annulerRes(idReservation);
            getCx().commit();
        } catch(Exception exception) {
            try {
                getCx().rollback();
            } catch(ConnexionException connexionException) {
                throw new ServiceException(connexionException);
            }
        }
    }

    /**
     * Annulation d'une réservation.
     * La réservation doit exister.
     *
     * @param idReservation
     * @throws ServiceException
     */
    public void annulerRes(int idReservation) throws ServiceException {
        try {

            // Vérifie que la réservation existe.
            if(getReservation().annulerRes(idReservation) == 0) {
                throw new ServiceException("Réservation "
                    + idReservation
                    + " n'existe pas");
            }

            getCx().commit();

        } catch(Exception exception) {
            try {
                getCx().rollback();
            } catch(ConnexionException connexionException) {
                throw new ServiceException(connexionException);
            }
            throw new ServiceException(exception);
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
    public void setLivre(LivreDAO livre) {
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
    public void setMembre(MembreDAO membre) {
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
    public void setReservation(ReservationDAO reservation) {
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
    public void setCx(Connexion cx) {
        this.cx = cx;
    }
}
