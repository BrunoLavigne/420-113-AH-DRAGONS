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
 *
 * Service de la table reservation.
 *
 * @author Dragons Vicieux
 */
public class ReservationService extends Services {

    private static final long serialVersionUID = 1L;

    private LivreDAO livreDAO;

    private MembreDAO membreDAO;

    private ReservationDAO reservationDAO;

    /**
     *
     * Crée le service de la table <code>reservation</code>.
     *
     * @param livreDAO Le DAO de la table <code>livre</code>
     * @param membreDAO Le DAO de la table <code>membre</code>
     * @param reservationDAO Le DAO de la table <code>reservation</code>
     */
    public ReservationService(LivreDAO livreDAO,
        MembreDAO membreDAO,
        ReservationDAO reservationDAO) {

        super();
        setLivreDAO(livreDAO);
        setMembreDAO(membreDAO);
        setReservationDAO(reservationDAO);
    }

    // Region Opérations CRUD

    /**
     *
     * Ajoute une nouvelle réservation.
     *
     * @param reservationDTO La réservation à ajouter.
     * @throws ServiceException S'il y a une erreur avec la base de données.
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
     * Lit une réservation.
     *
     * @param idReservation L'ID de la réservation à lire.
     * @return La réservation qui correspond au ID reçu.
     * @throws ServiceException S'il y a une erreur avec la base de données.
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
     * Met à jour une réservation.
     *
     * @param reservationDTO La réservation à mettre à jour.
     * @throws ServiceException S'il y a une erreur avec la base de données.
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
     * Supprime une réservation.
     *
     * @param reservationDTO La réservation à supprimer.
     * @throws ServiceException S'il y a une erreur avec la base de données.
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
     * Trouve toutes les réservations.
     *
     * @return La liste des réservations ; une liste vide sinon.
     * @throws ServiceException S'il y a une erreur avec la base de données.
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
     * Trouve les réservations à partir d'un livre.
     *
     * @param livreDTO Le livre à utiliser
     * @return La liste des réservations correspondantes, triée par date de réservation croissante ; une liste vide sinon.
     * @throws ServiceException S'il y a une erreur avec la base de données.
     */
    public List<ReservationDTO> findByLivre(LivreDTO livreDTO) throws ServiceException {

        try {
            return getReservationDAO().findByLivre(getLivreDAO().read(livreDTO.getIdLivre()));
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /**
     *
     * Trouve les réservations à partir d'un membre.
     *
     * @param membreDTO Le membre à utiliser
     * @return La liste des réservations correspondantes ; une liste vide sinon
     * @throws ServiceException S'il y a une erreur avec la base de données.
     */
    public List<ReservationDTO> findByMembre(MembreDTO membreDTO) throws ServiceException {

        try {
            return getReservationDAO().findByMembre(getMembreDAO().read(membreDTO.getIdMembre()));
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    // End Opérations de recherche

    // Region Méthodes métier

    /**
     *
     * Réserve un livre.
     *
     * @param reservationDTO La réservation à créer
     * @param livreDTO Le membre qui réserve
     * @param membreDTO Le livre à réserver
     * @param dateReservation La date de la réservation
     * @throws ServiceException Si la réservation existe déjà, si le membre n'existe pas, si le livre n'existe pas, si le livre n'a pas encore été prêté, si le livre est déjà prêté au membre, si le membre a déjà réservé ce livre ou s'il y a une erreur avec la base de données
     *
     */

    public void reserver(ReservationDTO reservationDTO,
        LivreDTO livreDTO,
        MembreDTO membreDTO,
        String dateReservation) throws ServiceException {

        try {

            // Si la réservation existe déjà

            ReservationDTO uneReservationDTO = getReservationDAO().read(reservationDTO.getIdReservation());

            if(uneReservationDTO != null) {
                System.err.println("La réservation : "
                    + reservationDTO.getIdReservation()
                    + " existe déjà");
                return;
            }

            //  Si le membre n'existe pas

            MembreDTO unMembreDTO = getMembreDAO().read(membreDTO.getIdMembre());

            if(unMembreDTO == null) {
                System.err.println("Membre inexistant: "
                    + membreDTO.getIdMembre());
                return;
            }

            // Vérification sur le livre

            LivreDTO unLivreDTO = getLivreDAO().read(livreDTO.getIdLivre());

            if(unLivreDTO == null) {
                System.err.println("Livre inexistant: "
                    + livreDTO.getIdLivre());
                return;
            }

            // Si le livre n'a pas encore été prêté,

            MembreDTO emprunteur = getMembreDAO().read(unLivreDTO.getIdMembre());

            if(emprunteur == null) {
                System.err.println("Le livre : "
                    + unLivreDTO.getIdLivre()
                    + " n'a pas été prêté encore. Faire un emprunt au lieu d'un réservation");
                return;
            }

            // Si le livre est déjà prêté au membre

            if(unMembreDTO.getIdMembre() == emprunteur.getIdMembre()) {
                System.err.println("Le livre : "
                    + unLivreDTO.getIdLivre()
                    + " est déjà prêté au membre : "
                    + unMembreDTO.getIdMembre());
                return;
            }

            // si le membre a déjà réservé ce livre

            List<ReservationDTO> listeReservation = getReservationDAO().findByMembre(unMembreDTO);

            for(ReservationDTO reservation : listeReservation) {
                if(reservation.getIdLivre() == unLivreDTO.getIdLivre()) {
                    System.err.println("Le livre : "
                        + unLivreDTO.getIdLivre()
                        + " a déjà été réservé par le membre : "
                        + unMembreDTO.getIdMembre());
                    return;
                }
            }

            //Création de la réservation
            ReservationDTO laReservationDTO = new ReservationDTO();

            laReservationDTO.setIdLivre(livreDTO.getIdLivre());
            laReservationDTO.setIdMembre(membreDTO.getIdMembre());
            laReservationDTO.setIdReservation(reservationDTO.getIdReservation());
            laReservationDTO.setDateReservation(Date.valueOf(dateReservation));

            add(uneReservationDTO);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /**
     *
     * Utilise une réservation.
     *
     * @param reservationDTO La réservation à utiliser
     * @param membreDTO Le membre qui utilise sa réservation
     * @param livreDTO Le livre à emprunter
     * @throws ServiceException Si la réservation n'existe pas, si le membre n'existe pas, si le livre n'existe pas, si la réservation n'est pas la première de la liste, si le livre est déjà prété, si le membre a atteint sa limite de prêt ou s'il y a une erreur avec la base de données.
     *
     */
    public void utiliser(ReservationDTO reservationDTO,
        MembreDTO membreDTO,
        LivreDTO livreDTO) throws ServiceException {

        try {

            // Si la réservation existe déjà

            ReservationDTO uneReservationDTO;

            uneReservationDTO = getReservationDAO().read(reservationDTO.getIdReservation());

            if(uneReservationDTO == null) {
                System.err.println("La réservation : "
                    + reservationDTO.getIdReservation()
                    + " n'existe pas");
                return;
            }

            //  Si le membre n'existe pas

            MembreDTO unMembreDTO = getMembreDAO().read(membreDTO.getIdMembre());

            if(unMembreDTO == null) {
                System.err.println("Membre inexistant: "
                    + membreDTO.getIdMembre());
                return;
            }

            // Vérification sur le livre

            LivreDTO unLivreDTO = getLivreDAO().read(livreDTO.getIdLivre());

            if(unLivreDTO == null) {
                System.err.println("Livre inexistant: "
                    + livreDTO.getIdLivre());
                return;
            }

            // Si la réservation n'est pas la première de la liste

            List<ReservationDTO> listReservations = findByLivre(getLivreDAO().read(reservationDTO.getIdLivre()));

            if(listReservations.get(0) != null) {

                ReservationDTO firstReservationDTO = listReservations.get(0);

                if(reservationDTO.getIdReservation() != firstReservationDTO.getIdReservation()) {
                    System.err.println("La réservation n'est pas la première de la liste "
                        + "pour ce livre; la première est "
                        + firstReservationDTO.getIdReservation());
                    return;
                }
            }

            // Si le livre est déjà prété

            if(unLivreDTO.getIdMembre() != 0) {
                System.err.println("Livre "
                    + unLivreDTO.getIdLivre()
                    + " déjà prêté à "
                    + unLivreDTO.getIdMembre());
                return;
            }

            // Si le membre a atteint sa limite de prêt

            if(unMembreDTO.getNbPret() >= unMembreDTO.getLimitePret()) {
                System.err.println("Limite de prêt du membre "
                    + uneReservationDTO.getIdMembre()
                    + " atteinte");
                return;
            }

            // Éliminer la réservation.
            delete(reservationDTO);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /**
     *
     * Annule une réservation.
     *
     * @param reservationDTO La reservation à annuler
     * @throws ServiceException Si la réservation n'existe pas ou s'il y a une erreur avec la base de données
     */
    public void annuler(ReservationDTO reservationDTO) throws ServiceException {

        try {

            // Si la réservation existe

            if(getReservationDAO().read(reservationDTO.getIdReservation()) == null) {
                System.err.println("Réservation "
                    + reservationDTO.getIdReservation()
                    + " n'existe pas");
                return;
            }

            delete(reservationDTO);

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
    private LivreDAO getLivreDAO() {
        return this.livreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @param livreDAO La valeur à utiliser pour la variable d'instance <code>this.livreDAO</code>
     */
    private void setLivreDAO(LivreDAO livreDAO) {
        this.livreDAO = livreDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @return La variable d'instance <code>this.membreDAO</code>
     */
    private MembreDAO getMembreDAO() {
        return this.membreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @param membreDAO La valeur à utiliser pour la variable d'instance <code>this.membreDAO</code>
     */
    private void setMembreDAO(MembreDAO membreDAO) {
        this.membreDAO = membreDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @return La variable d'instance <code>this.reservationDAO</code>
     */
    private ReservationDAO getReservationDAO() {
        return this.reservationDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @param reservationDAO La valeur à utiliser pour la variable d'instance <code>this.reservationDAO</code>
     */
    private void setReservationDAO(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    // End Getter et Setter

}
