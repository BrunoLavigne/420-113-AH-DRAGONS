
package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.Timestamp;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.PretDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
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

    private PretDAO pretDAO;

    private MembreDAO membreDAO;

    private LivreDAO livreDAO;

    private ReservationDAO reservationDAO;

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
    public PretService(PretDAO pretDAO,
        MembreDAO membreDAO,
        LivreDAO livreDAO,
        ReservationDAO reservationDAO) throws ServiceException {

        setPretDAO(pretDAO);
        setMembreDAO(membreDAO);
        setLivreDAO(livreDAO);
        setReservationDAO(reservationDAO);
    }

    /**
     *
     * Ajoute une nouveau prêt.
     *
     * @param PretDTO La réservation à ajouter.
     * @throws ServiceException S'il y a une erreur avec la base de données.
     */
    public void add(PretDTO pretDTO) throws ServiceException {

        try {
            getPretDAO().add(pretDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /**
     *
     * Lit une prêt
     *
     * @param idpret L'ID du pret à lire.
     * @return Le prêt qui correspond au ID reçu.
     * @throws ServiceException S'il y a une erreur avec la base de données.
     */
    public PretDTO read(int idPret) throws ServiceException {

        try {
            return getPretDAO().read(idPret);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /**
     *
     * Met à jour un prêt.
     *
     * @param pretDTO Mettre à jour le prêt.
     * @throws ServiceException S'il y a une erreur avec la base de données.
     */
    public void update(PretDTO pretDTO) throws ServiceException {

        try {
            getPretDAO().update(pretDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     *
     * Supprime un prêt.
     *
     * @param pretDTO Le prêt à supprimer.
     * @throws ServiceException S'il y a une erreur avec la base de données.
     */
    public void delete(PretDTO pretDTO) throws ServiceException {

        try {
            getPretDAO().delete(pretDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    // End Opérations CRUD

    // Region Opérations de recherche

    /**
     *
     * Trouve toutes les prêts.
     *
     * @return La liste des prêts ; une liste vide sinon.
     * @throws ServiceException S'il y a une erreur avec la base de données.
     */
    public List<PretDTO> getAll() throws ServiceException {

        try {
            return getPretDAO().getAll();
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /**
     *
     * Trouve les prêts non terminés d'un membre.
     *
     * @param idMembre - L'ID du membre à trouver
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws ServiceException S'il y a une erreur avec la base de données.
     */
    public List<PretDTO> findByMembre(int idMembre) throws ServiceException {

        try {
            return getPretDAO().findByMembre(getMembreDAO().read(idMembre));
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /**
     *
     * Trouve les livres en cours d'emprunt.
     *
     * @param idLivre - L'ID du livre à trouver
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws ServiceException S'il y a une erreur avec la base de données.
     */
    public List<PretDTO> findByLivre(int idLivre) throws ServiceException {

        try {
            return getPretDAO().findByLivre(getLivreDAO().read(idLivre));
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /**
     *
     * TODO Auto-generated method javadoc
     *
     * @param datePret La date de prêt à trouver
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws ServiceException
     */
    public List<PretDTO> findByDatePret(Timestamp datePret) throws ServiceException {

        try {
            return getPretDAO().findByDatePret(datePret);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /**
     *
     * TODO Auto-generated method javadoc
     *
     * @param datePret La date de prêt à trouver
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws ServiceException
     */
    public List<PretDTO> findByDateRetour(Timestamp datePret) throws ServiceException {

        try {
            return getPretDAO().findByDateRetour(datePret);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

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

            LivreDTO livreDTO;

            // Vérfie si le livre est disponible
            if(getLivreDAO().read(idLivre) == null) {
                System.err.println("Livre inexistant: "
                    + idLivre);
                return;
            }

            livreDTO = getLivreDAO().read(idLivre);

            // Vérifie si le livre est déjà prêté
            if(livreDTO.getIdMembre() != 0) {
                System.err.println("Livre "
                    + idLivre
                    + " déjà prêté à "
                    + livreDTO.getIdMembre());
                return;
            }

            // Vérifie si le membre existe et sa limite de prêt
            MembreDTO membreDTO;

            if(getMembreDAO().read(idMembre) == null) {
                System.err.println("Membre inexistant: "
                    + idMembre);
                return;
            }

            membreDTO = getMembreDAO().read(idMembre);

            if(membreDTO.getNbPret() >= membreDTO.getLimitePret()) {
                System.err.println("Limite de prêt du membre "
                    + idMembre
                    + " atteinte");
                return;
            }

            // Vérifie s'il existe une réservation pour le livre.

            if(getReservationDAO().read(idLivre) != null) {

                ReservationDTO reservationDTO = getReservationDAO().read(idLivre);

                System.err.println("Livre réservé par le membre : "
                    + reservationDTO.getIdMembre()
                    + " Le idReservation : "
                    + reservationDTO.getIdReservation());
                return;
            }

            // Enregistrement du prêt.
            livreDTO.setIdMembre(idMembre);
            getLivreDAO().emprunter(livreDTO);

        } catch(Exception exception) {
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
    public void renouveler(int idLivre) throws ServiceException {

        try {
            // Vérifie si le livre est prêté
            LivreDTO tupleLivre;
            tupleLivre = getLivreDAO().read(idLivre);
            if(tupleLivre == null) {
                System.err.println("Livre inexistant: "
                    + idLivre);
                return;
            }
            if(tupleLivre.getIdMembre() == 0) {
                System.err.println(("Livre "
                    + idLivre + " n'est pas prêté"));
                return;
            }

            java.util.Date date = new java.util.Date();
            Timestamp maintenant = new Timestamp(date.getTime());

            // Vérifie si date renouvellement >= datePret
            if(maintenant.before(tupleLivre.getDatePret())) {
                System.err.println("Date de renouvellement inférieure à la date de prêt");
                return;
                // throw new ServiceException("Date de renouvellement inférieure à la date de prêt");
            }

            // Vérifie s'il existe une réservation pour le livre.
            ReservationDTO tupleReservation = getReservationDAO().read(idLivre);
            if(tupleReservation != null) {
                System.err.println("Livre réservé par : "
                    + tupleReservation.getIdMembre()
                    + " idReservation : "
                    + tupleReservation.getIdReservation());
                return;
            }

            // Enregistrement du prêt.

            getLivreDAO().emprunter(tupleLivre);
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
            throw new ServiceException(daoException);
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
    public void retourner(int idLivre) throws ServiceException {

        try {

            // Vérifie si le livre existe
            LivreDTO livreDTO;
            if(getLivreDAO().read(idLivre) == null) {
                System.err.println("Livre inexistant: "
                    + idLivre);
                return;
                //throw new ServiceException("Livre inexistant: " + idLivre);
            }

            livreDTO = getLivreDAO().read(idLivre);

            // Vérifie si le livre est prêté
            if(livreDTO.getIdMembre() == 0) {
                System.err.println("Livre "
                    + idLivre
                    + " n'est pas prêté ");
                return;
                /* throw new ServiceException("Livre "
                    + idLivre
                    + " n'est pas prêté "); */
            }

            livreDTO.setDatePret(null);
            getLivreDAO().retourner(livreDTO);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
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
     * @param pret La valeur à utiliser pour la variable d'instance <code>this.pretDAO</code>
     */
    public void setPretDAO(PretDAO pretDAO) {
        this.pretDAO = pretDAO;
    }

}
