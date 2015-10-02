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

    /**
     *
     * TODO Auto-generated method javadoc
     *
     * @param livreDTO
     * @throws ServiceException
     */
    public void add(LivreDTO livreDTO) throws ServiceException {
        try {
            getLivreDAO().add(livreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

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
     * Ajout d'un nouveau livre dans la base de données.
     * S'il existe déjà, une exception est levée.
     * @param idLivre
     * @param titre
     * @param auteur
     * @param dateAcquisition
     * @throws ServiceException
     */
    public void acquerir(LivreDTO livreDTO) throws DAOException {

        //Vérifie si le livre existe  déjà
        try {
            if(getLivreDAO().read(livreDTO.getIdLivre()) != null) {
                throw new ServiceException("Le livre existe déjà: "
                    + livreDTO.getIdLivre());
            }

            //Ajout du livre dans la table livre
            getLivreDAO().add(livreDTO);

            //getCx().commit();

        } catch(ServiceException serviceException) {
            throw new DAOException(serviceException);
        }
    }

    /**
     * Vente d'un livre
     * @param idLivre
     * @throws ServiceException
     */
    public void vendre(LivreDTO livreDTO) throws DAOException {
        try {

            if(livreDTO == null) {
                throw new DAOException("Livre inexistant: "
                    + livreDTO);
            }
            //if(livreDTO.getIdMembre() != 0) {
            if(getMembreDAO().read(livreDTO.getIdMembre()) != null) {
                throw new DAOException("Le livre est "
                    + livreDTO.getIdLivre()
                    + " prêté à "
                    + livreDTO.getIdMembre());
            }
            if(getReservationDAO().read(livreDTO.getIdLivre()) != null) {
                throw new DAOException("Le livre est "
                    + livreDTO.getIdLivre()
                    + " réservé ");
            }

            // Suppression du livre
            getLivreDAO().delete(livreDTO);
            /*if(nb == 0) {
                throw new DAOException("Le livre est "
                    + livreDTO.getTitre()
                    + " inexistant");
            }*/

            //getCx().commit();

        }
        //getCx().rollback();

        catch(Exception exception) {
            // getCx().rollback();
            throw new DAOException(exception);
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
