// Fichier LivreService.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.util.Date;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.PretDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 * Service de la table <code>livre</code>.
 *
 * @author Dragons Vicieux
 *
 */
public class LivreService extends Services {

    private static final long serialVersionUID = 1L;

    private LivreDAO livreDAO;

    private MembreDAO membreDAO;

    private ReservationDAO reservationDAO;

    private PretDAO pretDAO;

    /**
     *
     * Crée le service de la table <code>livre</code>.
     *
     * @param livreDAO - Le DAO de la table <code>livre</code>.
     * @param membreDAO - Le DAO de la table <code>membre</code>.
     * @param reservationDAO - Le DAO de la table <code>reservation</code>.
     */
    public LivreService(LivreDAO livreDAO,
        MembreDAO membreDAO,
        ReservationDAO reservationDAO) {
        setLivreDAO(livreDAO);
        setReservationDAO(reservationDAO);
        setMembreDAO(membreDAO);
    }

    /**
     *
     * Ajoute un nouveau livre.
     *
     * @param livreDTO - Le livre à ajouter.
     * @throws ServiceException S'il y a une erreur avec la base de données.
     */
    public void add(LivreDTO livreDTO) throws ServiceException {
        try {
            getLivreDAO().add(livreDTO);
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
    public LivreDTO read(int idLivre) throws ServiceException {
        try {
            return getLivreDAO().read(idLivre);
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
    public void update(LivreDTO livreDTO) throws ServiceException {
        try {
            if(getLivreDAO().checkLivreExist(livreDTO.getIdLivre())) {
                System.err.println("Livre inexistant dans la base de données: "
                    + livreDTO.getTitre());
                return;
                //throw new ServiceException("SRV-0001");
            }
            getLivreDAO().update(livreDTO);
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
     */
    public void delete(LivreDTO livreDTO) throws ServiceException {
        try {
            getLivreDAO().delete(livreDTO);
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
     */
    public List<LivreDTO> getAll() throws ServiceException {
        try {
            return getLivreDAO().getAll();
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
     */
    public List<LivreDTO> findByTitre(String titre) throws ServiceException {
        try {
            return getLivreDAO().findByTitre(titre);
        } catch(DAOException daoexception) {
            throw new ServiceException(daoexception);
        }
    }

    /**
     *
     * Acquiert un livre.
     *
     * @param livreDTO - Le livre à ajouter.
     * @throws ServiceException S'il y a une erreur avec la base de données.
     */
    public void acquerir(LivreDTO livreDTO) throws ServiceException {
        try {
            if(getLivreDAO().read(livreDTO.getIdLivre()) == null) {
                add(livreDTO);
            } else {
                System.err.println("Le livre existe déjà: "
                    + livreDTO.getIdLivre());
                return;
                //throw new ServiceException("SRV-0002");
            }
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
    public void vendre(LivreDTO livreDTO) throws ServiceException {
        try {

            LivreDTO unLivreDTO;

            // Vérifie si le livre passé en paramètre existe dans la base de données.
            if(getLivreDAO().read(livreDTO.getIdLivre()) == null) {
                System.err.println("Livre inexistant dans la base de données: "
                    + livreDTO.getTitre());
                return;
            }

            unLivreDTO = getLivreDAO().read(livreDTO.getIdLivre());

            // Vérifie si le livre passé en paramètre est prêté à un membre.

            // TODO chercher si le livre a des prêt
            // FIND BY LIVRE sur pretDAO

            // si le livre existe // pretDAO find by livre, si != null il est deja prêter // si pas prêter est-il reserver // sinnon vendre le livre

            if(getMembreDAO().read(unLivreDTO.getIdMembre()) != null) {
                System.err.println("Le livre "
                    + livreDTO.getTitre()
                    + " est prêté au membre #"
                    + livreDTO.getIdMembre());
                return;
            }

            // Vérifie si le livre passé en paramètre est réservé par un membre.
            if(getReservationDAO().read(livreDTO.getIdLivre()) != null) {
                System.err.println("Le livre "
                    + livreDTO.getTitre()
                    + " est réservé.");
                return;
            }
            // Sinon, suppression du livre de la base de données.
            getLivreDAO().delete(unLivreDTO);

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
}
