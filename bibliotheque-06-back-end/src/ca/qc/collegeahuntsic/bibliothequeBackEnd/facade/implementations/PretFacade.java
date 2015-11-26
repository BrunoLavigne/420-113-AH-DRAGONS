// Fichier Facade.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-21

package ca.qc.collegeahuntsic.bibliothequeBackEnd.facade.implementations;

import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidCriterionValueException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dto.InvalidDTOClassException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dto.MissingDTOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.facade.FacadeException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.facade.InvalidServiceException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.InvalidLoanLimitException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.MissingLoanException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ServiceException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.facade.interfaces.IPretFacade;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.service.interfaces.IPretService;
import org.hibernate.Session;

/**
 * Facade pour interagir avec le service de pret.
 *
 * @author Dragons Vicieux
 */
public class PretFacade extends Facade implements IPretFacade {

    private IPretService pretService;

    // TODO: Change to package when switching to Spring
    /**
     * Crée la façade de la table <code>pret</code>.
     *
     * @param pretService Le service de la table <code>pret</code>
     * @throws InvalidServiceException Si le service de pret est <code>null</code>
     */
    PretFacade(IPretService pretService) throws InvalidServiceException {
        super();
        if(pretService == null) {
            throw new InvalidServiceException("Le service de pret ne peut être null");
        }
        setPretService(pretService);
    }

    // Region Getters and Setters

    /**
     * Setter de la variable d'instance <code>this.pretService</code>.
     *
     * @param pretService La valeur à utiliser pour la variable d'instance <code>this.pretService</code>
     */
    public void setPretService(IPretService pretService) {
        this.pretService = pretService;
    }

    /**
     * Getter de la variable d'instance <code>this.pretService</code>.
     *
     * @return La variable d'instance <code>this.pretService</code>
     */
    private IPretService getPretService() {
        return this.pretService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PretDTO getPret(Session session,
        String idPret) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        FacadeException {
        try {
            return getPretService().getPret(session,
                idPret);
        } catch(ServiceException serviceException) {
            throw new FacadeException(serviceException);
        }
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
        FacadeException {
        try {
            getPretService().commencerPret(session,
                pretDTO);
        } catch(
            ServiceException
            | InvalidPrimaryKeyException
            | MissingDTOException
            | InvalidCriterionException
            | InvalidCriterionValueException
            | InvalidSortByPropertyException
            | InvalidDTOClassException serviceException) {
            // TODO confirmer pourquoi il faut thrower toute ces exceptions ici
            throw new FacadeException(serviceException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void terminerPret(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        MissingLoanException,
        FacadeException {
        try {
            getPretService().terminerPret(session,
                pretDTO);
        } catch(
            ServiceException
            | InvalidPrimaryKeyException
            | MissingDTOException
            | InvalidCriterionException
            | InvalidCriterionValueException
            | InvalidSortByPropertyException
            | ExistingLoanException
            | InvalidDTOClassException serviceException) {
            // TODO confirmer pourquoi il faut thrower toute ces exceptions ici
            throw new FacadeException(serviceException);
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
        FacadeException {
        try {
            getPretService().renouvelerPret(session,
                pretDTO);
        } catch(
            ServiceException
            | InvalidPrimaryKeyException
            | MissingDTOException
            | InvalidCriterionException
            | InvalidCriterionValueException
            | InvalidSortByPropertyException
            | ExistingLoanException
            | InvalidDTOClassException serviceException) {
            // TODO confirmer pourquoi il faut thrower toute ces exceptions ici
            throw new FacadeException(serviceException);
        }
    }
}
