
package ca.qc.collegeahuntsic.bibliotheque.service.implementations;

import java.sql.Timestamp;
import java.util.Collections;
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
 * Service de la table <code>pret</code>.
 */

public class PretService extends Service implements IPretService {

    private static final long serialVersionUID = 1L;

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

        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null.");
        }

        if(pretDTO == null) {
            throw new InvalidDTOException("Le pret ne peut être null.");
        }

        try {
            getPretDAO().add(connexion,
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
    public PretDTO get(Connexion connexion,
        String idPret) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        ServiceException {

        try {
            return (PretDTO) getPretDAO().get(connexion,
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

        List<PretDTO> listeDesPrets = Collections.EMPTY_LIST;

        try {
            listeDesPrets = (List<PretDTO>) getPretDAO().getAll(connexion,
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
    public List<PretDTO> findByMembre(Connexion connexion,
        String idMembre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ServiceException {

        List<PretDTO> listeDesPrets = Collections.EMPTY_LIST;

        try {
            listeDesPrets = getPretDAO().findByMembre(connexion,
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
    public List<PretDTO> findByLivre(Connexion connexion,
        String idLivre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ServiceException {

        List<PretDTO> listeDesPrets = Collections.EMPTY_LIST;

        try {
            listeDesPrets = getPretDAO().findByLivre(connexion,
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
    public List<PretDTO> findByDatePret(Connexion connexion,
        Timestamp datePret,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ServiceException {

        List<PretDTO> listeDesPrets = Collections.EMPTY_LIST;

        try {
            listeDesPrets = getPretDAO().findByDatePret(connexion,
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
    public List<PretDTO> findByDateRetour(Connexion connexion,
        Timestamp dateRetour,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ServiceException {

        List<PretDTO> listeDesPrets = Collections.EMPTY_LIST;

        try {
            listeDesPrets = getPretDAO().findByDateRetour(connexion,
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
            //  Si le prêt est null
            if(pretDTO == null) {
                throw new InvalidDTOException("Le DTO du prêt ne peut pas être null");
            }

            PretDTO unPretDTO = (PretDTO) getPretDAO().get(connexion,
                pretDTO.getIdPret());

            // Si le pret n'existe pas
            if(unPretDTO == null) {
                throw new MissingDTOException("Le prêt n'existe pas");
            }

            MembreDTO unMembreDTO = (MembreDTO) getMembreDAO().get(connexion,
                unPretDTO.getMembreDTO().getIdMembre());

            // Si le membre n'existe pas
            if(unMembreDTO == null) {
                throw new MissingDTOException("Le membre n'existe pas");
            }

            LivreDTO unLivreDTO = (LivreDTO) getLivreDAO().get(connexion,
                unPretDTO.getLivreDTO().getIdLivre());

            // Si le livre n'existe pas
            if(unLivreDTO == null) {
                throw new MissingDTOException("Le livre n'existe pas");
            }

            // Si le livre n'a pas encore été prêté
            List<PretDTO> listeDesPrets = getPretDAO().findByLivre(connexion,
                unLivreDTO.getIdLivre(),
                PretDTO.ID_LIVRE_COLUMN_NAME);

            if(listeDesPrets.isEmpty()) {
                throw new MissingLoanException("Le livre n'a pas été prêté encore.");
            }

            // Si le livre a été prêté à quelqu'un d'autre
            for(PretDTO lePretDTO : listeDesPrets) {
                if(!unMembreDTO.equals(lePretDTO.getMembreDTO())) {
                    throw new ExistingLoanException("Le livre est déjà prêté au membre "
                        + lePretDTO.getMembreDTO().getIdMembre());
                }
            }

            // Si le livre a été réservé
            List<ReservationDTO> listeDesReservations = getReservationDAO().findByLivre(connexion,
                unLivreDTO.getIdLivre(),
                ReservationDTO.ID_LIVRE_COLUMN_NAME);

            if(!listeDesReservations.isEmpty()) {
                throw new ExistingReservationException("Le livre "
                    + unLivreDTO.getIdLivre()
                    + " a été réservé.");
            }

            unPretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
            unPretDTO.setLivreDTO(unLivreDTO);
            unPretDTO.setMembreDTO(unMembreDTO);
            update(connexion,
                unPretDTO);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException.getMessage(),
                daoException);
        }
    }

    /**
     * {@inheritDoc}
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

            // Si la connexion est null
            if(connexion == null) {
                throw new InvalidHibernateSessionException("La connexion ne peut être null.");
            }
            // Si le prêt n'existe pas
            if(pretDTO == null) {
                throw new InvalidDTOException("Le pret  ne peut être null.");
            }

            MembreDTO unMembreDTO = (MembreDTO) getMembreDAO().get(connexion,
                pretDTO.getMembreDTO().getIdMembre());

            // Si le membre n'existe pas
            if(unMembreDTO == null) {
                throw new MissingDTOException("Le membre n'existe pas");
            }

            LivreDTO unLivreDTO = (LivreDTO) getLivreDAO().get(connexion,
                pretDTO.getLivreDTO().getIdLivre());

            // Si le livre n'existe pas
            if(unLivreDTO == null) {
                throw new MissingDTOException("Le livre est inexistant");
            }

            // Si le livre a été prêté
            List<PretDTO> listeDesPrets = getPretDAO().findByLivre(connexion,
                unLivreDTO.getIdLivre(),
                PretDTO.ID_LIVRE_COLUMN_NAME);
            if(!listeDesPrets.isEmpty()) {
                throw new ExistingLoanException("Le livre "
                    + unLivreDTO.getIdLivre()
                    + " a été prêté.");
            }

            // Si le livre a été réservé
            List<ReservationDTO> listeDesReservations = getReservationDAO().findByLivre(connexion,
                unLivreDTO.getIdLivre(),
                ReservationDTO.ID_LIVRE_COLUMN_NAME);

            if(!listeDesReservations.isEmpty()) {
                throw new ExistingReservationException("Le livre "
                    + unLivreDTO.getIdLivre()
                    + " a été réservé.");
            }

            // Si le membre a atteint sa limite de prêt
            if(unMembreDTO.getNbPret() == unMembreDTO.getLimitePret()) {
                throw new InvalidLoanLimitException("Le membre "
                    + unMembreDTO.getIdMembre()
                    + " a atteint sa limite de prêt");
            }

            //Creation du pret

            unMembreDTO.setNbPret(unMembreDTO.getNbPret() + 1);
            getMembreDAO().update(connexion,
                unMembreDTO);

            PretDTO unPretDTO = new PretDTO();

            unPretDTO.setMembreDTO(unMembreDTO);
            unPretDTO.setLivreDTO(unLivreDTO);
            unPretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
            add(connexion,
                unPretDTO);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException.getMessage(),
                daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void terminer(Connexion connexion,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidPrimaryKeyException,
        MissingDTOException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        MissingLoanException,
        ExistingLoanException,
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

            LivreDTO unLivreDTO = (LivreDTO) getLivreDAO().get(connexion,
                pretDTO.getLivreDTO().getIdLivre());

            // Si le livre n'existe pas
            if(unLivreDTO == null) {
                throw new MissingDTOException("Le livre est inexistant");
            }

            // Si le livre n'a pas encore été prêté
            List<PretDTO> listeDesPrets = findByLivre(connexion,
                unLivreDTO.getIdLivre(),
                PretDTO.ID_PRET_COLUMN_NAME);
            if(listeDesPrets.isEmpty()) {
                throw new MissingLoanException("Le livre n'a pas encore été prêté.");
            }

            PretDTO unPretDTO = (PretDTO) getPretDAO().get(connexion,
                listeDesPrets.get(0).getIdPret());

            // Si le prêt n'existe pas
            if(unPretDTO == null) {
                throw new MissingDTOException("Le pret n'existe pas");
            }

            MembreDTO unMembreDTO = (MembreDTO) getMembreDAO().get(connexion,
                unPretDTO.getMembreDTO().getIdMembre());

            // Si le membre n'existe pas
            if(unMembreDTO == null) {
                throw new MissingDTOException("Le membre n'existe pas");
            }

            /*
            // Si le livre a été prêté à quelqu'un d'autre
            for(PretDTO emprunterDTO : listeDesPrets) {
                if(unMembreDTO.getIdMembre() != emprunterDTO.getMembreDTO().getIdMembre()) {
                    throw new ServiceException("Le livre est déjà prêté");
                }
            }
             */

            unPretDTO.setMembreDTO(unMembreDTO);
            unPretDTO.setLivreDTO(unLivreDTO);
            unMembreDTO.setNbPret(unMembreDTO.getNbPret() - 1);

            getMembreDAO().update(connexion,
                unMembreDTO);

            unPretDTO.setDateRetour(new Timestamp(System.currentTimeMillis()));

            update(connexion,
                unPretDTO);

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
