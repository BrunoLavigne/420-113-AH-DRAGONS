
package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.ConnexionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 * Gestion des transactions reliées aux prêts de livres
 * aux membres dans une bibliothèque.
 *
 * Ce programme permet de gérer les transactions prêtées,
 * rénouveller et retourner.
 *
 * <pre>
 * Pré-condition
 *   la base ded données de la bibliothèque doit exister
 * </pre>
 * <pos>
 * Post-condition
 *   le programme effectue les maj associées à chaque
 *   transaction
 * </pos>
 */

public class PretService extends Services {

    private static final long serialVersionUID = 1L;

    private LivreDAO livreDAO;

    private MembreDAO membreDAO;

    private ReservationDAO reservationDAO;

    private Connexion cx;

    /**
     * Création d'une instance.
     * La connection de l'instance de livre et de membre doit être la même que cx,
     * afin d'assurer l'intégrité des transactions.
     *
     * @param livreDAO
     * @param membreDAO
     * @param reservationDAO
     * @throws ServiceException
     */
    public PretService(LivreDAO livreDAO,
        MembreDAO membreDAO,
        ReservationDAO reservationDAO) throws ServiceException {

        // This whole shit is useless since we now have a dedicated class for a single connection used by
        // all DAOs...

        /*
        if(livreDAO.getConnexion() != membreDAO.getConnexion()
            || reservationDAO.getConnexion() != membreDAO.getConnexion()) {
            throw new ServiceException("Les instances de livre, de membre et de réservation n'utilisent pas la même connexion au serveur");
        }
        setCx(livreDAO.getConnexion());
         */

        setLivreDAO(livreDAO);
        setMembreDAO(membreDAO);
        setReservationDAO(reservationDAO);
    }

    /**
     * Prêt d'un livre à un membre.
     * Le livre ne doit pas être prêté.
     * Le membre ne doit pas avoir dépasser sa limite de prêt.
     *
     * @param idLivre
     * @param idMembre
     * @param datePret
     * @throws ServiceException
     */
    public void preter(int idLivre,
        int idMembre,
        String datePret) throws ServiceException {
        try {
            // Vérfie si le livre est disponible
            LivreDTO tupleLivre = getLivreDAO().read(idLivre);
            if(tupleLivre == null) {
                throw new ServiceException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.getIdMembre() != 0) {
                throw new ServiceException("Livre "
                    + idLivre
                    + " déjà prêté à "
                    + tupleLivre.getIdMembre());
            }

            // Vérifie si le membre existe et sa limite de prêt
            MembreDTO tupleMembre = getMembreDAO().read(idMembre);
            if(tupleMembre == null) {
                throw new ServiceException("Membre inexistant: "
                    + idMembre);
            }
            if(tupleMembre.getNbPret() >= tupleMembre.getLimitePret()) {
                throw new ServiceException("Limite de prêt du membre "
                    + idMembre
                    + " atteinte");
            }

            // Vérifie s'il existe une réservation pour le livre.
            ReservationDTO tupleReservation = getReservationDAO().read(idLivre);
            if(tupleReservation != null) {
                throw new ServiceException("Livre réservé par : "
                    + tupleReservation.getIdMembre()
                    + " idReservation : "
                    + tupleReservation.getIdReservation());
            }

            // Enregistrement du prêt.
            LivreDTO livre = new LivreDTO();
            livre.setIdLivre(idLivre);
            SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");
            Date date;
            try {
                date = new Date(format.parse(datePret).getTime());
            } catch(ParseException exception) {
                // TODO Auto-generated catch block
                exception.printStackTrace();
                throw new ServiceException("Erreur de parsing dans le format de date lors de la création d'un objet livre.");
            }
            livre.setDatePret(date);
            livre.setIdMembre(idMembre);
            getLivreDAO().emprunter(livre);
            /*
            if(nb1 == 0) {
                throw new ServiceException("Livre suprimé par une autre transaction");
            }
            int nb2 = getMembreDAO().preter(idMembre);
            if(nb2 == 0) {
                throw new ServiceException("Membre suprimé par une autre transaction");
            }
            getCx().commit();
             */

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
     * Renouvellement d'un prêt.
     * Le livre doit être prêté.
     * Le livre ne doit pas être réservé.
     *
     * @param idLivre
     * @param datePret
     * @throws ServiceException
     */
    public void renouveler(int idLivre,
        String datePret) throws ServiceException {
        try {
            // Vérifie si le livre est prêté
            LivreDTO tupleLivre;
            tupleLivre = getLivreDAO().read(idLivre);
            if(tupleLivre == null) {
                throw new ServiceException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.getIdMembre() == 0) {
                throw new ServiceException("Livre "
                    + idLivre
                    + " n'est pas prêté");
            }

            // Vérifie si date renouvellement >= datePret
            if(Date.valueOf(datePret).before(tupleLivre.getDatePret())) {
                System.err.println("Date de renouvellement inférieure à la date de prêt");
                return;
                // throw new ServiceException("Date de renouvellement inférieure à la date de prêt");
            }

            // Vérifie s'il existe une réservation pour le livre.
            ReservationDTO tupleReservation = getReservationDAO().read(idLivre);
            if(tupleReservation != null) {
                throw new ServiceException("Livre réservé par : "
                    + tupleReservation.getIdMembre()
                    + " idReservation : "
                    + tupleReservation.getIdReservation());
            }

            // Enregistrement du prêt.
            LivreDTO livre = new LivreDTO();
            livre.setIdLivre(idLivre);
            SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");
            Date date;
            try {
                date = new Date(format.parse(datePret).getTime());
            } catch(ParseException exception) {
                // TODO Auto-generated catch block
                exception.printStackTrace();
                throw new ServiceException("Erreur de parsing dans le format de date lors de la création d'un objet livre.");
            }
            livre.setDatePret(date);
            livre.setIdMembre(tupleLivre.getIdMembre());
            getLivreDAO().emprunter(livre);
            /*
            int nb1 = getLivreDAO().preter(idLivre,
                tupleLivre.getIdMembre(),
                datePret);
            if(nb1 == 0) {
                throw new ServiceException("Livre suprimé par une autre transaction");
            }
            getCx().commit();
             */

        } catch(DAOException daoException) {
            try {
                getCx().rollback();
            } catch(ConnexionException connexionException) {
                throw new ServiceException(connexionException);
            }
            // TODO handle after-rollback plz.
        }

    }

    /**
     * Retourner un livre prêté
     * Le livre doit être prêté.
     *
     * @param idLivre
     * @param dateRetour
     * @throws ServiceException
     */
    public void retourner(int idLivre,
        String dateRetour) throws ServiceException {
        try {
            // Vérifie si le livre est prêté
            LivreDTO tupleLivre = getLivreDAO().read(idLivre);
            if(tupleLivre == null) {
                throw new ServiceException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.getIdMembre() == 0) {
                throw new ServiceException("Livre "
                    + idLivre
                    + " n'est pas prêté ");
            }

            // Vérifie si date retour >= datePret
            if(Date.valueOf(dateRetour).before(tupleLivre.getDatePret())) {
                throw new ServiceException("Date de retour inférieure à la date de prêt");
            }

            // Retour du prêt
            LivreDTO livre = new LivreDTO();
            livre.setIdLivre(idLivre);
            SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");
            Date date;
            try {
                date = new Date(format.parse(dateRetour).getTime());
            } catch(ParseException exception) {
                // TODO Auto-generated catch block
                exception.printStackTrace();
                throw new ServiceException("Erreur de parsing dans le format de date lors de la création d'un objet livre.");
            }
            livre.setDatePret(date);
            livre.setIdMembre(tupleLivre.getIdMembre());
            getLivreDAO().emprunter(livre);
            /*
            int nb1 = getLivreDAO().retourner(idLivre);
            if(nb1 == 0) {
                throw new ServiceException("Livre suprimé par une autre transaction");
            }

            int nb2 = getMembreDAO().retourner(tupleLivre.getIdMembre());
            if(nb2 == 0) {
                throw new ServiceException("Livre suprimé par une autre transaction");
            }
            getCx().commit();
             */
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
     * @param livre La valeur à utiliser pour la variable d'instance <code>this.livreDAO</code>
     */
    private void setLivreDAO(LivreDAO livre) {
        this.livreDAO = livre;
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
     * @param membre La valeur à utiliser pour la variable d'instance <code>this.membreDAO</code>
     */
    private void setMembreDAO(MembreDAO membre) {
        this.membreDAO = membre;
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
     * @param reservation La valeur à utiliser pour la variable d'instance <code>this.reservationDAO</code>
     */
    private void setReservationDAO(ReservationDAO reservation) {
        this.reservationDAO = reservation;
    }

    /**
     * Getter de la variable d'instance <code>this.cx</code>.
     *
     * @return La variable d'instance <code>this.cx</code>
     */
    public Connexion getCx() {
        return this.cx;
    }
}
