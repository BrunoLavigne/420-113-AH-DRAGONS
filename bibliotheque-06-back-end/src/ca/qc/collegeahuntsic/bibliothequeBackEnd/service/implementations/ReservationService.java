// Fichier ReservationService.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliothequeBackEnd.service.implementations;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dao.interfaces.IMembreDAO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dao.interfaces.IPretDAO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dao.interfaces.IReservationDAO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidCriterionValueException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dto.InvalidDTOClassException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dto.MissingDTOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.InvalidDAOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.InvalidLoanLimitException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.MissingLoanException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ServiceException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.service.interfaces.IReservationService;
import org.hibernate.Session;

/**
 *
 * Service de la table <code>reservation</code>.
 *
 * @author Dragons Vicieux
 */
public class ReservationService extends Service implements IReservationService {

    // TODO : remove membreDAO from this class
    private IReservationDAO reservationDAO;

    private IPretDAO pretDAO;

    /**
     * Crée le service de la table <code>reservation</code>.
     *
     * @param membreDAO Le DAO de la table <code>membre</code>
     * @param reservationDAO Le DAO de la table <code>reservation</code>
     * @param pretDAO Le DAO de la table <code>pret</code>
     * @throws InvalidDAOException Si le DAO de réservation est <code>null</code>, si le DAO de membre est <code>null</code>, si le DAO de livre est <code>null</code> ou si le DAO de prêt est <code>null</code>
     */
    public ReservationService(IMembreDAO membreDAO,
        IReservationDAO reservationDAO,
        IPretDAO pretDAO) throws InvalidDAOException {

        super();

        if(reservationDAO == null) {
            throw new InvalidDAOException("Le DAO de reservation ne peut être null");
        }
        if(pretDAO == null) {
            throw new InvalidDAOException("Le DAO de pret ne peut être null");
        }

        setReservationDAO(reservationDAO);
        setPretDAO(pretDAO);
    }

    // Region Opérations CRUD

    /**
     * {@inheritDoc}
     */
    @Override
    public void addReservation(Session session,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ServiceException {

        try {
            getReservationDAO().add(session,
                reservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReservationDTO getReservation(Session session,
        String idReservation) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        ServiceException {
        try {
            return (ReservationDTO) getReservationDAO().get(session,
                idReservation);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateReservation(Session session,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ServiceException {
        try {
            getReservationDAO().update(session,
                reservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteReservation(Session session,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ServiceException {
        try {
            getReservationDAO().delete(session,
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
    public List<ReservationDTO> getAllReservations(Session session,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidSortByPropertyException,
        ServiceException {
        try {
            return (List<ReservationDTO>) getReservationDAO().getAll(session,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReservationDTO> findReservationByMembre(Session session,
        String idMembre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ServiceException {

        //List<ReservationDTO> listeDesReservations = Collections.EMPTY_LIST;
        try {
            return getReservationDAO().findByMembre(session,
                idMembre,
                sortByPropertyName);
        } catch(
            DAOException
            | InvalidCriterionValueException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReservationDTO> findReservationByLivre(Session session,
        String idLivre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ServiceException {
        try {
            return getReservationDAO().findByLivre(session,
                idLivre,
                sortByPropertyName);
        } catch(
            DAOException
            | InvalidCriterionValueException daoException) {
            throw new ServiceException(daoException);
        }
    }

    // End Opérations de recherche

    // Region Méthodes métier

    /**
     * {@inheritDoc}
     */
    @Override
    public void placerReservation(Session session,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        MissingLoanException,
        ExistingLoanException,
        ExistingReservationException,
        ServiceException {

        if(session == null) {
            throw new InvalidHibernateSessionException("La session Hibernate ne peut être null");
        }

        if(reservationDTO == null) {
            throw new InvalidDTOException("Le DTO de la réservation ne peut pas être null");
        }

        // Si le livre n'a pas encore été prêté,
        final List<PretDTO> listeDesPrets = new ArrayList<>(reservationDTO.getLivreDTO().getPrets());

        if(listeDesPrets.isEmpty()) {
            throw new MissingLoanException("Le livre : "
                + reservationDTO.getLivreDTO().getIdLivre()
                + " n'a pas été prêté encore. Faire un emprunt au lieu d'un réservation");
        }

        // Si le livre est déjà prêté au membre
        for(PretDTO unPretDTO : listeDesPrets) {
            if(reservationDTO.getMembreDTO().equals(unPretDTO.getMembreDTO())) {
                throw new ExistingLoanException("Le livre "
                    + reservationDTO.getLivreDTO().getIdLivre()
                    + " est déjà prêté à ce membre");
            }
        }

        // si le membre a déjà réservé ce livre
        final List<ReservationDTO> listeReservation = new ArrayList<>(reservationDTO.getMembreDTO().getReservations());

        for(ReservationDTO reservation : listeReservation) {
            if(reservationDTO.getLivreDTO().equals(reservation.getLivreDTO())) {
                throw new ExistingReservationException("Le livre : "
                    + reservationDTO.getLivreDTO().getIdLivre()
                    + " a déjà été réservé par le membre : "
                    + reservation.getMembreDTO().getIdMembre());
            }
        }

        //Création de la réservation
        reservationDTO.setDateReservation(new Timestamp(System.currentTimeMillis()));
        addReservation(session,
            reservationDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void utiliserReservation(Session session,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ExistingReservationException,
        ExistingLoanException,
        InvalidLoanLimitException,
        ServiceException {

        if(session == null) {
            throw new InvalidHibernateSessionException("La session Hibernate ne peut être null");
        }
        if(reservationDTO == null) {
            throw new InvalidDTOException("Le DTO de la réservation ne peut pas être null");
        }

        // Si la réservation n'est pas la première de la liste
        final List<ReservationDTO> listeReservations = new ArrayList<>(reservationDTO.getLivreDTO().getReservations());

        if(!listeReservations.isEmpty()) {
            final ReservationDTO firstReservationDTO = listeReservations.get(0);

            if(!reservationDTO.getMembreDTO().equals(firstReservationDTO.getMembreDTO())) {
                throw new ExistingReservationException("La réservation n'est pas la première de la liste "
                    + "pour ce livre; la première est "
                    + firstReservationDTO.getIdReservation());
            }
        }

        // Si le livre est déjà prété
        final List<PretDTO> listeDesPret = new ArrayList<>(reservationDTO.getLivreDTO().getPrets());
        for(PretDTO pretDTO : listeDesPret) {
            if(pretDTO.getDateRetour() == null) {
                throw new ExistingLoanException("Livre "
                    + reservationDTO.getLivreDTO().getIdLivre()
                    + " déjà prêté à "
                    + pretDTO.getMembreDTO().getIdMembre());
            }
        }

        // Si le membre a atteint sa limite de prêt
        if(reservationDTO.getMembreDTO().getNbPret() >= reservationDTO.getMembreDTO().getLimitePret()) {
            throw new InvalidLoanLimitException("Limite de prêt du membre "
                + reservationDTO.getMembreDTO().getIdMembre()
                + " atteinte");
        }

        // Éliminer la réservation.

        final PretDTO unPretDTO = new PretDTO();
        unPretDTO.setMembreDTO(reservationDTO.getMembreDTO());
        unPretDTO.getMembreDTO().setNbPret(unPretDTO.getMembreDTO().getNbPret() + 1);
        unPretDTO.setLivreDTO(reservationDTO.getLivreDTO());
        unPretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
        unPretDTO.setDateRetour(null);

        try {
            getPretDAO().add(session,
                unPretDTO);
            reservationDTO.getLivreDTO().getReservations().remove(reservationDTO);
            annulerReservation(session,
                reservationDTO);
        } catch(
            DAOException
            | InvalidPrimaryKeyException
            | MissingDTOException
            | InvalidDTOClassException exception) {
            throw new ServiceException(exception);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void annulerReservation(Session session,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidPrimaryKeyException,
        MissingDTOException,
        InvalidDTOClassException,
        ServiceException {
        if(session == null) {
            throw new InvalidHibernateSessionException("La session Hibernate ne peut être null");
        }
        if(reservationDTO == null) {
            throw new InvalidDTOException("La réservation ne peut être null");
        }
        // TODO: vérifier pour l'erreur deleted object would be re-saved by cascade ????
        deleteReservation(session,
            reservationDTO);
    }

    // End Méthodes métier

    // Region Getter et Setter

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
