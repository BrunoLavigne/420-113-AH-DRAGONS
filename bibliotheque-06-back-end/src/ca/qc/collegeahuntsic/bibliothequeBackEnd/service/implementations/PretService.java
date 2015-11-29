// Fichier PretService.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliothequeBackEnd.service.implementations;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.InvalidDAOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.InvalidLoanLimitException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.MissingLoanException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ServiceException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.service.interfaces.IPretService;
import org.hibernate.Session;

/**
 * Service de la table <code>pret</code>.
 *
 * @author Dragons Vicieux
 */
public class PretService extends Service implements IPretService {

    private IPretDAO pretDAO;

    private IReservationDAO reservationDAO;

    /**
     * Crée le service de la table <code>pret</code>.
     *
     * @param pretDAO Le DAO de la table <code>pret</code>
     * @param reservationDAO Le DAO de la table <code>reservation</code>
     * @throws InvalidDAOException  Si le DAO de prêt est <code>null</code>, si le DAO de membre est <code>null</code>, si le DAO de livre est <code>null</code> ou si le DAO de réservation est <code>null</code>
     */
    public PretService(IPretDAO pretDAO,
        IReservationDAO reservationDAO) throws InvalidDAOException {
        if(pretDAO == null) {
            throw new InvalidDAOException("Le DAO du prêt ne peut être null");
        }
        if(reservationDAO == null) {
            throw new InvalidDAOException("Le DAO de réservation ne peut être null");
        }
        setPretDAO(pretDAO);
        setReservationDAO(reservationDAO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPret(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ServiceException {
        try {
            getPretDAO().add(session,
                pretDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException.getMessage(),
                daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PretDTO getPret(Session session,
        String idPret) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        ServiceException {
        try {
            return (PretDTO) getPretDAO().get(session,
                idPret);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException.getMessage(),
                daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePret(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ServiceException {
        try {
            getPretDAO().update(session,
                pretDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException.getMessage(),
                daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deletePret(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ServiceException {
        try {
            getPretDAO().delete(session,
                pretDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException.getMessage(),
                daoException);
        }
    }

    // End Opérations CRUD

    // Region Opérations de recherche

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<PretDTO> getAllPrets(Session session,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidSortByPropertyException,
        ServiceException {
        try {
            return (List<PretDTO>) getPretDAO().getAll(session,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     * @throws InvalidCriterionValueException
     */
    @Override
    public List<PretDTO> findPretByMembre(Session session,
        String idMembre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        ServiceException {
        try {
            return getPretDAO().findByMembre(session,
                idMembre,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> findPretByLivre(Session session,
        String idLivre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        ServiceException {
        try {
            return getPretDAO().findByLivre(session,
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
    public List<PretDTO> findPretByDatePret(Session session,
        Timestamp datePret,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        ServiceException {
        try {
            return getPretDAO().findByDatePret(session,
                datePret,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> findPretByDateRetour(Session session,
        Timestamp dateRetour,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        ServiceException {
        try {
            return getPretDAO().findByDateRetour(session,
                dateRetour,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renouvelerPret(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        MissingLoanException,
        ExistingReservationException,
        ServiceException {

        if(session == null) {
            throw new InvalidHibernateSessionException("La session ne peut être null.");
        }
        //  Si le prêt est null
        if(pretDTO == null) {
            throw new InvalidDTOException("Le DTO du prêt ne peut pas être null");
        }

        // Si le livre n'est pas prêté
        final List<PretDTO> prets = new ArrayList<>(pretDTO.getLivreDTO().getPrets());
        if(!prets.isEmpty()) {
            for(PretDTO unpretDTO : prets) {
                if(unpretDTO.getDateRetour() != null) {
                    throw new MissingLoanException("Le livre "
                        + pretDTO.getLivreDTO().getTitre()
                        + " (ID de livre : "
                        + pretDTO.getLivreDTO().getIdLivre()
                        + ") n'a pas été prêté encore");
                }
            }
        }

        // vérifie si le livre est réservé
        final List<ReservationDTO> reservations = new ArrayList<>(pretDTO.getLivreDTO().getReservations());
        if(!reservations.isEmpty()) {
            final ReservationDTO reservationDTO = reservations.get(0);
            throw new ExistingReservationException("Le livre "
                + pretDTO.getLivreDTO().getTitre()
                + " (ID de livre : "
                + pretDTO.getLivreDTO().getIdLivre()
                + ") est réservé pour "
                + reservationDTO.getMembreDTO().getNom()
                + " (ID de membre : "
                + reservationDTO.getMembreDTO().getIdMembre()
                + ")");
        }

        pretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
        updatePret(session,
            pretDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commencerPret(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ExistingLoanException,
        InvalidLoanLimitException,
        ExistingReservationException,
        ServiceException {

        // Si la connexion est null
        if(session == null) {
            throw new InvalidHibernateSessionException("La session ne peut être null.");
        }
        // Si le prêt est null
        if(pretDTO == null) {
            throw new InvalidDTOException("Le pret  ne peut être null.");
        }

        // Si le livre a été prêté
        final List<PretDTO> listeDesPrets = new ArrayList<>(pretDTO.getLivreDTO().getPrets());
        for(PretDTO unPretDTO : listeDesPrets) {
            if(unPretDTO.getDateRetour() == null) {
                throw new ExistingLoanException("Le livre "
                    + pretDTO.getLivreDTO().getTitre()
                    + " (ID du livre: "
                    + pretDTO.getLivreDTO().getIdLivre()
                    + " ) a été prêté au membre "
                    + pretDTO.getMembreDTO().getNom()
                    + " (ID du membre: "
                    + pretDTO.getMembreDTO().getIdMembre()
                    + " ).");
            }
        }

        // Si le livre a été réservé
        final List<ReservationDTO> listeDesReservations = new ArrayList<>(pretDTO.getLivreDTO().getReservations());
        if(!listeDesReservations.isEmpty()) {
            throw new ExistingReservationException("Le livre "
                + pretDTO.getLivreDTO().getTitre()
                + " (ID du livre: "
                + pretDTO.getLivreDTO().getIdLivre()
                + " ) a été réservé par le membre membre "
                + pretDTO.getMembreDTO().getNom()
                + " (ID du membre: "
                + pretDTO.getMembreDTO().getIdMembre()
                + " ).");
        }

        // Si le membre a atteint sa limite de prêt
        if(pretDTO.getMembreDTO().getNbPret() == pretDTO.getMembreDTO().getLimitePret()) {
            throw new ServiceException("Le membre "
                + pretDTO.getMembreDTO().getIdMembre()
                + " a atteint sa limite de prêt");
        }

        //Creation du pret
        pretDTO.getMembreDTO().setNbPret(pretDTO.getMembreDTO().getNbPret() + 1);
        pretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
        addPret(session,
            pretDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void terminerPret(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        MissingLoanException,
        ServiceException {
        if(session == null) {
            throw new InvalidHibernateSessionException("La session ne peut être null.");
        }
        if(pretDTO == null) {
            throw new InvalidDTOException("Le pret  ne peut être null.");
        }

        // Si le livre a été prêté
        final List<PretDTO> listeDesPrets = new ArrayList<>(pretDTO.getLivreDTO().getPrets());
        for(PretDTO unPretDTO : listeDesPrets) {
            if(unPretDTO.getDateRetour() == null) {
                throw new MissingLoanException("Le livre "
                    + pretDTO.getLivreDTO().getTitre()
                    + " (ID du livre: "
                    + pretDTO.getLivreDTO().getIdLivre()
                    + " ) a été prêté au membre "
                    + pretDTO.getMembreDTO().getNom()
                    + " (ID du membre: "
                    + pretDTO.getMembreDTO().getIdMembre()
                    + " ).");
            }
        }

        pretDTO.getMembreDTO().setNbPret(pretDTO.getMembreDTO().getNbPret() - 1);
        pretDTO.setDateRetour(new Timestamp(System.currentTimeMillis()));
        updatePret(session,
            pretDTO);

    }

    /**
     * Getter de la variable d'instance <code>this.pretDAO</code>.
     *
     * @return La variable d'instance <code>this.pretDAO</code>
     */
    public IPretDAO getPretDAO() {
        return this.pretDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.pretDAO</code>.
     *
     * @param pretDAO La valeur à utiliser pour la variable d'instance <code>this.pretDAO</code>
     */
    public void setPretDAO(IPretDAO pretDAO) {
        this.pretDAO = pretDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @return La variable d'instance <code>this.reservationDAO</code>
     */
    public IReservationDAO getReservationDAO() {
        return this.reservationDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @param reservationDAO La valeur à utiliser pour la variable d'instance <code>this.reservationDAO</code>
     */
    public void setReservationDAO(IReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

}
