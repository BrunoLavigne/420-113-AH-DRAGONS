// Fichier MembreService.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.ConnexionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 * Gestion des transactions reliées à la création et
 * la suppression des membres dans une bibliothèque.
 *
 * Ce programme permet de gérer les transactions reliées à la
 * création et la suppression des membres.
 *
 *<pre>
 * Pré-condition:
 *   la base de données de la bibliothèque doit exister
 *</pre>
 *
 *<post>
 * Post-condition:
 *   le programme effectue les *mises à jour* associées à chaque transaction
 * </post>
 */
public class MembreService extends Services {

    private static final long serialVersionUID = 1L;

    private MembreDAO membre;

    private ReservationDAO reservation;

    /**
     * Création d'une instance
     *
     * @param membre
     * @param reservation
     */
    public MembreService(MembreDAO membre,
        ReservationDAO reservation) {

        setCx(membre.getConnexion());
        setMembre(membre);
        setReservation(reservation);
    }

    /**
     *
     * Vérifie si un membre existe
     *
     * @param idMembre
     * @return
     * @throws ServiceException
     */
    public boolean existe(int idMembre) throws ServiceException {

        return(getMembre().read(idMembre) != null);

    }

    /**
     * @TODO [test comment]
     */
    public void inscrire(int idMembre,
        String nom,
        long telephone,
        int limitePret) throws ServiceException {

        if(!existe(idMembre)) {
            MembreDAO nouveauMembre = new MembreDAO(getMembre());
        }

        getMembre().add(nouveauMembre);

        try {

            getStmtInsert().setInt(1,
                idMembre);
            getStmtInsert().setString(2,
                nom);
            getStmtInsert().setLong(3,
                telephone);
            getStmtInsert().setInt(4,
                limitePret);
            getStmtInsert().executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    public void emprunter(MembreDTO unLivreDTO) {

    }

    /**
     * Ajout d'un nouveau membre dans la base de données.
     * Si elle existe déjà, une exception est levée.
     *
     * @param idMembre
     * @param nom
     * @param telephone
     * @param limitePret
     * @throws ServiceException
     * @throws ConnexionException
     */
    public void inscrire(int idMembre,
        String nom,
        long telephone,
        int limitePret) throws ServiceException {
        try {
            // Vérifie si le membre existe déjà
            if(getMembre().existe(idMembre)) {
                throw new ServiceException("Membre existe deja: "
                    + idMembre);
            }

            // Ajout du membre
            getMembre().inscrire(idMembre,
                nom,
                telephone,
                limitePret);
            getCx().commit();

        } catch(Exception exception) {
            try {
                getCx().rollback();
            } catch(ConnexionException connexionException) {
                throw new ServiceException(connexionException);
            }
            throw new ServiceException(exception);
        }
    }

    /**
     * Suppression d'un membre dans la base de données.
     *
     * @param idMembre
     * @throws ServiceException
     */
    public void desinscrire(int idMembre) throws ServiceException {
        try {
            // Vérifie si le membre existe et s'il a encore des prêts en cours
            MembreDTO tupleMembre = getMembre().getMembre(idMembre);
            if(tupleMembre == null) {
                throw new ServiceException("Membre inexistant: "
                    + idMembre);
            }
            if(tupleMembre.getNbPret() > 0) {
                throw new ServiceException("Le membre "
                    + idMembre
                    + " a encore des prêts.");
            }
            if(getReservation().getReservationMembre(idMembre) != null) {
                throw new ServiceException("Membre "
                    + idMembre
                    + " a des réservations");
            }

            /* Suppression du membre */
            int nb = getMembre().desinscrire(idMembre);
            if(nb == 0) {
                throw new ServiceException("Membre "
                    + idMembre
                    + " inexistant");
            }
            getCx().commit();

        } catch(Exception exception) {
            try {
                getCx().rollback();
            } catch(ConnexionException connexionException) {
                throw new ServiceException(connexionException);
            }
            throw new ServiceException(exception);
        }

    }

    /**
     * Getter de la variable d'instance <code>this.membre</code>.
     *
     * @return La variable d'instance <code>this.membre</code>
     */
    public MembreDAO getMembre() {
        return this.membre;
    }

    /**
     * Setter de la variable d'instance <code>this.membre</code>.
     *
     * @param membre La valeur à utiliser pour la variable d'instance <code>this.membre</code>
     */
    private void setMembre(MembreDAO membre) {
        this.membre = membre;
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
     * Setter de la variable d'instance <code>this.cx</code>.
     *
     * @param cx La valeur à utiliser pour la variable d'instance <code>this.cx</code>
     */
    private void setCx(Connexion cx) {
        this.cx = cx;
    }
    
    /**
     * [Générer commentaires]
     */
    private void add (MembreDTO membreDTO) throws ServiceException {
    	
    	if(true) {
    		
    	} else {
    		
    	}
        try {
            getMembreDAO().add(membreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }
    
    /**
     * [Générer commentaires]
     */
    private MembreDTO read(int idMembre) throws SericeException {
    	
    }
    
    /**
     * [Générer commentaires]
     */
    private void update (MembreDTO membreDTO) throws ServiceException {
    	
    }
    
    /**
     * [Générer commentaires]
     */
    private void delete (MembreDTO membreDTO) throws ServiceException {
    	
    }

}
