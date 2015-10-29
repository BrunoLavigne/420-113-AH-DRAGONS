
package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.Timestamp;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.implementations.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.implementations.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.implementations.PretDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.implementations.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ServiceException;

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
     *
     * Crée le service de la table <code>pret</code>.
     *
     * @param pretDAO - Le DAO de la table <code>pret</code>
     * @param membreDAO - Le DAO de la table <code>membre</code>
     * @param livreDAO - Le DAO de la table <code>livre</code>
     * @param reservationDAO - Le DAO de la table <code>reservation</code>
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
     * Ajoute un nouveau prêt.
     *
     * @param pretDTO - Le prêt à ajouter
     * @throws ServiceException - S'il y a une erreur avec la base de données
     */
    public void add(PretDTO pretDTO) throws ServiceException {

        try {
            getPretDAO().add(pretDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException.getMessage(),
                daoException);
        }

    }

    /**
     * Lit un prêt.
     *
     * @param idPret - L'ID du prêt à lire
     * @return
     * @throws ServiceException - L'ID du prêt à lire
     */
    public PretDTO read(int idPret) throws ServiceException {

        try {
            return getPretDAO().read(idPret);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException.getMessage(),
                daoException);
        }

    }

    /**
     * Met à jour un prêt.
     *
     * @param pretDTO - Le prêt à mettre à jour
     * @throws ServiceException - S'il y a une erreur avec la base de données
     */
    public void update(PretDTO pretDTO) throws ServiceException {

        try {
            getPretDAO().update(pretDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException.getMessage(),
                daoException);
        }
    }

    /**
     * Supprime un prêt.
     *
     * @param pretDTO - Le prêt à supprimer
     * @throws ServiceException - S'il y a une erreur avec la base de données
     */
    public void delete(PretDTO pretDTO) throws ServiceException {

        try {
            getPretDAO().delete(pretDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException.getMessage(),
                daoException);
        }

    }

    // End Opérations CRUD

    // Region Opérations de recherche

    /**
     * Trouve tous les prêts.
     *
     * @return La liste des prêts ; une liste vide sinon
     * @throws ServiceException - S'il y a une erreur avec la base de données
     */
    public List<PretDTO> getAll() throws ServiceException {

        try {
            return getPretDAO().getAll();
        } catch(DAOException daoException) {
            throw new ServiceException(daoException.getMessage(),
                daoException);
        }

    }

    /**
     * Trouve les prêts non terminés d'un membre.
     *
     * @param idMembre - L'ID du membre à trouver
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws ServiceException - S'il y a une erreur avec la base de données
     */
    public List<PretDTO> findByMembre(int idMembre) throws ServiceException {

        try {
            return getPretDAO().findByMembre(getMembreDAO().read(idMembre));
        } catch(DAOException daoException) {
            throw new ServiceException(daoException.getMessage(),
                daoException);
        }

    }

    /**
     * Trouve les livres en cours d'emprunt.
     *
     * @param idLivre - L'ID du livre à trouver
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws ServiceException - S'il y a une erreur avec la base de données
     */
    public List<PretDTO> findByLivre(int idLivre) throws ServiceException {

        try {
            return getPretDAO().findByLivre(getLivreDAO().read(idLivre));
        } catch(DAOException daoException) {
            throw new ServiceException(daoException.getMessage(),
                daoException);
        }

    }

    /**
     * Trouve les prêts à partir d'une date de prêt.
     *
     * @param datePret - La date de prêt à trouver
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws ServiceException - S'il y a une erreur avec la base de données
     */
    public List<PretDTO> findByDatePret(Timestamp datePret) throws ServiceException {

        try {
            return getPretDAO().findByDatePret(datePret);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException.getMessage(),
                daoException);
        }

    }

    /**
     * Trouve les prêts à partir d'une date de retour.
     *
     * @param datePret - La date de retour à trouver
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws ServiceException - S'il y a une erreur avec la base de données
     */
    public List<PretDTO> findByDateRetour(Timestamp datePret) throws ServiceException {

        try {
            return getPretDAO().findByDateRetour(datePret);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException.getMessage(),
                daoException);
        }

    }

    /**
     * Renouvelle le prêt d'un livre.
     *
     * @param pretDTO - Le prêt à renouveler
     * @throws ServiceException
     * - Si le prêt n'existe pas,
     * si le membre n'existe pas,
     * si le livre n'existe pas,
     * si le livre n'a pas encore été prêté,
     * si le livre a été prêté à quelqu'un d'autre,
     * si le livre a été réservé ou s'il y a une erreur avec la base de données.
     */
    public void renouveler(PretDTO pretDTO) throws ServiceException {

        try {
            // Si le prêt n'existe pas
            if(pretDTO == null) {
                throw new ServiceException("Le pret n'existe pas");
            }

            // Si le membre n'existe pas
            if(pretDTO.getMembreDTO() == null) {
                throw new ServiceException("Le membre n'existe pas");
            }

            // Si le livre n'existe pas
            if(pretDTO.getLivreDTO() == null) {
                throw new ServiceException("Le livre n'existe pas");
            }

            // Si le livre n'a pas encore été prêté
            List<PretDTO> listeDesPrets = getPretDAO().findByLivre(pretDTO.getLivreDTO());
            if(listeDesPrets.isEmpty()) {
                throw new ServiceException("Le livre n'a pas été prêté encore.");
            }

            // Si le livre a été prêté à quelqu'un d'autre
            for(PretDTO unPretDTO : listeDesPrets) {
                if(!(pretDTO.getMembreDTO().getIdMembre() == unPretDTO.getMembreDTO().getIdMembre())) {
                    throw new ServiceException("Le livre est déjà prêté");
                }
            }

            // Si le livre a été réservé
            if(getReservationDAO().read(pretDTO.getLivreDTO().getIdLivre()) != null) {
                throw new ServiceException("Le livre est déjà réservé");
            }

            pretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
            update(pretDTO);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException.getMessage(),
                daoException);
        }
    }

    /**
     * Commence un prêt.
     *
     * @param pretDTO - Le prêt à commencer
     * @throws ServiceException
     * - Si le membre n'existe pas,
     * si le livre n'existe pas,
     * si le livre a été prêté,
     * si le livre a été réservé,
     * si le membre a atteint sa limite de prêt ou s'il y a une erreur avec la base de données
     */
    public void commencer(PretDTO pretDTO) throws ServiceException {

        try {

            // Si le membre n'existe pas
            if(pretDTO.getMembreDTO() == null) {
                throw new ServiceException("Le membre n'existe pas");
            }

            pretDTO.setMembreDTO(getMembreDAO().read(pretDTO.getMembreDTO().getIdMembre()));

            // Si le livre n'existe pas
            if(pretDTO.getLivreDTO() == null) {
                throw new ServiceException("Le livre est inexistant");
            }

            pretDTO.setLivreDTO(getLivreDAO().read(pretDTO.getLivreDTO().getIdLivre()));

            // Si le livre a été prêté
            List<PretDTO> listeDesPrets = getPretDAO().findByLivre(pretDTO.getLivreDTO());
            if(!listeDesPrets.isEmpty()) {
                throw new ServiceException("Le livre a été prêté.");
            }

            // Si le livre a été réservé
            List<ReservationDTO> listeDesReservations = getReservationDAO().findByLivre(pretDTO.getLivreDTO());
            if(!listeDesReservations.isEmpty()) {
                throw new ServiceException("Le livre a été réservé.");
            }

            // Si le membre a atteint sa limite de prêt
            if(pretDTO.getMembreDTO().getNbPret() == pretDTO.getMembreDTO().getLimitePret()) {
                throw new ServiceException("Le membre a atteint sa limite de prêt");
            }

            //Création du pret

            pretDTO.getMembreDTO().setNbPret(pretDTO.getMembreDTO().getNbPret() + 1);
            getMembreDAO().update(pretDTO.getMembreDTO());
            pretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
            add(pretDTO);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException.getMessage(),
                daoException);
        }
    }

    /**
     * Retourne un livre.
     *
     * @param pretDTO - Le prêt à terminer
     * @throws ServiceException
     * Si le prêt n'existe pas,
     * si le membre n'existe pas,
     * si le livre n'existe pas,
     * si le livre n'a pas encore été prêté,
     * si le livre a été prêté à quelqu'un d'autre ou s'il y a une erreur avec la base de données
     */
    public void retourner(PretDTO pretDTO) throws ServiceException {

        try {

            // Si le prêt n'existe pas
            if(pretDTO == null) {
                throw new ServiceException("Le pret n'existe pas");
            }

            // Si le membre n'existe pas
            if(pretDTO.getMembreDTO() == null) {
                throw new ServiceException("Le membre n'existe pas");
            }

            // Si le livre n'existe pas
            if(pretDTO.getLivreDTO() == null) {
                throw new ServiceException("Le livre est inexistant");
            }

            // Si le livre n'a pas encore été prêté
            List<PretDTO> listeDesPrets = getPretDAO().findByLivre(pretDTO.getLivreDTO());
            if(listeDesPrets.isEmpty()) {
                throw new ServiceException("Le livre n'a pas encore été prêté.");
            }

            // Si le livre a été prêteé à quelqu'un d'autre
            for(PretDTO unPretDTO : listeDesPrets) {
                if(!(pretDTO.getMembreDTO().getIdMembre() == unPretDTO.getMembreDTO().getIdMembre())) {
                    throw new ServiceException("Le livre est déjà prêté");
                }
            }

            pretDTO.setMembreDTO(getMembreDAO().read(pretDTO.getMembreDTO().getIdMembre()));
            pretDTO.setLivreDTO(getLivreDAO().read(pretDTO.getLivreDTO().getIdLivre()));
            pretDTO.getMembreDTO().setNbPret(pretDTO.getMembreDTO().getNbPret() - 1);
            getMembreDAO().update(pretDTO.getMembreDTO());
            pretDTO.setDateRetour(new Timestamp(System.currentTimeMillis()));
            update(pretDTO);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException.getMessage(),
                daoException);
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
