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
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidDAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidLoanLimitException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.MissingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ServiceException;
import ca.qc.collegeahuntsic.bibliotheque.service.interfaces.IReservationService;

/**
 *
 * Service de la table <code>reservation</code>.
 *
 * @author Dragons Vicieux
 */
public class ReservationService extends Service implements IReservationService {

    private static final long serialVersionUID = 1L;

    private ILivreDAO livreDAO;

    private IMembreDAO membreDAO;

    private IReservationDAO reservationDAO;

    private IPretDAO pretDAO;

    /**
     * Crée le service de la table <code>reservation</code>.
     *
     * @param livreDAO Le DAO de la table <code>livre</code>
     * @param membreDAO Le DAO de la table <code>membre</code>
     * @param reservationDAO Le DAO de la table <code>reservation</code>
     * @throws InvalidDAOException Si le DAO de réservation est <code>null</code>, si le DAO de membre est <code>null</code>, si le DAO de livre est <code>null</code> ou si le DAO de prêt est <code>null</code>
     */
    public ReservationService(ILivreDAO livreDAO,
        IMembreDAO membreDAO,
        IReservationDAO reservationDAO,
        IPretDAO pretDAO) throws InvalidDAOException {

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

            MembreDTO unMembreDTO = (MembreDTO) getMembreDAO().get(connexion,
                reservationDTO.getMembreDTO().getIdMembre());

            //  Si le membre n'existe pas
            if(unMembreDTO == null) {
                throw new MissingDTOException("Le membre numéro "
                    + reservationDTO.getMembreDTO().getIdMembre()
                    + " n'existe pas");
            }

            // Vérification sur le livre

            LivreDTO unLivreDTO = (LivreDTO) getLivreDAO().get(connexion,
                reservationDTO.getLivreDTO().getIdLivre());

            if(unLivreDTO == null) {
                throw new MissingDTOException("Le livre numéro "
                    + reservationDTO.getLivreDTO().getIdLivre()
                    + " n'existe pas.");
            }

            // Si le livre n'a pas encore été prêté,
            List<PretDTO> listeDesPrets = getPretDAO().findByLivre(connexion,
                unLivreDTO.getIdLivre(),
                PretDTO.ID_PRET_COLUMN_NAME);

            if(listeDesPrets.isEmpty()) {
                throw new MissingLoanException("Le livre : "
                    + unLivreDTO.getIdLivre()
                    + " n'a pas été prêté encore. Faire un emprunt au lieu d'un réservation");

            }

            // Si le livre est déjà prêté au membre
            for(PretDTO unPretDTO : listeDesPrets) {
                if(unMembreDTO.equals(unPretDTO.getMembreDTO())) {
                    throw new ExistingLoanException("Le livre "
                        + unLivreDTO.getIdLivre()
                        + " est déjà prêté");
                }
            }

            // si le membre a déjà réservé ce livre
            List<ReservationDTO> listeReservation = findByMembre(connexion,
                unMembreDTO.getIdMembre(),
                MembreDTO.ID_MEMBRE_COLUMN_NAME);

            for(ReservationDTO reservation : listeReservation) {
                if(reservation.getLivreDTO().equals(unLivreDTO)) {
                    throw new ExistingReservationException("Le livre : "
                        + unLivreDTO.getIdLivre()
                        + " a déjà été réservé par le membre : "
                        + reservation.getMembreDTO().getIdMembre());
                }
            }

            ReservationDTO uneReservationDTO = new ReservationDTO();

            uneReservationDTO.setMembreDTO(unMembreDTO);
            uneReservationDTO.setLivreDTO(unLivreDTO);

            //Création de la réservation
            uneReservationDTO.setDateReservation(new Timestamp(System.currentTimeMillis()));

            add(connexion,
                uneReservationDTO);

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

            if(connexion == null) {
                throw new InvalidHibernateSessionException("La connexion na pas pu être établie");
            }

            if(reservationDTO == null) {
                throw new InvalidDTOException("Le DTO de la réservation ne peut pas être null");
            }

            ReservationDTO uneReservationDTO = (ReservationDTO) getReservationDAO().get(connexion,
                reservationDTO.getIdReservation());

            // Si la réservation existe déjà
            if(uneReservationDTO == null) {
                throw new InvalidCriterionException("La réservation numéro "
                    + reservationDTO.getIdReservation()
                    + " n'existe pas");
            }

            MembreDTO unMembreDTO = new MembreDTO();
            unMembreDTO = (MembreDTO) getMembreDAO().get(connexion,
                uneReservationDTO.getMembreDTO().getIdMembre());

            //  Si le membre n'existe pas
            if(unMembreDTO == null) {
                throw new InvalidCriterionException("Le membre numéro "
                    + uneReservationDTO.getMembreDTO().getIdMembre()
                    + " n'existe pas");
            }

            LivreDTO unLivreDTO = new LivreDTO();
            unLivreDTO = (LivreDTO) getLivreDAO().get(connexion,
                uneReservationDTO.getLivreDTO().getIdLivre());

            // Vérification sur le livre
            if(unLivreDTO == null) {
                throw new InvalidCriterionException("Le livre numéro "
                    + uneReservationDTO.getLivreDTO().getIdLivre()
                    + " n'existe pas");
            }

            // Si la réservation n'est pas la première de la liste
            List<ReservationDTO> listeReservations = findByLivre(connexion,
                unLivreDTO.getIdLivre(),
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
                unLivreDTO.getIdLivre(),
                ReservationDTO.ID_RESERVATION_COLUMN_NAME);

            if(!listeDesPret.isEmpty()) {
                PretDTO pretDTO = listeDesPret.get(0);
                throw new ExistingLoanException("Livre "
                    + uneReservationDTO.getLivreDTO().getIdLivre()
                    + " déjà prêté à "
                    + pretDTO.getMembreDTO().getIdMembre());
            }

            // Si le membre a atteint sa limite de prêt
            if(unMembreDTO.getNbPret() >= unMembreDTO.getLimitePret()) {
                throw new InvalidLoanLimitException("Limite de prêt du membre "
                    + uneReservationDTO.getMembreDTO().getIdMembre()
                    + " atteinte");
            }

            // Éliminer la réservation.

            unMembreDTO.setNbPret(unMembreDTO.getNbPret() + 1);
            getMembreDAO().update(connexion,
                unMembreDTO);

            PretDTO unPretDTO = new PretDTO();
            unPretDTO.setMembreDTO(unMembreDTO);
            unPretDTO.setLivreDTO(unLivreDTO);
            unPretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));

            getPretDAO().add(connexion,
                unPretDTO);

            uneReservationDTO.setLivreDTO(unLivreDTO);
            uneReservationDTO.setMembreDTO(unMembreDTO);

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

        try {

            if(connexion == null) {
                throw new InvalidHibernateSessionException("La connexion na pas pu être établie");
            }

            if(reservationDTO == null) {
                throw new InvalidDTOException("Le DTO de la réservation ne peut pas être null");
            }

            ReservationDTO uneReservationDTO = (ReservationDTO) getReservationDAO().get(connexion,
                reservationDTO.getIdReservation());

            if(uneReservationDTO == null) {
                throw new InvalidDTOException("La réservation "
                    + reservationDTO.getIdReservation()
                    + " n'existe pas");
            }

            delete(connexion,
                uneReservationDTO);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException.getMessage(),
                daoException);
        }

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
