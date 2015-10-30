// Fichier LivreService.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.service.implementations;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.implementations.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.implementations.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.implementations.PretDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.implementations.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOClassException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.MissingDTOException;
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

    private LivreDAO livreDAO;

    private MembreDAO membreDAO;

    private ReservationDAO reservationDAO;

    private PretDAO pretDAO;

    /**
     *
     * Crée le service de la table <code>livre</code>.
     *
     * @param livreDAO - Le DAO de la table <code>livre</code>
     * @param membreDAO - Le DAO de la table <code>membre</code>
     * @param pretDAO - Le DAO de la table <code>pret</code>
     * @param reservationDAO - Le DAO de la table <code>reservation</code>
     */
    public LivreService(LivreDAO livreDAO, // TODO protéger le constructeur
        MembreDAO membreDAO,
        PretDAO pretDAO,
        ReservationDAO reservationDAO) {
        // TODO check des DAOs
        setLivreDAO(livreDAO);
        setReservationDAO(reservationDAO);
        setMembreDAO(membreDAO);
        setPretDAO(pretDAO);
    }

    /**
     *
     * Ajoute un nouveau livre.
     *
     * @param livreDTO - Le livre à ajouter.
     * @throws ServiceException S'il y a une erreur avec la base de données.
     * @throws InvalidDTOClassException
     * @throws InvalidDTOException
     * @throws InvalidHibernateSessionException
     */
    @Override
    public void add(Connexion connexion,
        LivreDTO livreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException {
        try {
            getLivreDAO().add(connexion,
                livreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     *
     * Lit un livre.
     *
     * @param idLivre - L'ID du livre à lire.
     * @return Le livre.
     * @throws ServiceException S'il y a une erreur avec la base de données.
     */
    public LivreDTO get(Connexion connexion,
        int idLivre) throws ServiceException {
        try {
            return getLivreDAO().get(connexion,
                idLivre);
        } catch(DAOException daoexception) {
            throw new ServiceException(daoexception);
        }
    }

    /**
     *
     * Met à jour un livre.
     *
     * @param livreDTO - Le livre à mettre à jour.
     * @throws ServiceException S'il y a une erreur avec la base de données.
     */
    @Override
    public void update(Connexion connexion,
        LivreDTO livreDTO) throws ServiceException {
        try {
            if(getLivreDAO().checkLivreExist(livreDTO.getIdLivre())) {
                throw new InvalidDTOException("Livre inexistant dans la base de données: "
                    + livreDTO.getTitre());
                return;
            }
            getLivreDAO().update(connexion,
                livreDTO);
        } catch(DAOException daoexception) {
            throw new ServiceException(daoexception);
        }
    }

    /**
     *
     * Supprime un livre.
     *
     * @param livreDTO - Le livre à supprimer.
     * @throws ServiceException S'il y a une erreur avec la base de données.
     * @throws InvalidDTOClassException
     * @throws InvalidDTOException
     * @throws InvalidHibernateSessionException
     */
    @Override
    public void delete(Connexion connexion,
        LivreDTO livreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException {
        try {
            getLivreDAO().delete(connexion,
                livreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     *
     * Trouve tous les livres.
     *
     * @return La liste des livres ; une liste vide sinon.
     * @throws ServiceException S'il y a une erreur avec la base de données.
     * @throws InvalidSortByPropertyException
     * @throws InvalidHibernateSessionException
     */
    @Override
    public List<LivreDTO> getAll(Connexion connexion,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidSortByPropertyException,
        ServiceException {
        try {
            return getLivreDAO().getAll(connexion,
                sortByPropertyName);
        } catch(DAOException daoexception) {
            throw new ServiceException(daoexception);
        }
    }

    /**
     *
     * Trouve les livres à partir d'un titre.
     *
     * @param titre - Le titre à utiliser.
     * @return La liste des livres correspondants ; une liste vide sinon.
     * @throws ServiceException S'il y a une erreur avec la base de données.
     * @throws InvalidSortByPropertyException
     * @throws InvalidCriterionException
     * @throws InvalidHibernateSessionException
     */
    @Override
    public List<LivreDTO> findByTitre(Connexion connexion,
        String titre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ServiceException {
        try {
            return getLivreDAO().findByTitre(connexion,
                titre,
                sortByPropertyName);
        } catch(DAOException daoexception) {
            throw new ServiceException(daoexception);
        }
    }

    /**
     *
     * Acquiert un livre.
     *
     * @param livreDTO - Le livre à ajouter.
     * @throws InvalidDTOClassException
     * @throws InvalidDTOException
     * @throws InvalidHibernateSessionException
     * @throws ServiceException S'il y a une erreur avec la base de données.
     */
    @Override
    public void acquerir(Connexion connexion,
        LivreDTO livreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException {
        try {
            getLivreDAO().add(connexion,
                livreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     *
     * Vendre un livre.
     *
     * @param livreDTO - Le livre à vendre.
     * @throws ServiceException S'il y a une erreur avec la base de données.
     */
    @Override
    public void vendre(Connexion connexion,
        LivreDTO livreDTO) throws ServiceException {
        // TODO check de DTO
        try {

            // Vérifie si le livre passé en paramètre existe dans la base de données.
            if(livreDTO == null) {
                // TODO passer le read ici dans les Services
                throw new MissingDTOException("Le livre n'existe pas.");
            }
            //TODO connexion check missing Hibernate SEssion
            // Vérifie si le livre passé en paramètre est prêté à un membre.
            // si le livre existe // pretDAO find by livre, si != null il est deja prêter // si pas prêter est-il reserver // sinnon vendre le livre

            if(!getPretDAO().findByLivre(livreDTO).isEmpty()) {
                throw new ServiceException("Le livre "
                    + livreDTO.getTitre()
                    + " est prêté au membre #"
                    + getPretDAO().findByLivre(livreDTO).get(0).getMembreDTO().getIdMembre());
            }

            // Vérifie si le livre passé en paramètre est réservé par un membre.
            if(getReservationDAO().get(connexion,
                livreDTO.getIdLivre()) != null) {
                throw new ServiceException("Le livre "
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

    /**
     *
     * Trouve les livres dont le prêt est en retard
     *
     * @param dateJour - La date du jour
     * @return List<LivreDTO> - La liste des livres en retard
     * @throws ServiceException - Si la date du jour est <code>null</code>
     */
    /*
    public List<LivreDTO> findPretsEnRetard(Date dateJour) throws ServiceException {

        // Vérifier si la date du jour est valide (exception sinon)
        if(dateJour != null) {
            try {
                return getLivreDAO().findPretsEnRetard(dateJour);
            } catch(DAOException daoexception) {
                throw new ServiceException(daoexception);
            }
        }
        throw new ServiceException("La date du jour ne peut être null");
    }
     */

    // GETTERS ET SETTERS
    /**
     * Getter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @return La variable d'instance <code>this.livreDAO</code>
     */
    private LivreDAO getLivreDAO() {
        return this.livreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @param livreDAO La valeur à utiliser pour la variable d'instance <code>this.livreDAO</code>
     */
    private void setLivreDAO(LivreDAO livreDAO) {
        this.livreDAO = livreDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @return La variable d'instance <code>this.membreDAO</code>
     */
    private MembreDAO getMembreDAO() {
        return this.membreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @param membreDAO La valeur à utiliser pour la variable d'instance <code>this.membreDAO</code>
     */
    private void setMembreDAO(MembreDAO membreDAO) {
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
    private ReservationDAO getReservationDAO() {
        return this.reservationDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @param reservationDAO La valeur à utiliser pour la variable d'instance <code>this.reservationDAO</code>
     */
    private void setReservationDAO(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.pretDAO</code>.
     *
     * @return La variable d'instance <code>this.pretDAO</code>
     */
    public PretDAO getPretDAO() {
        return this.pretDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.pretDAO</code>.
     *
     * @param pretDAO La valeur à utiliser pour la variable d'instance <code>this.pretDAO</code>
     */
    public void setPretDAO(PretDAO pretDAO) {
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
