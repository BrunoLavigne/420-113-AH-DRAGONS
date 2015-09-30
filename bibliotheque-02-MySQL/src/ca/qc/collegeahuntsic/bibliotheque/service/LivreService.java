// Fichier LivreService.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.service;

import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.ConnexionException;
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

    private LivreDAO livre;

    private ReservationDAO reservation;

    private Connexion cx;

    //Création d'une instance
    public LivreService(LivreDAO livre,
        ReservationDAO reservation) {
        // setCx(livre.getConnexion());
        setLivre(livre);
        setReservation(reservation);
    }

    /**
     * Ajout d'un nouveau livre dans la base de données.
     * S'il existe déjà, une exception est levée.
     * @param idLivre
     * @param titre
     * @param auteur
     * @param dateAcquisition
     * @throws ServiceException
     * @throws ConnexionException
     */
    public void acquerir(int idLivre,
        String titre,
        String auteur,
        String dateAcquisition) throws ServiceException {

        //Vérifie si le livre existe  déjà
        try {
            if(getLivre().existe(idLivre)) {
                throw new ServiceException("Le livre existe déjà: "
                    + idLivre);
            }

            //Ajout du livre dans la table livre

            getLivre().acquerir(idLivre,
                titre,
                auteur,
                dateAcquisition);

            getCx().commit();

        } catch(DAOException daoException) {
            try {
                getCx().rollback();
            } catch(ConnexionException connexionException) {
                throw new ServiceException(connexionException);
            }
            throw new ServiceException(daoException);
        } catch(ConnexionException connexionException) {
            throw new ServiceException(connexionException);
        }

    }

    /**
     * Vente d'un livre
     * @param idLivre
     * @throws ServiceException
     * @throws ConnexionException
     */
    public void vendre(int idLivre) throws ServiceException,
        ConnexionException {
        try {
            LivreDTO tupleLivre = getLivre().getLivre(idLivre);
            if(tupleLivre == null) {
                throw new ServiceException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.getIdMembre() != 0) {
                throw new ServiceException("Le livre est "
                    + idLivre
                    + " prêté à "
                    + tupleLivre.getIdMembre());
            }
            if(getReservation().getReservationLivre(idLivre) != null) {
                throw new ServiceException("Le livre est "
                    + idLivre
                    + " réservé ");
            }

            // Suppression du livre
            int nb = getLivre().vendre(idLivre);
            if(nb == 0) {
                throw new ServiceException("Le livre est "
                    + idLivre
                    + " inexistant");
            }
            getCx().commit();

        } catch(ConnexionException connexionException) {
            getCx().rollback();
            throw new ServiceException(connexionException);
        } catch(Exception exception) {
            getCx().rollback();
            throw new ServiceException(exception);
        }

    }

    /**
     * Getter de la variable d'instance <code>this.livre</code>.
     *
     * @return La variable d'instance <code>this.livre</code>
     */
    public LivreDAO getLivre() {
        return this.livre;
    }

    /**
     * Setter de la variable d'instance <code>this.livre</code>.
     *
     * @param livre La valeur à utiliser pour la variable d'instance <code>this.livre</code>
     */
    private void setLivre(LivreDAO livre) {
        this.livre = livre;
    }

    /**
     * Getter de la variable d'instance <code>this.reservation</code>.
     *
     * @return La variable d'instance <code>this.reservation</code>
     */
    public ReservationDAO getReservation() {
        return this.reservation;
    }

    /**
     * Setter de la variable d'instance <code>this.reservation</code>.
     *
     * @param reservation La valeur à utiliser pour la variable d'instance <code>this.reservation</code>
     */
    private void setReservation(ReservationDAO reservation) {
        this.reservation = reservation;
    }

    /**
     * Getter de la variable d'instance <code>this.cx</code>.
     *
     * @return La variable d'instance <code>this.cx</code>
     */
    public Connexion getCx() {
        return this.cx;
    }

    /**
     *
     * TODO Auto-generated method javadoc
     *
     * @param livreDTO
     * @throws ServiceException
     */
    public void acquerir(LivreDTO livreDTO) throws ServiceException {
        /*if(read(livreDTO.getIdLivre()) != null)) {
            throw new ServiceException("Le livre " + livreDTO.getTitre() + " existe déjà.");
        }*/
    }

    public void emprunter(MembreDTO membreDTO,
        LivreDTO livreDTO) throws ServiceException {
    }
}
