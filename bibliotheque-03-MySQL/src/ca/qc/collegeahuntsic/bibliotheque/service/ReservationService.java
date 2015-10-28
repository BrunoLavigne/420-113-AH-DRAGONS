// Fichier ReservationService.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.Timestamp;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.PretDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
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

    private PretDAO pretDAO;

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
        ReservationDAO reservationDAO,
        PretDAO pretDAO) {

        super();
        setLivreDAO(livreDAO);
        setMembreDAO(membreDAO);
        setReservationDAO(reservationDAO);
        setPretDAO(pretDAO);
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
            return getReservationDAO().findByLivre(livreDTO);
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
            return getReservationDAO().findByMembre(membreDTO);
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
     * @throws ServiceException Si la réservation existe déjà,
     * si le membre n'existe pas,
     * si le livre n'existe pas,
     * si le livre n'a pas encore été prêté,
     * si le livre est déjà prêté au membre,
     * si le membre a déjà réservé ce livre ou
     * s'il y a une erreur avec la base de données
     *
     */
    public void reserver(ReservationDTO reservationDTO) throws ServiceException {

        try {

            // Membre null // Livre null // pret du livre. pretDAO.findbylivre (si pret.isEmpty() faire une reservation puisqu'il n'est pas prêté // )
            // extra: Le livre est prêté List(pretDTO) // boolean false // foreach pret in listPret // a ete emprunterParMembre = unMembreDTO.equals(pretDTO.).
            //
            // reservationDTO.setDateREservation(new Timestamp(System.currentTimeMillis()));
            // add(reservationDTO);

            // Si la réservation existe déjà
            // IMPOSSIBLE VU LA STRUCTURE DE BIBLIOTHEQUE -> new ReservationDTO()
            /*
            if(reservationDTO != null) {
                throw new ServiceException("La réservation existe déjà");
            }
             */

            //  Si le membre n'existe pas
            if(reservationDTO.getMembreDTO() == null) {
                throw new ServiceException("Membre inexistant");
            }

            // Vérification sur le livre

            if(reservationDTO.getLivreDTO() == null) {
                throw new ServiceException("Livre inexistant");
            }

            // Si le livre n'a pas encore été prêté,
            List<PretDTO> listeDesPrets = getPretDAO().findByLivre(reservationDTO.getLivreDTO());
            if(listeDesPrets.isEmpty()) {
                System.out.println("Le livre : "
                    + reservationDTO.getLivreDTO().getIdLivre()
                    + " n'a pas été prêté encore. Faire un emprunt au lieu d'un réservation");
            }

            // Si le livre est déjà prêté au membre
            for(PretDTO unPretDTO : listeDesPrets) {
                if(reservationDTO.getMembreDTO().equals(unPretDTO.getMembreDTO())) {
                    throw new ServiceException("Le livre est déjà prêté");
                }
            }

            // si le membre a déjà réservé ce livre
            List<ReservationDTO> listeReservation = getReservationDAO().findByMembre(reservationDTO.getMembreDTO());
            for(ReservationDTO reservation : listeReservation) {
                if(reservation.getLivreDTO().equals(reservationDTO.getLivreDTO())) {
                    throw new ServiceException("Le livre : "
                        + reservationDTO.getLivreDTO().getIdLivre()
                        + " a déjà été réservé par le membre : "
                        + reservation.getMembreDTO().getIdMembre());
                }
            }

            //Création de la réservation
            reservationDTO.setDateReservation(new Timestamp(System.currentTimeMillis()));

            add(reservationDTO);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /**
     *
     * Utilise une réservation.
     *
     * @param reservationDTO La réservation à utiliser
     * @throws ServiceException Si la réservation n'existe pas, si le membre n'existe pas, si le livre n'existe pas, si la réservation n'est pas la première de la liste, si le livre est déjà prété, si le membre a atteint sa limite de prêt ou s'il y a une erreur avec la base de données
     *
     */
    public void utiliser(ReservationDTO reservationDTO) throws ServiceException {

        try {

            // reservation existe
            // membre existe
            // livre existe
            // premier dans liste des reservation
            // le livre n'est pas prêter
            //
            // nombre d'emprunt maximum ?

            // Si la réservation existe déjà
            if(reservationDTO == null) {
                throw new ServiceException("La réservation n'existe pas");
            }

            reservationDTO.setMembreDTO(getMembreDAO().read(reservationDTO.getMembreDTO().getIdMembre()));
            reservationDTO.setLivreDTO(getLivreDAO().read(reservationDTO.getLivreDTO().getIdLivre()));

            //  Si le membre n'existe pas
            if(reservationDTO.getMembreDTO() == null) {
                throw new ServiceException("Membre inexistant");
            }

            // Vérification sur le livre
            if(reservationDTO.getLivreDTO() == null) {
                throw new ServiceException("Livre inexistant");
            }

            // Si la réservation n'est pas la première de la liste
            List<ReservationDTO> listeReservations = findByLivre(reservationDTO.getLivreDTO());

            if(!listeReservations.isEmpty()) {
                ReservationDTO firstReservationDTO = listeReservations.get(0);
                if(!reservationDTO.equals(firstReservationDTO)) {
                    throw new ServiceException("La réservation n'est pas la première de la liste "
                        + "pour ce livre; la première est "
                        + firstReservationDTO.getIdReservation());
                }
            }

            // Si le livre est déjà prété

            List<PretDTO> listeDesPret = getPretDAO().findByLivre(reservationDTO.getLivreDTO());
            if(!listeDesPret.isEmpty()) {
                PretDTO pretDTO = listeDesPret.get(0);
                throw new ServiceException("Livre "
                    + reservationDTO.getLivreDTO().getIdLivre()
                    + " déjà prêté à "
                    + pretDTO.getMembreDTO().getIdMembre());
            }

            // Si le membre a atteint sa limite de prêt
            if(reservationDTO.getMembreDTO().getNbPret() >= reservationDTO.getMembreDTO().getLimitePret()) {
                throw new ServiceException("Limite de prêt du membre "
                    + reservationDTO.getMembreDTO().getIdMembre()
                    + " atteinte");
            }

            // Éliminer la réservation.
            reservationDTO.getMembreDTO().setNbPret(reservationDTO.getMembreDTO().getNbPret() + 1);
            getMembreDAO().update(reservationDTO.getMembreDTO());

            PretDTO unPretDTO = new PretDTO();
            unPretDTO.setMembreDTO(reservationDTO.getMembreDTO());
            unPretDTO.setLivreDTO(reservationDTO.getLivreDTO());
            unPretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
            getPretDAO().add(unPretDTO);
            annuler(reservationDTO);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException.getMessage(),
                daoException);
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

        //try {

        // reservation existe

        // Si la réservation existe

        if(reservationDTO == null) {
            throw new ServiceException("La réservation n'existe pas");
        }

        delete(reservationDTO);

        /*}  catch(DAOException daoException) {
            throw new ServiceException(daoException);
        } */
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

    /**
     * Getter de la variable d'instance <code>this.pretDAO</code>.
     *
     * @return La variable d'instance <code>this.pretDAO</code>
     */
    private PretDAO getPretDAO() {
        return this.pretDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.pretDAO</code>.
     *
     * @param pretDAO La valeur à utiliser pour la variable d'instance <code>this.pretDAO</code>
     */
    private void setPretDAO(PretDAO pretDAO) {
        this.pretDAO = pretDAO;
    }

    // End Getter et Setter

}
