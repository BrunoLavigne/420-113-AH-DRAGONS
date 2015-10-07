// Fichier LivreService.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 * Service de la table livre.
 *
 * @author Dragons Vicieux
 *
 * @see {@link #Serialized Serialized} {@link #Form Form}
 */
public class LivreService extends Services {

    private static final long serialVersionUID = 1L;

    private LivreDAO livreDAO;

    private MembreDAO membreDAO;

    private ReservationDAO reservationDAO;

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
     * @throws {@link #ServiceException ServiceException} - S'il y a une erreur avec la base de données.
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
     * @throws {@link #ServiceException ServiceException} - S'il y a une erreur avec la base de données.
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
     * @throws {@link #ServiceException ServiceException} - S'il y a une erreur avec la base de données.
     */
    public void update(LivreDTO livreDTO) throws ServiceException {
        try {
            if(getLivreDAO().checkLivreExist(livreDTO.getIdLivre())) {
                throw new ServiceException("Livre inexistant dans la base de données: "
                    + livreDTO.getTitre());
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
     * @throws {@link #ServiceException ServiceException} - S'il y a une erreur avec la base de données.
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
     * @throws {@link #ServiceException ServiceException} - S'il y a une erreur avec la base de données.
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
     * @throws {@link #ServiceException ServiceException} - S'il y a une erreur avec la base de données.
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
     * Trouve les livres à partir d'un membre.
     *
     * @param membreDTO - Le membre à utiliser.
     * @return La liste des livres correspondants ; une liste vide sinon.
     * @throws {@link #ServiceException ServiceException} - S'il y a une erreur avec la base de données.
     */
    public List<LivreDTO> findByMembre(MembreDTO membreDTO) throws ServiceException {
        try {
            return getLivreDAO().findByMembre(membreDTO);
        } catch(DAOException daoexception) {
            throw new ServiceException(daoexception);
        }
    }

    /**
     *
     * Acquiert un livre.
     *
     * @param livreDTO - Le livre à ajouter.
     * @throws {@link #ServiceException ServiceException} - S'il y a une erreur avec la base de données.
     */
    public void acquerir(LivreDTO livreDTO) throws ServiceException {
        try {
            if(getLivreDAO().checkLivreExist(livreDTO.getIdLivre())) {
                throw new ServiceException("Le livre existe déjà: "
                    + livreDTO.getIdLivre());
            }
            add(livreDTO);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     *
     * Emprunte un livre.
     *
     * @param livreDTO - Le livre à emprunter.
     * @throws {@link #ServiceException ServiceException} - S'il y a une erreur avec la base de données.
     */
    public void emprunter(LivreDTO livreDTO) throws ServiceException {
        try {
            getLivreDAO().emprunter(livreDTO);
        } catch(DAOException daoexception) {
            throw new ServiceException(daoexception);
        }
    }

    /**
     *
     * Retourne un livre.
     *
     * @param livreDTO - Le livre à retourner.
     * @throws {@link #ServiceException ServiceException} - S'il y a une erreur avec la base de données.
     */
    public void retourner(LivreDTO livreDTO) throws ServiceException {
        try {
            getLivreDAO().retourner(livreDTO);
        } catch(DAOException daoexception) {
            throw new ServiceException(daoexception);
        }
    }

    /**
     *
     * Vendre un livre.
     *
     * @param livreDTO - Le livre à vendre.
     * @throws {@link #ServiceException ServiceException} - S'il y a une erreur avec la base de données.
     */
    public void vendre(LivreDTO livreDTO) throws ServiceException {
        try {
            // Vérifie si le livre passé en paramètre existe dans la base de données.
            if(getLivreDAO().read(livreDTO.getIdLivre()) == null) {
                throw new ServiceException("Livre inexistant dans la base de données: "
                    + livreDTO.getTitre());
            }
            // Vérifie si le livre passé en paramètre est prêté à un membre.
            if(getMembreDAO().read(livreDTO.getIdMembre()) != null) {
                throw new ServiceException("Le livre "
                    + livreDTO.getTitre()
                    + " est prêté au membre #"
                    + livreDTO.getIdMembre());
            }
            // Vérifie si le livre passé en paramètre est réservé par un membre.
            if(getReservationDAO().read(livreDTO.getIdLivre()) != null) {
                throw new ServiceException("Le livre "
                    + livreDTO.getTitre()
                    + " est réservé.");
            }
            // Sinon, suppression du livre de la base de données.
            getLivreDAO().delete(livreDTO);

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
}
