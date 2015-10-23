// Fichier BibliothequeCreateur.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.util;

import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.PretDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.exception.BibliothequeException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ConnexionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;
import ca.qc.collegeahuntsic.bibliotheque.service.LivreService;
import ca.qc.collegeahuntsic.bibliotheque.service.MembreService;
import ca.qc.collegeahuntsic.bibliotheque.service.PretService;
import ca.qc.collegeahuntsic.bibliotheque.service.ReservationService;

/**
 *
 * Utilitaire de création des outils de la bibliothèque.
 *
 * @author Dragons Vicieux
 */
public class BibliothequeCreateur {

    private Connexion connexion;

    private LivreDAO livreDAO;

    private MembreDAO membreDAO;

    private PretDAO pretDAO;

    private ReservationDAO reservationDAO;

    private LivreService livreService;

    private MembreService membreService;

    private PretService pretService;

    private ReservationService reservationService;

    /**
     * Crée les services nécessaires à l'application bibliothèque.
     *
     * @param typeServeur - Type de serveur SQL de la BD
     * @param schema - Nom du schéma de la base de données
     * @param nomUtilisateur - Nom d'utilisateur sur le serveur SQL
     * @param motPasse - Mot de passe sur le serveur SQL
     * @throws BibliothequeException - S'il y a une erreur avec la base de données
     *
     */
    public BibliothequeCreateur(String typeServeur,
        String schema,
        String nomUtilisateur,
        String motPasse) throws BibliothequeException {
        // allocation des objets pour le traitement des transaction

        try(
            Connexion connection = new Connexion(typeServeur,
                schema,
                nomUtilisateur,
                motPasse)) {

            setConnexion(connection);
            //System.out.println(connexion.toString());

            LivreDAO unLivreDAO = new LivreDAO(getConnexion());
            //System.out.println(livreDAO.getConnection().toString());
            MembreDAO unMembreDAO = new MembreDAO(getConnexion());
            PretDAO unPretDAO = new PretDAO(getConnexion());
            ReservationDAO uneReservationDAO = new ReservationDAO(getConnexion());

            setLivreDAO(unLivreDAO);
            setMembreDAO(unMembreDAO);
            setPretDAO(unPretDAO);
            setReservationDAO(uneReservationDAO);

            setLivreService(new LivreService(getLivreDAO(),
                getMembreDAO(),
                getPretDAO(),
                getReservationDAO()));
            setMembreService(new MembreService(getMembreDAO(),
                getLivreDAO(),
                getReservationDAO()));
            setPretService(new PretService(getPretDAO(),
                getMembreDAO(),
                getLivreDAO(),
                getReservationDAO()));
            setReservationService(new ReservationService(getLivreDAO(),
                getMembreDAO(),
                getReservationDAO(),
                getPretDAO()));

        } catch(ServiceException serviceException) {
            throw new BibliothequeException(serviceException);

        } catch(ConnexionException connexionException) {
            throw new BibliothequeException(connexionException);

        } catch(DAOException daoException) {
            throw new BibliothequeException(daoException);
        } catch(Exception exception) {
            throw new BibliothequeException(exception);
        }

    }

    /**
     * Ferme la connexion
     *
     * @throws BibliothequeException S'il y a une erreur avec la base de données
     */
    public void fermer() throws BibliothequeException {
        // fermeture de la connexion
        try {
            getConnexion().fermer();
        } catch(ConnexionException connexionException) {
            throw new BibliothequeException(connexionException);
        }
    }

    /**
     * Getter de la variable d'instance <code>this.connexion</code>.
     *
     * @return La variable d'instance <code>this.connexion</code>
     */
    public Connexion getConnexion() {
        return this.connexion;
    }

    /**
     * Setter de la variable d'instance <code>this.connexion</code>.
     *
     * @param connexion La valeur à utiliser pour la variable d'instance <code>this.connexion</code>
     */
    public void setConnexion(Connexion connexion) {
        this.connexion = connexion;
    }

    /**
     * Getter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @return La variable d'instance <code>this.livreDAO</code>
     */
    public LivreDAO getLivreDAO() {
        return this.livreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @param livreDAO La valeur à utiliser pour la variable d'instance <code>this.livreDAO</code>
     */
    public void setLivreDAO(LivreDAO livreDAO) {
        this.livreDAO = livreDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @return La variable d'instance <code>this.membreDAO</code>
     */
    public MembreDAO getMembreDAO() {
        return this.membreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @param membreDAO La valeur à utiliser pour la variable d'instance <code>this.membreDAO</code>
     */
    public void setMembreDAO(MembreDAO membreDAO) {
        this.membreDAO = membreDAO;
    }

    public PretDAO getPretDAO() {
        return this.pretDAO;
    }

    public void setPretDAO(PretDAO pretDAO) {
        this.pretDAO = pretDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @return La variable d'instance <code>this.reservationDAO</code>
     */
    public ReservationDAO getReservationDAO() {
        return this.reservationDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @param reservationDAO La valeur à utiliser pour la variable d'instance <code>this.reservationDAO</code>
     */
    public void setReservationDAO(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.livreService</code>.
     *
     * @return La variable d'instance <code>this.livreService</code>
     */
    public LivreService getLivreService() {
        return this.livreService;
    }

    /**
     * Setter de la variable d'instance <code>this.livreService</code>.
     *
     * @param livreService La valeur à utiliser pour la variable d'instance <code>this.livreService</code>
     */
    public void setLivreService(LivreService livreService) {
        this.livreService = livreService;
    }

    /**
     * Getter de la variable d'instance <code>this.membreService</code>.
     *
     * @return La variable d'instance <code>this.membreService</code>
     */
    public MembreService getMembreService() {
        return this.membreService;
    }

    /**
     * Setter de la variable d'instance <code>this.membreService</code>.
     *
     * @param membreService La valeur à utiliser pour la variable d'instance <code>this.membreService</code>
     */
    public void setMembreService(MembreService membreService) {
        this.membreService = membreService;
    }

    /**
     * Getter de la variable d'instance <code>this.pretService</code>.
     *
     * @return La variable d'instance <code>this.pretService</code>
     */
    public PretService getPretService() {
        return this.pretService;
    }

    /**
     * Setter de la variable d'instance <code>this.pretService</code>.
     *
     * @param pretService La valeur à utiliser pour la variable d'instance <code>this.pretService</code>
     */
    public void setPretService(PretService pretService) {
        this.pretService = pretService;
    }

    /**
     * Getter de la variable d'instance <code>this.reservationService</code>.
     *
     * @return La variable d'instance <code>this.reservationService</code>
     */
    public ReservationService getReservationService() {
        return this.reservationService;
    }

    /**
     * Setter de la variable d'instance <code>this.reservationService</code>.
     *
     * @param reservationService La valeur à utiliser pour la variable d'instance <code>this.reservationService</code>
     */
    public void setReservationService(ReservationService reservationService) {
        this.reservationService = reservationService;
    }
}
