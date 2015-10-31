// Fichier MembreService.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.service.implementations;

import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IMembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
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
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ServiceException;
import ca.qc.collegeahuntsic.bibliotheque.service.interfaces.IMembreService;

/**
 * Service de la table <code>membre</code>.
 */
public class MembreService extends Service implements IMembreService {

    private static final long serialVersionUID = 1L;

    private IMembreDAO membreDAO;

    private IReservationDAO reservationDAO;

    /**
     * Crée le service de la table <code>membre</code>
     *
     * @param membreDAO Le DAO de la table <code>membre</code>
     * @param reservationDAO Le DAO de la table <code>reservation</code>
     * @throws InvalidDAOException Si le DAO de membre est <code>null</code> ou si le DAO de réservation est <code>null</code>
     */
    public MembreService(IMembreDAO membreDAO,
        IReservationDAO reservationDAO) throws InvalidDAOException {
        setMembreDAO(membreDAO);
        setReservationDAO(reservationDAO);
    }

    // Opérations CRUD

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(Connexion connexion,
        MembreDTO membreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException {

        try {
            getMembreDAO().add(connexion,
                membreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MembreDTO get(Connexion connexion,
        String idMembre) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        ServiceException {

        try {
            return (MembreDTO) getMembreDAO().get(connexion,
                idMembre);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Connexion connexion,
        MembreDTO membreDTO) throws ServiceException,
        InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException {

        try {
            getMembreDAO().update(connexion,
                membreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Connexion connexion,
        MembreDTO membreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException {

        try {
            getMembreDAO().delete(connexion,
                membreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    // Opérations de recherche

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<MembreDTO> getAll(Connexion connexion,
        String sortByPropertyName) throws ServiceException,
        InvalidHibernateSessionException,
        InvalidSortByPropertyException {

        try {
            return (List<MembreDTO>) getMembreDAO().getAll(connexion,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    // Méthodes métiers

    /**
     * {@inheritDoc}
     */
    @Override
    public void inscrire(Connexion connexion,
        MembreDTO membreDTO) throws ServiceException,
        InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException {

        // Vérifier si la connexion est null
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion est null");
        }

        // Vérifier si le membre est null
        if(membreDTO == null) {
            throw new InvalidDTOException("Le membre est null");
        }

        // On peut ajouter le membre
        add(connexion,
            membreDTO);

    }

    @Override
    public void desinscrire(Connexion connexion,
        MembreDTO membreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        InvalidPrimaryKeyException,
        MissingDTOException,
        ExistingLoanException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ExistingReservationException,
        ServiceException {

        // Vérifier si la connexion est null
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion est null");
        }

        // Vérifier si le membre est null
        if(membreDTO == null) {
            throw new InvalidDTOException("Le membre est null");
        }

        try {

            // Si le membre n'existe pas
            if(get(connexion,
                membreDTO.getIdMembre()) == null) {
                throw new MissingDTOException("Le membre n'existe pas");
            }

            // Si le membre a encore des prêts
            if(get(connexion,
                membreDTO.getIdMembre()).getNbPret() > 0) {
                throw new ExistingLoanException("Le membre a encore des prêts");
            }

            // Si le membre a encore des réservations
            // vérif. pour membreDTO.getIdMembre() dans...faut-il faire un autre get()?
            // TODCHANGER LE NOM DE COLONNE (AVANT HIBERNATE) POUR 3ÈME ARG DE FINDBYMEMBRE...pour l'instant sert à rien
            if(!getReservationDAO().findByMembre(connexion,
                membreDTO.getIdMembre(),
                ReservationDTO.ID_RESERVATION_COLUMN_NAME).isEmpty()) {
                throw new ExistingReservationException("Le membre a encore des réservations");
            }

            // On peut supprimer le membre
            delete(connexion,
                membreDTO);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    // Getters & setters

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
     * @param membreDAO
     *            La valeur à utiliser pour la variable d'instance
     *            <code>this.membreDAO</code>
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
     * @param reservationDAO
     *            La valeur à utiliser pour la variable d'instance
     *            <code>this.reservationDAO</code>
     */
    private void setReservationDAO(IReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    @Override
    public List<MembreDTO> findByNom(Connexion connexion,
        String nom,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ServiceException {
        // TODO Auto-generated method stub
        return null;
    }
}
