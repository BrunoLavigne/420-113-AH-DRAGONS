
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
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidDAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidLoanLimitException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.MissingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ServiceException;
import ca.qc.collegeahuntsic.bibliotheque.service.interfaces.IPretService;

/**
 * Gestion des transactions reliées aux prêts de livres
 * aux membres dans une bibliothèque.
 *
 * Ce programme permet de gérer les transactions prêtées,
 * rénouveller et retourner.
 *
 * <pre>
 * Pré-condition
 *   la base ded données de la bibliothèque doit exister
 * </pre>
 * <pos>
 * Post-condition
 *   le programme effectue les maj associées à chaque
 *   transaction
 * </pos>
 */

public class PretService extends Services implements IPretService {

    private static final long serialVersionUID = 1L;

    private IPretDAO pretDAO;

    private IMembreDAO membreDAO;

    private ILivreDAO livreDAO;

    private IReservationDAO reservationDAO;

    /**
     *
     * Crée le service de la table <code>pret</code>.
     *
     * @param pretDAO - Le DAO de la table <code>pret</code>
     * @param membreDAO - Le DAO de la table <code>membre</code>
     * @param livreDAO - Le DAO de la table <code>livre</code>
     * @param reservationDAO - Le DAO de la table <code>reservation</code>
     * @throws InvalidDAOException -  Si le DAO de prêt est null, si le DAO de membre est null, si le DAO de livre est null ou si le DAO de réservation est null
     */
    public PretService(IPretDAO pretDAO,
        IMembreDAO membreDAO,
        ILivreDAO livreDAO,
        IReservationDAO reservationDAO) throws InvalidDAOException {

        setPretDAO(pretDAO);
        setMembreDAO(membreDAO);
        setLivreDAO(livreDAO);
        setReservationDAO(reservationDAO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(Connexion connexion,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException {
        try {
            getPretDAO().add(connexion,
                pretDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PretDTO get(Connexion connexion,
        String idPret) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        ServiceException {

        try {
            return (PretDTO) getPretDAO().get(connexion,
                idPret);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Connexion connexion,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException {

        try {
            getPretDAO().update(connexion,
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
    public void delete(Connexion connexion,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException {

        try {
            getPretDAO().delete(connexion,
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
    public List<PretDTO> getAll(Connexion connexion,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidSortByPropertyException,
        ServiceException {

        try {
            return (List<PretDTO>) getPretDAO().getAll(connexion,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> findByMembre(Connexion connexion,
        String idMembre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ServiceException {
        try {
            return getPretDAO().findByMembre(connexion,
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
    public List<PretDTO> findByLivre(Connexion connexion,
        String idLivre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ServiceException {

        try {
            return getPretDAO().findByLivre(connexion,
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
    public List<PretDTO> findByDatePret(Connexion connexion,
        Timestamp datePret,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ServiceException {

        try {
            return getPretDAO().findByDatePret(connexion,
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
    public List<PretDTO> findByDateRetour(Connexion connexion,
        Timestamp dateRetour,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ServiceException {

        try {
            return getPretDAO().findByDateRetour(connexion,
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
    public void renouveler(Connexion connexion,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
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

            // Si la connexion est null
            if(connexion == null) {
                throw new InvalidHibernateSessionException("La connexion ne peut être null.");
            }
            // Si le prêt n'existe pas
            if(pretDTO == null) {
                throw new InvalidDTOClassException("Le pret  ne peut être null.");
            }
            // Si la clef primaire du prêt est null
            if(pretDTO.getIdPret() == null) {
                throw new InvalidPrimaryKeyException("La clef ne peut être null.");
            }
            // Si la clef primaire du membre est null
            if(pretDTO.getMembreDTO().getIdMembre() == null) {
                throw new InvalidPrimaryKeyException("La clef ne peut être null.");
            }
            // Si la clef primaire du livre est null
            if(pretDTO.getLivreDTO().getIdLivre() == null) {
                throw new InvalidPrimaryKeyException("La clef ne peut être null.");
            }

            // Si le prêt n'existe pas

            // Si le membre n'existe pas
            if(pretDTO.getMembreDTO() == null) {
                throw new MissingDTOException("Le membre n'existe pas");
            }

            // Si le livre n'existe pas
            if(pretDTO.getLivreDTO() == null) {
                throw new MissingDTOException("Le livre n'existe pas");
            }

            // Si l'ID du membre est null

            // Si l'ID du livre est null

            // Si la propriété à utiliser pour classer est null

            // Si le livre n'a pas encore été prêté
            List<PretDTO> listeDesPrets = getPretDAO().findByLivre(connexion,
                pretDTO.getLivreDTO().getIdLivre(),
                PretDTO.ID_LIVRE_COLUMN_NAME);
            if(listeDesPrets.isEmpty()) {
                throw new ServiceException("Le livre n'a pas été prêté encore.");
            }

            // Si le livre a été prêté à quelqu'un d'autre
            for(PretDTO unPretDTO : listeDesPrets) {
                if(!(pretDTO.getMembreDTO().getIdMembre() == unPretDTO.getMembreDTO().getIdMembre())) {
                    throw new ServiceException("Le livre est déjà prêté");
                }
            }

            // Si le livre a été réservé
            if(getReservationDAO().get(connexion,
                pretDTO.getLivreDTO().getIdLivre()) != null) {
                throw new ServiceException("Le livre est déjà réservé");
            }

            // Si la classe du membre n'est pas celle que prend en charge le DAO

            // Si la classe du prêt n'est pas celle que prend en charge le DAO

            pretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
            update(connexion,
                pretDTO);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException.getMessage(),
                daoException);
        }
    }

    /**
     * Commence un prêt.
     *
     * @param pretDTO - Le prêt à commencer
     * @throws ServiceException
     * - Si le membre n'existe pas,
     * si le livre n'existe pas,
     * si le livre a été prêté,
     * si le livre a été réservé,
     * si le membre a atteint sa limite de prêt ou s'il y a une erreur avec la base de données
     */
    @Override
    public void commencer(Connexion connexion,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidPrimaryKeyException,
        MissingDTOException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ExistingLoanException,
        InvalidLoanLimitException,
        ExistingReservationException,
        InvalidDTOClassException,
        ServiceException {

        try {

            //Si le membre n'existe pas
            if(pretDTO.getMembreDTO() == null) {
                throw new ServiceException("Le membre"
                    + pretDTO.getMembreDTO().getIdMembre()
                    + " n'existe pas");
            }

            pretDTO.setMembreDTO((MembreDTO) getMembreDAO().get(connexion,
                pretDTO.getMembreDTO().getIdMembre()));

            // Si le livre n'existe pas
            if(pretDTO.getLivreDTO() == null) {
                throw new ServiceException("Le livre est inexistant");
            }

            pretDTO.setLivreDTO((LivreDTO) getLivreDAO().get(connexion,
                pretDTO.getLivreDTO().getIdLivre()));

            // Si le livre a été prêté
            List<PretDTO> listeDesPrets = getPretDAO().findByLivre(connexion,
                pretDTO.getLivreDTO().getIdLivre(),
                PretDTO.ID_LIVRE_COLUMN_NAME);
            if(!listeDesPrets.isEmpty()) {
                throw new ServiceException("Le livre a été prêté.");
            }

            // Si le livre a été réservé
            List<ReservationDTO> listeDesReservations = getReservationDAO().findByLivre(connexion,
                pretDTO.getLivreDTO().getIdLivre(),
                ReservationDTO.ID_RESERVATION_COLUMN_NAME);
            if(!listeDesReservations.isEmpty()) {
                throw new ServiceException("Le livre a été réservé.");
            }

            // Si le membre a atteint sa limite de prêt
            if(pretDTO.getMembreDTO().getNbPret() == pretDTO.getMembreDTO().getLimitePret()) {
                throw new ServiceException("Le membre a atteint sa limite de prêt");
            }

            //Création du pret

            pretDTO.getMembreDTO().setNbPret(pretDTO.getMembreDTO().getNbPret() + 1);
            getMembreDAO().update(pretDTO.getMembreDTO());
            pretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
            add(pretDTO);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException.getMessage(),
                daoException);
        }
    }

    /**
     * Retourne un livre.
     *
     * @param pretDTO - Le prêt à terminer
     * @throws ServiceException
     * Si le prêt n'existe pas,
     * si le membre n'existe pas,
     * si le livre n'existe pas,
     * si le livre n'a pas encore été prêté,
     * si le livre a été prêté à quelqu'un d'autre ou s'il y a une erreur avec la base de données
     */
    public void retourner(PretDTO pretDTO) throws ServiceException {

        try {

            // Si le prêt n'existe pas
            if(pretDTO == null) {
                throw new ServiceException("Le pret n'existe pas");
            }

            // Si le membre n'existe pas
            if(pretDTO.getMembreDTO() == null) {
                throw new ServiceException("Le membre n'existe pas");
            }

            // Si le livre n'existe pas
            if(pretDTO.getLivreDTO() == null) {
                throw new ServiceException("Le livre est inexistant");
            }

            // Si le livre n'a pas encore été prêté
            List<PretDTO> listeDesPrets = getPretDAO().findByLivre(pretDTO.getLivreDTO());
            if(listeDesPrets.isEmpty()) {
                throw new ServiceException("Le livre n'a pas encore été prêté.");
            }

            // Si le livre a été prêteé à quelqu'un d'autre
            for(PretDTO unPretDTO : listeDesPrets) {
                if(!(pretDTO.getMembreDTO().getIdMembre() == unPretDTO.getMembreDTO().getIdMembre())) {
                    throw new ServiceException("Le livre est déjà prêté");
                }
            }

            pretDTO.setMembreDTO(getMembreDAO().read(pretDTO.getMembreDTO().getIdMembre()));
            pretDTO.setLivreDTO(getLivreDAO().read(pretDTO.getLivreDTO().getIdLivre()));
            pretDTO.getMembreDTO().setNbPret(pretDTO.getMembreDTO().getNbPret() - 1);
            getMembreDAO().update(pretDTO.getMembreDTO());
            pretDTO.setDateRetour(new Timestamp(System.currentTimeMillis()));
            update(pretDTO);

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
