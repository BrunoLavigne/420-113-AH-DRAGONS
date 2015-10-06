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
 * @see {@link #Serialized Form Serialized Form}
 */
public class LivreService extends Services {

    private static final long serialVersionUID = 1L;

    private LivreDAO livreDAO;

    private MembreDAO membreDAO;

    private ReservationDAO reservationDAO;

    //Création d'une instance
    public LivreService(LivreDAO livreDAO,
        ReservationDAO reservationDAO,
        MembreDAO membreDAO) {
        // setCx(livre.getConnexion());
        setLivreDAO(livreDAO);
        setReservationDAO(reservationDAO);
        setMembreDAO(membreDAO);
    }

    /**
     *
     * Méthode appelant la fonction <code>add</code> de la classe <code>LivreDAO</code> permettant d'ajouter (d'acquérir) un objet <code>LivreDTO</code> à la base de données.
     *
     * @param livreDTO un objet de type <code>LivreDTO</code> à ajouter dans la base de données.
     * @throws ServiceException en cas d'erreur dans l'exécution de la méthode <code>add</code> de la classe <code>LivreDAO</code>
     */
    public void acquerir(LivreDTO livreDTO) throws ServiceException {
        try {
            // Vérification si le livre existe dans la base de données.
            if(getLivreDAO().read(livreDTO.getIdLivre()) != null) {
                throw new ServiceException("Le livre existe déjà: "
                    + livreDTO.getIdLivre());
            }
            // Ajout du livre s'il n'existe pas déjà.
            getLivreDAO().add(livreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     *
     * Méthode appelant la fonction <code>delete</code> de la classe <code>LivreDAO</code> permettant de retirer (de vendre) un objet <code>LivreDTO</code> de la base de données.
     *
     * @param LivreDTO un objet de type <code>LivreDTO</code> à retirer de la base de données.
     * @throws ServiceException en cas d'erreur dans l'exécution de la méthode <code>delete</code> de la classe <code>LivreDAO</code>
     */
    public void vendre(LivreDTO LivreDTO) throws ServiceException {
        try {
            // Vérifie si le livre passé en paramètre existe dans la base de données.
            if(getLivreDAO().read(LivreDTO.getIdLivre()) == null) {
                throw new ServiceException("Livre inexistant dans la base de données: "
                    + LivreDTO.getTitre());
            }
            // Vérifie si le livre passé en paramètre est prêté à un membre.
            if(getMembreDAO().read(LivreDTO.getIdMembre()) != null) {
                throw new ServiceException("Le livre "
                    + LivreDTO.getTitre()
                    + " est prêté au membre #"
                    + LivreDTO.getIdMembre());
            }
            // Vérifie si le livre passé en paramètre est réservé par un membre.
            if(getReservationDAO().read(LivreDTO.getIdLivre()) != null) {
                throw new ServiceException("Le livre "
                    + LivreDTO.getTitre()
                    + " est réservé.");
            }
            // Sinon, suppression du livre de la base de données.
            getLivreDAO().delete(LivreDTO);

        } catch(DAOException daoexception) {
            throw new ServiceException(daoexception);
        }
    }

    /**
     *
     * Méthode appelant la fonction <code>emprunter</code> de la classe <code>LivreDAO</code> permettant de mettre à jour (prêter) un objet <code>LivreDTO</code> de la base de données.
     *
     * @param LivreDTO Le livre à emprunter.
     * @throws ServiceException en cas d'erreur dans l'exécution de la méthode <code>emprunter</code> de la classe <code>LivreDAO</code>
     */
    public void emprunter(LivreDTO LivreDTO) throws ServiceException {
        try {
            getLivreDAO().emprunter(LivreDTO);
        } catch(DAOException daoexception) {
            throw new ServiceException(daoexception);
        }
    }

    /**
     *
     * Méthode appelant la fonction <code>retourner</code> de la classe <code>LivreDAO</code> permettant de mettre à jour (retourner) un objet <code>LivreDTO</code> de la base de données.
     *
     * @param LivreDTO Le livre à retourner.
     * @throws ServiceException en cas d'erreur dans l'exécution de la méthode <code>retourner</code> de la classe <code>LivreDAO</code>
     */
    public void retourner(LivreDTO LivreDTO) throws ServiceException {
        try {
            getLivreDAO().retourner(LivreDTO);
        } catch(DAOException daoexception) {
            throw new ServiceException(daoexception);
        }
    }

    /**
     *
     * Méthode appelant la fonction <code>update</code> de la classe <code>LivreDAO</code> permettant de mettre à jour (prêter ou retourner) un objet <code>LivreDTO</code> de la base de données.
     *
     * @param LivreDTO un objet de type <code>LivreDTO</code> à modifier (update) dans la base de données.
     * @throws ServiceException en cas d'erreur dans l'exécution de la méthode <code>update</code> de la classe <code>LivreDAO</code>
     */
    public void update(LivreDTO LivreDTO) throws ServiceException {
        try {
            // Vérifie si le livre passé en paramètre existe dans la base de données.
            if(getLivreDAO().read(LivreDTO.getIdLivre()) == null) {
                throw new ServiceException("Livre inexistant dans la base de données: "
                    + LivreDTO.getTitre());
            }
            getLivreDAO().update(LivreDTO);
        } catch(DAOException daoexception) {
            throw new ServiceException(daoexception);
        }
    }

    /**
     *
     * Méthode appelant la fonction <code>read</code> de la classe <code>LivreDAO</code> permettant la lecture d'un objet <code>LivreDTO</code> de la base de données.
     *
     * @param idLivre un entier représentant le numéro d'identification du livre à lire (read) dans la Base de données.
     * @return un objet de type <code>LivreDTO</code> représentant le livre lu dans la base de données.
     * @throws ServiceException en cas d'erreur dans l'exécution de la méthode <code>read</code> de la classe <code>LivreDAO</code>
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
     * Méthode appelant la fonction <code>getAll</code> de la classe <code>LivreDAO</code> retournant l'ensemble des livres contenus dans la base de données.
     *
     * @return un objet de type <code>List</code> contenant des objets <code>LivreDTO</code> représentant l'ensemble des livres contenus dans la base de données.
     * @throws ServiceException en cas d'erreur dans l'exécution de la méthode <code>getAll</code> de la classe <code>LivreDAO</code>
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
     * Méthode appelant la fonction <code>findByTitre</code> de la classe <code>LivreDAO</code> retournant les livres contenus dans la base de données ayant les mots du paramètre <code>Titre</code> dans leur titre.
     *
     * @param Titre un <code>String</code> contenant des mots à rechercher dans les titres de livre de la base de données.
     * @return un objet de type <code>List</code> contenant des objets <code>LivreDTO</code> représentant les livres contenus dans la base de données ayant les mots du paramètre <code>Titre</code> dans leur titre.
     * @throws ServiceException en cas d'erreur dans l'exécution de la méthode <code>findByTitre</code> de la classe <code>LivreDAO</code>
     */
    public List<LivreDTO> findByTitre(String Titre) throws ServiceException {
        try {
            return getLivreDAO().findByTitre(Titre);
        } catch(DAOException daoexception) {
            throw new ServiceException(daoexception);
        }
    }

    /**
     *
     * Méthode appelant la fonction <code>findByMembre</code> de la classe <code>LivreDAO</code> retournant les livres contenus dans la base de données ayant été prêtés à un membre particulier.
     *
     * @param MembreDTO un objet <code>MembreDTO</code> représentant le membre duquel on veut rechercher les livres prêtés.
     * @return un objet de type <code>List</code> contenant des objets <code>LivreDTO</code> représentant les livres contenus dans la base de données prêtés à un membre.
     * @throws ServiceException en cas d'erreur dans l'exécution de la méthode <code>findByMembre</code> de la classe <code>LivreDAO</code>
     */
    public List<LivreDTO> findByMembre(MembreDTO MembreDTO) throws ServiceException {
        try {
            return getLivreDAO().findByMembre(MembreDTO);
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
