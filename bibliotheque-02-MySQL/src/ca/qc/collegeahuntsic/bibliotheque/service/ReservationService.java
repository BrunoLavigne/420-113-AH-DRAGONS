// Fichier ReservationService.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.Date;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
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

    private LivreDAO livreDAO;

    private MembreDAO membreDAO;

    private ReservationDAO reservationDAO;

    /**
     * Constructeur
     * Instancie le <code>livreDAO</code>, le <code>membreDAO</code> et le <code>reservationDAO</code>
     *
     * @param livreDAO
     * @param membreDAO
     * @param reservationDAO
     * @throws ServiceException
     */
    public ReservationService(LivreDAO livreDAO,
        MembreDAO membreDAO,
        ReservationDAO reservationDAO) throws ServiceException {

        setLivreDAO(livreDAO);
        setMembreDAO(membreDAO);
        setReservationDAO(reservationDAO);
    }

    // Region Opérations CRUD

    /**
     *
     * Appel la fonction ADD de <code>ReservationDAO</code>
     *
     * @param reservationDTO La réservation qui doit être ajouté à la base de données.
     * @throws ServiceException En cas d'erreur lors de l'appel au DAO, une exception est levée.
     */
    public void add(ReservationDTO reservationDTO) throws ServiceException {

        try {
            getReservationDAO().add(reservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /**
     *
     * Appel de la fonction READ de <code>ReservationDAO</code>
     *
     * @param idReservation Le ID de la réservation qui est recherchée.
     * @return La réservation qui est recherchée.
     * @throws ServiceException En cas d'erreur lors de l'appel au DAO, une exception est levée.
     */
    public ReservationDTO read(int idReservation) throws ServiceException {

        try {
            return getReservationDAO().read(idReservation);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /**
     *
     * Appel de la fonction UPDATE de <code>ReservationDAO</code>
     *
     * @param reservationDTO La réservation qui doit être updatée
     * @throws ServiceException En cas d'erreur lors de l'appel au DAO, une exception est levée.
     */
    public void update(ReservationDTO reservationDTO) throws ServiceException {

        try {
            getReservationDAO().update(reservationDTO,
                reservationDTO.getDateReservation());
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     *
     * Appel de la fonction DELETE de <code>ReservationDAO</code>
     *
     * @param reservationDTO La réservation qui doit être updatée
     * @throws ServiceException En cas d'erreur lors de l'appel au DAO, une exception est levée.
     */
    public void delete(ReservationDTO reservationDTO) throws ServiceException {

        try {
            getReservationDAO().delete(reservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    // End Opérations CRUD

    // Region Opérations de recherche

    /**
     *
     * Recherche de toutes les réservations
     *
     * @return Une List<ReservationDTO> qui contient toute les réservations
     * @throws ServiceException
     */
    public List<ReservationDTO> getAll() throws ServiceException {

        try {
            return getReservationDAO().getAll();
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /**
     *
     * Recheche des réservations qui sont liées à un livre
     *
     * @param idLivre Le ID du livre pour lequel nous souhaitons avoir les réservations
     * @return Une <code>List<ReservationDTO></code> contenant toutes les réservations associées à ce livre
     * @throws ServiceException En cas d'erreur lors de l'appel au DAO, une exception est levée.
     */
    public List<ReservationDTO> findByLivre(int idLivre) throws ServiceException {

        try {
            LivreDTO livreDTO = getLivreDAO().read(idLivre);
            return getReservationDAO().getByLivre(livreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    // End Opérations de recherche

    // Region Méthodes métier

    /**
     * Réservation d'un livre par un membre.
     * Le livre doit être prêté.
     *
     * @param idReservation Le numéro ID de la réservation qui doit être crée
     * @param idLivre Le ID du livre que le membre désire réserver
     * @param idMembre Le ID du membre qui désire effectuer la réservation
     * @param dateReservation La date à laquelle le membre désire effectuer la réservation
     * @throws ServiceException En cas d'erreur avec le DAO, un exception de type <code>ServiceException</code> est lancée.
     */
    public void reserver(ReservationDTO reservationDTO,
        LivreDTO livreDTO,
        MembreDTO membreDTO,
        String dateReservation) throws ServiceException {

        try {

            // Vérifie sur le livre
            LivreDTO unLivreDTO = getLivreDAO().read(livreDTO.getIdLivre());

            if(unLivreDTO == null) {
                throw new ServiceException("Livre inexistant: "
                    + livreDTO.getIdLivre());
            }
            if(unLivreDTO.getIdMembre() == 0) {
                throw new ServiceException("Livre "
                    + livreDTO.getIdLivre()
                    + " n'est pas prêté");
            }
            if(unLivreDTO.getIdMembre() == membreDTO.getIdMembre()) {
                throw new ServiceException("Livre "
                    + livreDTO.getIdLivre()
                    + " déjà prêté à ce membre");
            }

            // Vérifie sur le membre
            MembreDTO unMembreDTO = getMembreDAO().read(membreDTO.getIdMembre());
            if(unMembreDTO == null) {
                throw new ServiceException("Membre inexistant: "
                    + membreDTO.getIdMembre());
            }

            // Vérification sur la réservation
            // Vérifie si date reservation >= datePret
            if(Date.valueOf(dateReservation).before(livreDTO.getDatePret())) {
                throw new ServiceException("Date de réservation inférieure à la date de prêt");
            }

            // Vérifie que la réservation n'existe pas
            if(getReservationDAO().read(reservationDTO.getIdReservation()) != null) {
                throw new ServiceException("Réservation "
                    + reservationDTO.getIdReservation()
                    + " existe déjà");
            }

            //Création de la réservation
            ReservationDTO uneReservationDTO = new ReservationDTO();

            uneReservationDTO.setIdLivre(livreDTO.getIdLivre());
            uneReservationDTO.setIdMembre(membreDTO.getIdMembre());
            uneReservationDTO.setIdReservation(reservationDTO.getIdReservation());
            uneReservationDTO.setDateReservation(Date.valueOf(dateReservation));

            add(uneReservationDTO);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
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
     * @throws ServiceException
     */
    public void prendreRes(int idReservation,
        String datePret) throws ServiceException {

        try {

            // Vérifie s'il y existe déjà une réservation pour le livre

            ReservationDTO reservationDTO = getReservationDAO().read(idReservation);
            if(reservationDTO == null) {
                throw new ServiceException("Réservation inexistante : "
                    + idReservation);
            }

            // Vérifie que c'est la première réservation pour le livre

            List<ReservationDTO> tempListReservation = findByLivre(reservationDTO.getIdLivre());

            if(tempListReservation.get(0) != null) {

                ReservationDTO firstReservationDTO = tempListReservation.get(0);

                if(reservationDTO.getIdReservation() != firstReservationDTO.getIdReservation()) {
                    throw new ServiceException("La réservation n'est pas la première de la liste "
                        + "pour ce livre; la première est "
                        + firstReservationDTO.getIdReservation());
                }
            }

            // Vérifie si le livre est disponible
            LivreDTO livreDTO = getLivreDAO().read(reservationDTO.getIdLivre());
            if(livreDTO == null) {
                throw new ServiceException("Livre inexistant: "
                    + reservationDTO.getIdLivre());
            }
            if(livreDTO.getIdMembre() != 0) {
                throw new ServiceException("Livre "
                    + livreDTO.getIdLivre()
                    + " déjà prêté à "
                    + livreDTO.getIdMembre());
            }

            // Vérifie si le membre existe et sa limite de prêt
            MembreDTO membreDTO = getMembreDAO().read(reservationDTO.getIdMembre());
            if(membreDTO == null) {
                throw new ServiceException("Membre inexistant: "
                    + reservationDTO.getIdMembre());
            }
            if(membreDTO.getNbPret() >= membreDTO.getLimitePret()) {
                throw new ServiceException("Limite de prêt du membre "
                    + reservationDTO.getIdMembre()
                    + " atteinte");
            }

            // Vérifie si datePret >= tupleReservation.dateReservation
            if(Date.valueOf(datePret).before(reservationDTO.getDateReservation())) {
                throw new ServiceException("Date de prêt inférieure à la date de réservation");
            }

            /*
                // Enregistrement du prêt.
                if(getLivreDAO().preter(reservationDTO.getIdLivre(),
                    reservationDTO.getIdMembre(),
                    datePret) == 0) {
                    throw new ServiceException("Livre suprimé par une autre transaction");
                }
                if(getMembreDAO().preter(reservationDTO.getIdMembre()) == 0) {
                    throw new ServiceException("Membre suprimé par une autre transaction");
                }
             */

            // Éliminer la réservation.
            delete(reservationDTO);

        } catch(Exception exception) {
            throw new ServiceException(exception);
        }
    }

    /**
     * Annulation d'une réservation.
     * La réservation doit exister.
     *
     * @param idReservation Le ID de la réservation à annuler
     * @throws ServiceException En cas d'erreur lors de l'appel au DAO, une exception est levée.
     */
    public void annulerRes(int idReservation) throws ServiceException {

        try {

            // Vérifie que la réservation existe.
            if(getReservationDAO().read(idReservation) == null) {
                throw new ServiceException("Réservation "
                    + idReservation
                    + " n'existe pas");
            }

            delete(getReservationDAO().read(idReservation));

        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    // End Méthodes métier

    // Region Getter et Setter

    /**
     * Getter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @return La variable d'instance <code>this.livreDAO</code>
     */
    public LivreDAO getLivreDAO() {
        return this.livreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @param livre La valeur à utiliser pour la variable d'instance <code>this.livreDAO</code>
     */
    public void setLivreDAO(LivreDAO livreDAO) {
        this.livreDAO = livreDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @return La variable d'instance <code>this.membreDAO</code>
     */
    public MembreDAO getMembreDAO() {
        return this.membreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @param membre La valeur à utiliser pour la variable d'instance <code>this.membreDAO</code>
     */
    public void setMembreDAO(MembreDAO membreDAO) {
        this.membreDAO = membreDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @return La variable d'instance <code>this.reservationDAO</code>
     */
    public ReservationDAO getReservationDAO() {
        return this.reservationDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @param reservation La valeur à utiliser pour la variable d'instance <code>this.reservationDAO</code>
     */
    public void setReservationDAO(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    // End Getter et Setter

}
