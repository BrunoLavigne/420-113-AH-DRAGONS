
package ca.qc.collegeahuntsic.bibliothequeBackEnd.service.implementations;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dao.interfaces.ILivreDAO;
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
import ca.qc.collegeahuntsic.bibliothequeBackEnd.service.interfaces.IPretService;
import org.hibernate.Session;

/**
 * Service de la table <code>pret</code>.
 *
 * @author Dragons Vicieux
 */
public class PretService extends Service implements IPretService {

    private IPretDAO pretDAO;

    private IMembreDAO membreDAO;

    private ILivreDAO livreDAO;

    private IReservationDAO reservationDAO;

    /**
     * Crée le service de la table <code>pret</code>.
     *
     * @param pretDAO Le DAO de la table <code>pret</code>
     * @param membreDAO Le DAO de la table <code>membre</code>
     * @param livreDAO Le DAO de la table <code>livre</code>
     * @param reservationDAO Le DAO de la table <code>reservation</code>
     * @throws InvalidDAOException  Si le DAO de prêt est <code>null</code>, si le DAO de membre est <code>null</code>, si le DAO de livre est <code>null</code> ou si le DAO de réservation est <code>null</code>
     */
    public PretService(IPretDAO pretDAO,
        IMembreDAO membreDAO,
        ILivreDAO livreDAO,
        IReservationDAO reservationDAO) throws InvalidDAOException {
        if(pretDAO == null) {
            throw new InvalidDAOException("Le DAO du prêt ne peut être null");
        }
        if(membreDAO == null) {
            throw new InvalidDAOException("Le DAO du membre ne peut être null");
        }
        if(livreDAO == null) {
            throw new InvalidDAOException("Le DAO du livre ne peut être null");
        }
        if(reservationDAO == null) {
            throw new InvalidDAOException("Le DAO de réservation ne peut être null");
        }
        setPretDAO(pretDAO);
        setMembreDAO(membreDAO);
        setLivreDAO(livreDAO);
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
        InvalidDTOClassException,
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
        InvalidDTOClassException,
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

        List<PretDTO> listeDesPrets = Collections.EMPTY_LIST;
        try {
            listeDesPrets = (List<PretDTO>) getPretDAO().getAll(session,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException.getMessage(),
                daoException);
        }
        return listeDesPrets;
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

        List<PretDTO> listeDesPrets = Collections.EMPTY_LIST;

        try {
            listeDesPrets = getPretDAO().findByMembre(session,
                idMembre,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException.getMessage(),
                daoException);
        }
        return listeDesPrets;
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

        List<PretDTO> listeDesPrets = Collections.EMPTY_LIST;

        try {
            listeDesPrets = getPretDAO().findByLivre(session,
                idLivre,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException.getMessage(),
                daoException);
        }
        return listeDesPrets;
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

        List<PretDTO> listeDesPrets = Collections.EMPTY_LIST;

        try {
            listeDesPrets = getPretDAO().findByDatePret(session,
                datePret,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException.getMessage(),
                daoException);
        }
        return listeDesPrets;
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

        List<PretDTO> listeDesPrets = Collections.EMPTY_LIST;

        try {
            listeDesPrets = getPretDAO().findByDateRetour(session,
                dateRetour,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException.getMessage(),
                daoException);
        }
        return listeDesPrets;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renouvelerPret(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidPrimaryKeyException,
        MissingDTOException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        MissingLoanException,
        ExistingLoanException,
        ExistingReservationException,
        InvalidDTOClassException,
        ServiceException {
        try {

            // Si la connexion est null
            if(session == null) {
                throw new InvalidHibernateSessionException("La session ne peut être null.");
            }
            //  Si le prêt est null
            if(pretDTO == null) {
                throw new InvalidDTOException("Le DTO du prêt ne peut pas être null");
            }
            // Si la clef primaire du prêt est null
            if(pretDTO.getIdPret() == null) {
                throw new InvalidPrimaryKeyException("La clef ne peut être null.");
            }

            //TODO
            /*
            PretDTO unPretDTO = (PretDTO) getPretDAO().get(session,
                pretDTO.getIdPret());
            unPretDTO.setLivreDTO((LivreDTO) getLivreDAO().get(session,
                unPretDTO.getLivreDTO().getIdLivre()));
            unPretDTO.setMembreDTO((MembreDTO) getMembreDAO().get(session,
                unPretDTO.getMembreDTO().getIdMembre()));

            // Si la clef primaire du membre est null
            if(unPretDTO.getMembreDTO().getIdMembre() == null) {
                throw new InvalidCriterionException("La clef ne peut être null.");
            }

            // Si la clef primaire du livre est null
            if(unPretDTO.getLivreDTO().getIdLivre() == null) {
                throw new InvalidCriterionException("La clef ne peut être null.");
            }

            // Si le membre n'existe pas
            if(unPretDTO.getMembreDTO() == null) {
                throw new MissingDTOException("Le membre n'existe pas");
            }

            // Si le livre n'existe pas
            if(unPretDTO.getLivreDTO() == null) {
                throw new MissingDTOException("Le livre n'existe pas");
            }

            // Si l'ID du membre est null

            // Si l'ID du livre est null

            // Si la propriété à utiliser pour classer est null

            // Si le livre n'a pas encore été prêté
            List<PretDTO> listeDesPrets = getPretDAO().findByLivre(session,
                unPretDTO.getLivreDTO().getIdLivre(),
                PretDTO.ID_LIVRE_COLUMN_NAME);
            if(listeDesPrets.isEmpty()) {
                throw new ServiceException("Le livre n'a pas été prêté encore.");
            }

            // Si le livre a été prêté à quelqu'un d'autre
            for(PretDTO lePretDTO : listeDesPrets) {
                if(!unPretDTO.getMembreDTO().equals(lePretDTO.getMembreDTO())) {
                    throw new ServiceException("Le livre est déjà prêté");
                }
            }

            // Si le livre a été réservé
            if(getReservationDAO().get(session,
                unPretDTO.getLivreDTO().getIdLivre()) != null) {
                throw new ServiceException("Le livre est déjà réservé");
            }

            // Si la classe du membre n'est pas celle que prend en charge le DAO

            // Si la classe du prêt n'est pas celle que prend en charge le DAO

            unPretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
            updatePret(session,
                unPretDTO);
            // TODO
             */

            // Si le livre n'est pas prêté
            if(pretDTO.getLivreDTO().getPrets().isEmpty()) {
                throw new ServiceException("Le livre n'a pas été prêté encore.");
            }

            // Si le livre est réservé
            if(!pretDTO.getLivreDTO().getReservations().isEmpty()) {
                throw new ServiceException("Le livre est déjà réservé");
            }

            pretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
            updatePret(session,
                pretDTO);
            //TODO

        } catch(Exception exception) {
            throw new ServiceException(exception.getMessage(),
                exception);
        }
        /*
        catch(DAOException daoException) {
            throw new ServiceException(daoException.getMessage(),
                daoException);
        } */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commencerPret(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidPrimaryKeyException,
        MissingDTOException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        ExistingLoanException,
        InvalidLoanLimitException,
        ExistingReservationException,
        InvalidDTOClassException,
        ServiceException {

        try {
            // Si la connexion est null
            if(session == null) {
                throw new InvalidHibernateSessionException("La session ne peut être null.");
            }
            // Si le prêt est null
            if(pretDTO == null) {
                throw new InvalidDTOClassException("Le pret  ne peut être null.");
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
                throw new ExistingLoanException("Le livre "
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
            getMembreDAO().update(session,
                pretDTO.getMembreDTO());
            pretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
            addPret(session,
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
    public void terminerPret(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidPrimaryKeyException,
        MissingDTOException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        MissingLoanException,
        ExistingLoanException,
        InvalidDTOClassException,
        ServiceException {

        try {
            // Si la connexion est null
            if(session == null) {
                throw new InvalidHibernateSessionException("La session ne peut être null.");
            }
            // Si le prêt n'existe pas
            if(pretDTO == null) {
                throw new InvalidDTOClassException("Le pret  ne peut être null.");
            }

            final List<PretDTO> listeDesPrets = new ArrayList<>(pretDTO.getLivreDTO().getPrets());

            if(listeDesPrets.isEmpty()) {
                throw new ServiceException("Le livre n'a pas encore été prêté.");
            }

            /*
            PretDTO unPretDTO = (PretDTO) getPretDAO().get(session,
                pretDTO.getIdPret());

            // Si le prêt n'existe pas
            if(unPretDTO == null) {
                throw new ServiceException("Le pret n'existe pas");
            }

            // Si le membre n'existe pas
            if(unPretDTO.getMembreDTO() == null) {
                throw new ServiceException("Le membre n'existe pas");
            }

            // Si le livre n'existe pas
            if(unPretDTO.getLivreDTO() == null) {
                throw new ServiceException("Le livre est inexistant");
            }
            // Si l'ID du membre est null
            // Si l'ID du livre est null
            // Si la propriété à utiliser pour classer est null
            // Si le livre n'a pas encore été prêté
            List<PretDTO> listeDesPrets = getPretDAO().findByLivre(session,
                unPretDTO.getLivreDTO().getIdLivre(),
                PretDTO.ID_PRET_COLUMN_NAME);
            if(listeDesPrets.isEmpty()) {
                throw new ServiceException("Le livre n'a pas encore été prêté.");
            }

                        // Si le livre a été prêté à quelqu'un d'autre
                        for(PretDTO emprunterDTO : listeDesPrets) {
                            if(unPretDTO.getMembreDTO().getIdMembre() != emprunterDTO.getMembreDTO().getIdMembre()) {
                                throw new ServiceException("Le livre est déjà prêté");
                            }
                        }
            // Si la classe du membre n'est pas celle que prend en charge le DAO
            // Si la classe du prêt n'est pas celle que prend en charge le DAO
            unPretDTO.setMembreDTO((MembreDTO) getMembreDAO().get(session,
                unPretDTO.getMembreDTO().getIdMembre()));
            unPretDTO.setLivreDTO((LivreDTO) getLivreDAO().get(session,
                unPretDTO.getLivreDTO().getIdLivre()));
            unPretDTO.getMembreDTO().setNbPret(unPretDTO.getMembreDTO().getNbPret() - 1);
            getMembreDAO().update(session,
                unPretDTO.getMembreDTO());
            unPretDTO.setDateRetour(new Timestamp(System.currentTimeMillis()));
            updatePret(session,
                unPretDTO);
             */
            pretDTO.getMembreDTO().setNbPret(pretDTO.getMembreDTO().getNbPret() - 1);
            getMembreDAO().update(session,
                pretDTO.getMembreDTO());
            pretDTO.setDateRetour(new Timestamp(System.currentTimeMillis()));
            updatePret(session,
                pretDTO);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException.getMessage(),
                daoException);
        }
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
     * Getter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @return La variable d'instance <code>this.membreDAO</code>
     */
    public IMembreDAO getMembreDAO() {
        return this.membreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @param membreDAO La valeur à utiliser pour la variable d'instance <code>this.membreDAO</code>
     */
    public void setMembreDAO(IMembreDAO membreDAO) {
        this.membreDAO = membreDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @return La variable d'instance <code>this.livreDAO</code>
     */
    public ILivreDAO getLivreDAO() {
        return this.livreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @param livreDAO La valeur à utiliser pour la variable d'instance <code>this.livreDAO</code>
     */
    public void setLivreDAO(ILivreDAO livreDAO) {
        this.livreDAO = livreDAO;
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
