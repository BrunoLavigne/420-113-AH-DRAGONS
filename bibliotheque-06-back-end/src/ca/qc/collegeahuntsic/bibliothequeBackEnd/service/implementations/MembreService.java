// Fichier MembreService.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliothequeBackEnd.service.implementations;

import java.util.ArrayList;
import java.util.List;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dao.interfaces.IMembreDAO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.MembreDTO;
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
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ServiceException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.service.interfaces.IMembreService;
import org.hibernate.Session;

/**
 * Service de la table <code>membre</code>.
 *
 * @author Dragons Vicieux
 */
public class MembreService extends Service implements IMembreService {

    private IMembreDAO membreDAO;

    /**
     * Crée le service de la table <code>membre</code>.
     *
     * @param membreDAO Le DAO de la table <code>membre</code>
     * @throws InvalidDAOException Si le DAO de membre est <code>null</code> ou si le DAO de réservation est <code>null</code>
     */
    public MembreService(IMembreDAO membreDAO) throws InvalidDAOException {
        super();
        if(membreDAO == null) {
            throw new InvalidDAOException("Le DAO de membre ne peut être null");
        }
        setMembreDAO(membreDAO);
    }

    // Opérations CRUD

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMembre(Session session,
        MembreDTO membreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ServiceException {
        try {
            getMembreDAO().add(session,
                membreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MembreDTO getMembre(Session session,
        String idMembre) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        ServiceException {
        try {
            return (MembreDTO) getMembreDAO().get(session,
                idMembre);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateMembre(Session session,
        MembreDTO membreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ServiceException {
        try {
            getMembreDAO().update(session,
                membreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteMembre(Session session,
        MembreDTO membreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ServiceException {
        try {
            getMembreDAO().delete(session,
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
    public List<MembreDTO> getAllMembres(Session session,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidSortByPropertyException,
        ServiceException {
        try {
            return (List<MembreDTO>) getMembreDAO().getAll(session,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MembreDTO> findMembresByNom(Session session,
        String nom,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        ServiceException {
        try {
            return getMembreDAO().findByNom(session,
                nom,
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
    public void inscrireMembre(Session session,
        MembreDTO membreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ServiceException {
        if(session == null) {
            throw new InvalidHibernateSessionException("La session Hibernate ne peut être null");
        }
        if(membreDTO == null) {
            throw new InvalidDTOException("Le membre ne peut être null");
        }

        addMembre(session,
            membreDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void desinscrireMembre(Session session,
        MembreDTO membreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ServiceException,
        ExistingLoanException,
        ExistingReservationException {
        if(session == null) {
            throw new InvalidHibernateSessionException("La session Hibernate ne peut être null");
        }
        if(membreDTO == null) {
            throw new InvalidDTOException("Le membre ne peut être null");
        }

        try {

            final List<PretDTO> prets = new ArrayList<>(membreDTO.getPrets());
            if(!prets.isEmpty()) {
                for(PretDTO pretDTO : prets) {
                    if(pretDTO.getDateRetour() == null) {
                        throw new ExistingLoanException("Le membre "
                            + membreDTO.getNom()
                            + " (ID du membre : "
                            + membreDTO.getIdMembre()
                            + ") a encore le livre "
                            + pretDTO.getLivreDTO().getTitre()
                            + " (ID du livre : "
                            + pretDTO.getLivreDTO().getIdLivre()
                            + ") en sa possesion");
                    }
                }
            }

            final List<ReservationDTO> reservations = new ArrayList<>(membreDTO.getReservations());
            if(!reservations.isEmpty()) {
                throw new ExistingReservationException("Le membre "
                    + membreDTO.getNom()
                    + " (ID du membre : "
                    + membreDTO.getIdMembre()
                    + ") a encore des réservations");
            }

            deleteMembre(session,
                membreDTO);

        } catch(Exception exception) {
            throw new ServiceException(exception);
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

}
