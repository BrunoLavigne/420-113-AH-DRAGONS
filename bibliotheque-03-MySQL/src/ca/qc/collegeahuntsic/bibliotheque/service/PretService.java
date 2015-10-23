
package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.Timestamp;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.PretDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
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
     * Renouvellement d'un prêt.
     * Le livre doit être prêté.
     * Le livre ne doit pas être réservé.
     *
     * @param idLivre
     * @param datePret
     * @throws ServiceException
     */
    public void renouveler(PretDTO pretDTO) throws ServiceException {

        try {
            // Vérifie si le livre est prêté
            PretDTO unPretDTO;
            unPretDTO = getPretDAO().read(idPret);
            if(unLivreDTO == null) {
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
    public void retourner(PretDTO pretDTO) throws ServiceException {

        try {
            if(getPretDAO().read(pretDTO.getIdPret()) == null) {
                System.err.println("Le pret : "
                    + pretDTO.getIdPret()
                    + " n'existe pas");
                return;
            }
            getPretDAO().read(pretDTO.getIdPret());

            // Verifie si le membre existe déjà
            MembreDTO unMembreDTO;
            if(getMembreDAO().read(pretDTO.getMembreDTO().getIdMembre()) == null) {
                System.err.println("Le membre : "
                    + pretDTO.getMembreDTO().getIdMembre()
                    + " n'existe pas");
                return;
            }
            unMembreDTO = getMembreDAO().read(pretDTO.getMembreDTO().getIdMembre());

            //verifie si le livre existe
            if(getLivreDAO().read(pretDTO.getLivreDTO().getIdLivre()) == null) {
                System.err.println("Le livre est inexistant: "
                    + pretDTO.getLivreDTO().getIdLivre());
                return;
            }

            //  Si le membre n'existe pas

            if(getMembreDAO().read(pretDTO.getMembreDTO().getIdMembre()) == null) {
                System.err.println("Membre inexistant: "
                    + pretDTO.getMembreDTO().getIdMembre());
                return;
            }
            unMembreDTO = getMembreDAO().read(pretDTO.getMembreDTO().getIdMembre());

            unMembreDTO.setNbPret(unMembreDTO.getNbPret() - 1);
            getMembreDAO().update(unMembreDTO);
            pretDTO.setDateRetour(new Timestamp(System.currentTimeMillis()));
            update(pretDTO);

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
