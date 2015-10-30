// Fichier LivreService.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.service.implementations;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.ILivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IMembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IPretDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
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
import ca.qc.collegeahuntsic.bibliotheque.service.interfaces.ILivreService;

/**
 * Service de la table <code>livre</code>.
 *
 * @author Dragons Vicieux
 *
 */
public class LivreService extends Services implements ILivreService {

    private static final long serialVersionUID = 1L;

    private ILivreDAO livreDAO;

    private IMembreDAO membreDAO;

    private IReservationDAO reservationDAO;

    private IPretDAO pretDAO;

    /**
     *
     * Crée le service de la table <code>livre</code>.
     *
     * @param livreDAO - Le DAO de la table <code>livre</code>
     * @param membreDAO - Le DAO de la table <code>membre</code>
     * @param pretDAO - Le DAO de la table <code>pret</code>
     * @param reservationDAO - Le DAO de la table <code>reservation</code>
     * @throws InvalidDAOException - Si le DAO de livre est <code>null</code>, si le DAO de membre est <code>null</code>, si le DAO de prêt est <code>null</code> ou si le DAO de réservation est <code>null</code>
     */
    public LivreService(ILivreDAO livreDAO, // TODO passer a protected quand les facades seront prêtes
        IMembreDAO membreDAO,
        IPretDAO pretDAO,
        IReservationDAO reservationDAO) throws InvalidDAOException {
        if(livreDAO == null) {
            throw new InvalidDAOException("Le DAO de la table livre est null");
        }
        if(membreDAO == null) {
            throw new InvalidDAOException("Le DAO de la table membre est null");
        }
        if(pretDAO == null) {
            throw new InvalidDAOException("Le DAO de la table pret est null");
        }
        if(reservationDAO == null) {
            throw new InvalidDAOException("Le DAO de la table reservation est null");
        }
        setLivreDAO(livreDAO);
        setReservationDAO(reservationDAO);
        setMembreDAO(membreDAO);
        setPretDAO(pretDAO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(Connexion connexion,
        LivreDTO livreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion n'existe pas.");
        }
        if(livreDTO == null) {
            throw new InvalidDTOException("Le livre n'existe pas.");
        }
        try {
            getLivreDAO().add(connexion,
                livreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LivreDTO get(Connexion connexion,
        String idLivre) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        ServiceException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null.");
        }
        if(idLivre == null) {
            throw new InvalidPrimaryKeyException("La clé ne peut être null.");
        }
        try {
            return (LivreDTO) getLivreDAO().get(connexion,
                idLivre);
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
        LivreDTO livreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion n'existe pas.");
        }
        if(livreDTO == null) {
            throw new InvalidDTOException("Le livre n'existe pas.");
        }
        try {
            getLivreDAO().update(connexion,
                livreDTO);
        } catch(DAOException daoexception) {
            throw new ServiceException(daoexception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Connexion connexion,
        LivreDTO livreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion n'existe pas.");
        }
        if(livreDTO == null) {
            throw new InvalidDTOException("Le livre n'existe pas.");
        }
        try {
            getLivreDAO().delete(connexion,
                livreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<LivreDTO> getAll(Connexion connexion,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidSortByPropertyException,
        ServiceException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion n'existe pas.");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété de recherche est null.");
        }
        try {
            return (List<LivreDTO>) getLivreDAO().getAll(connexion,
                sortByPropertyName);
        } catch(DAOException daoexception) {
            throw new ServiceException(daoexception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LivreDTO> findByTitre(Connexion connexion,
        String titre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ServiceException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion n'existe pas.");
        }
        if(titre == null) {
            throw new InvalidCriterionException("Le titre à recherché est null.");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété de recherche est null.");
        }
        try {
            return getLivreDAO().findByTitre(connexion,
                titre,
                sortByPropertyName);
        } catch(DAOException daoexception) {
            throw new ServiceException(daoexception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void acquerir(Connexion connexion,
        LivreDTO livreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion n'existe pas.");
        }
        if(livreDTO == null) {
            throw new InvalidDTOException("Le livre n'existe pas.");
        }
        try {
            getLivreDAO().add(connexion,
                livreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void vendre(Connexion connexion,
        LivreDTO livreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        InvalidPrimaryKeyException,
        MissingDTOException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ExistingLoanException,
        ExistingReservationException,
        ServiceException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null.");
        }
        if(livreDTO == null) {
            throw new InvalidDTOException("La connexion ne peut être null.");
        }
        try {
            // Vérifie si le livre passé en paramètre est prêté à un membre.
            if(!getPretDAO().findByLivre(connexion,
                livreDTO.getTitre(),
                LivreDTO.TITRE_COLUMN_NAME).isEmpty()) {
                throw new ExistingLoanException("Le livre "
                    + livreDTO.getTitre()
                    + " est prêté au membre #"
                    + getPretDAO().findByLivre(connexion,
                        livreDTO.getTitre(),
                        LivreDTO.TITRE_COLUMN_NAME).get(0).getMembreDTO().getIdMembre());
            }

            // Vérifie si le livre passé en paramètre est réservé par un membre.
            if(getReservationDAO().get(connexion,
                livreDTO.getIdLivre()) != null) {
                throw new ExistingReservationException("Le livre "
                    + livreDTO.getTitre()
                    + " est réservé.");
            }
            // Sinon, suppression du livre de la base de données.
            getLivreDAO().delete(connexion,
                livreDTO);

        } catch(DAOException daoexception) {
            throw new ServiceException(daoexception);
        }
    }

    // GETTERS ET SETTERS
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
        // TODO THIS LINE DOES BASICALLY NOTHING BUT BLOCKS THE METHOD getMembreDAO() FROM BEING DELETED ON SAVE
        // OTHERWISE THE SAID METHOD WAS UNUSED LOCALLY AND WOULD BE DELETED.
        blockingmethodfromdeletion();
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

    // THIS THING DOES NOTHING BUT MAKE A USELESS "USE" OF getMembreDAO() SO THAT THAT METHOD IS NOT DELETED ON SAVE.
    private void blockingmethodfromdeletion() {
        try(
            PrintStream prout = new PrintStream(new FileOutputStream("NUL:"));) {
            prout.println(getMembreDAO());
        } catch(FileNotFoundException exception) {
            // TODO Auto-generated catch block
            exception.printStackTrace();
        }

    }
}
