// Fichier LivreService.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.service;

import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 * Gestion des transactions reliées à la création et
 * la suppression de livres dans une bibliothèque.
 *
 * Ce programme permet de gérer les transaction reliées à la
 * création et à la suppression de livres.
 *
 *<pre>
 * Pré-condition:
 *   la base de données de la bibliothèque doit exister
 *</pre>
 *
 *<post>
 * Post-condition:
 *   le programme effectue les majuscules associées à chaque
 *   transaction
 * </post>
 *
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
     * @param LivreDTO
     * @throws ServiceException
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
     *
     * TODO Auto-generated method javadoc
     *
     * @param livreDTO
     * @throws ServiceException
     */
    /* public void acquerir(LivreDTO livreDTO) throws ServiceException {
        if(read(livreDTO.getIdLivre()) != null)) {
            throw new ServiceException("Le livre " + livreDTO.getTitre() + " existe déjà.");

    }}*/

    /*public void emprunter(MembreDTO membreDTO,
        LivreDTO livreDTO) throws ServiceException {
    }*/
}
