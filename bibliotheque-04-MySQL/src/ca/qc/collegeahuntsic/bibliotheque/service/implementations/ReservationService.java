// Fichier ReservationService.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.service.implementations;

import java.sql.Timestamp;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.ILivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IMembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IPretDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOClassException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.MissingDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidLoanLimitException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.MissingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ServiceException;
import ca.qc.collegeahuntsic.bibliotheque.service.interfaces.IReservationService;

/**
 *
 * Service de la table reservation.
 *
 * @author Dragons Vicieux
 */
public class ReservationService extends Services implements IReservationService {

    private static final long serialVersionUID = 1L;

    private ILivreDAO livreDAO;

    private IMembreDAO membreDAO;

    private IReservationDAO reservationDAO;

    private IPretDAO pretDAO;

    /**
     *
     * Crée le service de la table <code>reservation</code>.
     *
     * @param livreDAO Le DAO de la table <code>livre</code>
     * @param membreDAO Le DAO de la table <code>membre</code>
     * @param reservationDAO Le DAO de la table <code>reservation</code>
     */
    public ReservationService(ILivreDAO livreDAO,
        IMembreDAO membreDAO,
        IReservationDAO reservationDAO,
        IPretDAO pretDAO) {

        super();
        setLivreDAO(livreDAO);
        setMembreDAO(membreDAO);
        setReservationDAO(reservationDAO);
        setPretDAO(pretDAO);
    }

    // Region Opérations CRUD

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(Connexion connexion,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException {

        try {
            getReservationDAO().add(connexion,
                reservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReservationDTO get(Connexion connexion,
        String idReservation) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        ServiceException {

        try {
            return (ReservationDTO) getReservationDAO().get(connexion,
                idReservation);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Connexion connexion,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException {

        try {
            getReservationDAO().update(connexion,
                reservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Connexion connexion,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException {

        try {
            getReservationDAO().delete(connexion,
                reservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    // End Opérations CRUD

    // Region Opérations de recherche

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ReservationDTO> getAll(Connexion connexion,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidSortByPropertyException,
        ServiceException {

        try {
            return (List<ReservationDTO>) getReservationDAO().getAll(connexion,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReservationDTO> findByLivre(Connexion connexion,
        String idLivre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ServiceException {

        try {
            return getReservationDAO().findByLivre(connexion,
                idLivre,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReservationDTO> findByMembre(Connexion connexion,
        String idMembre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ServiceException {

        try {
            return getReservationDAO().findByMembre(connexion,
                idMembre,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    // End Opérations de recherche

    // Region Méthodes métier

    /**
     * {@inheritDoc}
     */
    @Override
    public void placer(Connexion connexion,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidPrimaryKeyException,
        MissingDTOException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        MissingLoanException,
        ExistingLoanException,
        ExistingReservationException,
        InvalidDTOClassException,
        ServiceException {

        try {

            if(connexion == null) {
                throw new InvalidHibernateSessionException("La connexion na pas pu être établie");
            }

            if(reservationDTO == null) {
                throw new InvalidDTOException("Le DTO de la réservation ne peut pas être null");
            }

            reservationDTO.setLivreDTO((LivreDTO) getLivreDAO().get(connexion,
                reservationDTO.getLivreDTO().getIdLivre()));
            reservationDTO.setMembreDTO((MembreDTO) getMembreDAO().get(connexion,
                reservationDTO.getMembreDTO().getIdMembre()));

            //  Si le membre n'existe pas
            if(reservationDTO.getMembreDTO() == null) {
                throw new InvalidDTOException("Le DTO du membre ne peut pas être null");
            }

            // Vérification sur le livre

            if(reservationDTO.getLivreDTO() == null) {
                throw new InvalidDTOException("Le DTO du livre ne peut pas être null");
            }

            // Si le livre n'a pas encore été prêté,
            List<PretDTO> listeDesPrets = getPretDAO().findByLivre(connexion,
                reservationDTO.getLivreDTO().getIdLivre(),
                PretDTO.ID_PRET_COLUMN_NAME);

            if(listeDesPrets.isEmpty()) {
                throw new MissingLoanException("Le livre : "
                    + reservationDTO.getLivreDTO().getIdLivre()
                    + " n'a pas été prêté encore. Faire un emprunt au lieu d'un réservation");

            }

            // Si le livre est déjà prêté au membre
            for(PretDTO unPretDTO : listeDesPrets) {
                if(reservationDTO.getMembreDTO().equals(unPretDTO.getMembreDTO())) {
                    throw new ExistingLoanException("Le livre est déjà prêté");
                }
            }

            // si le membre a déjà réservé ce livre
            List<ReservationDTO> listeReservation = findByMembre(connexion,
                reservationDTO.getMembreDTO().getIdMembre(),
                MembreDTO.ID_MEMBRE_COLUMN_NAME);

            for(ReservationDTO reservation : listeReservation) {
                if(reservation.getLivreDTO().equals(reservationDTO.getLivreDTO())) {
                    throw new ExistingReservationException("Le livre : "
                        + reservationDTO.getLivreDTO().getIdLivre()
                        + " a déjà été réservé par le membre : "
                        + reservation.getMembreDTO().getIdMembre());
                }
            }

            //Création de la réservation
            reservationDTO.setDateReservation(new Timestamp(System.currentTimeMillis()));

            add(connexion,
                reservationDTO);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void utiliser(Connexion connexion,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidPrimaryKeyException,
        MissingDTOException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ExistingReservationException,
        ExistingLoanException,
        InvalidLoanLimitException,
        InvalidDTOClassException,
        ServiceException {

        try {

            // reservation existe
            // membre existe
            // livre existe
            // premier dans liste des reservation
            // le livre n'est pas prêter
            //
            // nombre d'emprunt maximum ?

            ReservationDTO uneReservationDTO = (ReservationDTO) getReservationDAO().get(connexion,
                reservationDTO.getIdReservation());

            // Si la réservation existe déjà
            if(uneReservationDTO == null) {
                throw new InvalidCriterionException("La réservation n'existe pas");
            }

            uneReservationDTO.setMembreDTO((MembreDTO) getMembreDAO().get(connexion,
                uneReservationDTO.getMembreDTO().getIdMembre()));
            uneReservationDTO.setLivreDTO((LivreDTO) getLivreDAO().get(connexion,
                uneReservationDTO.getLivreDTO().getIdLivre()));

            //  Si le membre n'existe pas
            if(uneReservationDTO.getMembreDTO() == null) {
                throw new InvalidCriterionException("Membre inexistant");
            }

            // Vérification sur le livre
            if(uneReservationDTO.getLivreDTO() == null) {
                throw new InvalidCriterionException("Livre inexistant");
            }

            // Si la réservation n'est pas la première de la liste
            List<ReservationDTO> listeReservations = findByLivre(connexion,
                uneReservationDTO.getLivreDTO().getIdLivre(),
                ReservationDTO.ID_RESERVATION_COLUMN_NAME);

            if(!listeReservations.isEmpty()) {
                ReservationDTO firstReservationDTO = listeReservations.get(0);
                if(!uneReservationDTO.equals(firstReservationDTO)) {
                    throw new ExistingReservationException("La réservation n'est pas la première de la liste "
                        + "pour ce livre; la première est "
                        + firstReservationDTO.getIdReservation());
                }
            }

            // Si le livre est déjà prété
            List<PretDTO> listeDesPret = getPretDAO().findByLivre(connexion,
                uneReservationDTO.getLivreDTO().getIdLivre(),
                ReservationDTO.ID_RESERVATION_COLUMN_NAME);
            if(!listeDesPret.isEmpty()) {
                PretDTO pretDTO = listeDesPret.get(0);
                throw new ExistingLoanException("Livre "
                    + uneReservationDTO.getLivreDTO().getIdLivre()
                    + " déjà prêté à "
                    + pretDTO.getMembreDTO().getIdMembre());
            }

            // Si le membre a atteint sa limite de prêt
            if(uneReservationDTO.getMembreDTO().getNbPret() >= uneReservationDTO.getMembreDTO().getLimitePret()) {
                throw new InvalidLoanLimitException("Limite de prêt du membre "
                    + uneReservationDTO.getMembreDTO().getIdMembre()
                    + " atteinte");
            }

            // Éliminer la réservation.
            uneReservationDTO.getMembreDTO().setNbPret(uneReservationDTO.getMembreDTO().getNbPret() + 1);
            getMembreDAO().update(connexion,
                uneReservationDTO.getMembreDTO());

            PretDTO unPretDTO = new PretDTO();
            unPretDTO.setMembreDTO(uneReservationDTO.getMembreDTO());
            unPretDTO.setLivreDTO(uneReservationDTO.getLivreDTO());
            unPretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
            getPretDAO().add(connexion,
                unPretDTO);
            annuler(connexion,
                uneReservationDTO);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException.getMessage(),
                daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void annuler(Connexion connexion,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidPrimaryKeyException,
        MissingDTOException,
        InvalidDTOClassException,
        ServiceException {

        //try {

        // reservation existe

        // Si la réservation existe

        if(reservationDTO == null) {
            throw new InvalidDTOException("La réservation n'existe pas");
        }

        delete(connexion,
            reservationDTO);

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
    private ILivreDAO getLivreDAO() {
        return this.livreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @param livreDAO La valeur à utiliser pour la variable d'instance <code>this.livreDAO</code>
     */
    private void setLivreDAO(ILivreDAO livreDAO) {
        this.livreDAO = livreDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @return La variable d'instance <code>this.membreDAO</code>
     */
    private IMembreDAO getMembreDAO() {
        return this.membreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @param membreDAO La valeur à utiliser pour la variable d'instance <code>this.membreDAO</code>
     */
    private void setMembreDAO(IMembreDAO membreDAO) {
        this.membreDAO = membreDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @return La variable d'instance <code>this.reservationDAO</code>
     */
    private IReservationDAO getReservationDAO() {
        return this.reservationDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @param reservationDAO La valeur à utiliser pour la variable d'instance <code>this.reservationDAO</code>
     */
    private void setReservationDAO(IReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.pretDAO</code>.
     *
     * @return La variable d'instance <code>this.pretDAO</code>
     */
    private IPretDAO getPretDAO() {
        return this.pretDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.pretDAO</code>.
     *
     * @param pretDAO La valeur à utiliser pour la variable d'instance <code>this.pretDAO</code>
     */
    private void setPretDAO(IPretDAO pretDAO) {
        this.pretDAO = pretDAO;
    }

    // End Getter et Setter

}
