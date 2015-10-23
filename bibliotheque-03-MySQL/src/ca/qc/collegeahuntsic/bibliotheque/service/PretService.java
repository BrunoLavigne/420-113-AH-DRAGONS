
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
     * Renouvelle le prêt d'un livre.
     *
     * @param pretDTO
     * @throws ServiceException
     * Si le prêt n'existe pas,
     * si le membre n'existe pas,
     * si le livre n'existe pas,
     * si le livre n'a pas encore été prêté,
     * si le livre a été prêté à quelqu'un d'autre,
     * si le livre a été réservé ou s'il y a une erreur avec la base de données.
     */
    public void renouveler(PretDTO pretDTO) throws ServiceException {

        try {
            // Si le prêt n'existe pas
            if(getPretDAO().read(pretDTO.getIdPret()) == null) {
                System.err.println("Le pret : "
                    + pretDTO.getIdPret()
                    + " n'existe pas");
                return;
            }
            getPretDAO().read(pretDTO.getIdPret());

            // Si le membre n'existe pas
            MembreDTO unMembreDTO;
            if(getMembreDAO().read(pretDTO.getMembreDTO().getIdMembre()) == null) {
                System.err.println("Le membre : "
                    + pretDTO.getMembreDTO().getIdMembre()
                    + " n'existe pas");
                return;
            }
            unMembreDTO = getMembreDAO().read(pretDTO.getMembreDTO().getIdMembre());

            LivreDTO unLivreDTO;
            // Si le livre n'existe pas
            if(getLivreDAO().read(pretDTO.getLivreDTO().getIdLivre()) == null) {
                System.err.println("Le livre est inexistant: "
                    + pretDTO.getLivreDTO().getIdLivre());
                return;
            }
            unLivreDTO = getLivreDAO().read(pretDTO.getLivreDTO().getIdLivre());

            // Si le livre n'a pas encore été prêté
            List<PretDTO> listeDesPrets = getPretDAO().findByLivre(unLivreDTO);
            if(listeDesPrets.isEmpty()) {
                System.err.println("Le livre : "
                    + unLivreDTO.getIdLivre()
                    + " n'a pas été prêté encore.");
                return;
            }

            // Si le livre a été prêté à quelqu'un d'autre
            for(PretDTO unPretDTO : listeDesPrets) {
                if(!unMembreDTO.equals(unPretDTO.getMembreDTO())) {
                    System.err.println("Le livre : "
                        + unLivreDTO.getIdLivre()
                        + " est déjà prêté au membre : "
                        + unMembreDTO.getIdMembre());
                    return;
                }
            }

            // Si le livre a été réservé
            if(getReservationDAO().read(unLivreDTO.getIdLivre()) != null) {
                System.err.println("Le livre : "
                    + unLivreDTO.getIdLivre()
                    + " est déjà prêté");
                return;
            }

            pretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
            update(pretDTO);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        } catch(NullPointerException nullPointerException) {
            throw new ServiceException(nullPointerException);
        }

    }

    /**
     * Retourne un livre.
     *
     * @param pretDTO
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
            if(getPretDAO().read(pretDTO.getIdPret()) == null) {
                System.err.println("Le pret : "
                    + pretDTO.getIdPret()
                    + " n'existe pas");
                return;
            }
            getPretDAO().read(pretDTO.getIdPret());

            // Si le membre n'existe pas
            MembreDTO unMembreDTO;
            if(getMembreDAO().read(pretDTO.getMembreDTO().getIdMembre()) == null) {
                System.err.println("Le membre : "
                    + pretDTO.getMembreDTO().getIdMembre()
                    + " n'existe pas");
                return;
            }
            unMembreDTO = getMembreDAO().read(pretDTO.getMembreDTO().getIdMembre());

            LivreDTO unLivreDTO;
            // Si le livre n'existe pas
            if(getLivreDAO().read(pretDTO.getLivreDTO().getIdLivre()) == null) {
                System.err.println("Le livre est inexistant: "
                    + pretDTO.getLivreDTO().getIdLivre());
                return;
            }
            unLivreDTO = getLivreDAO().read(pretDTO.getLivreDTO().getIdLivre());

            // Si le livre n'a pas encore été prêté
            List<PretDTO> listeDesPrets = getPretDAO().findByLivre(unLivreDTO);
            if(listeDesPrets.isEmpty()) {
                System.err.println("Le livre : "
                    + unLivreDTO.getIdLivre()
                    + " n'a pas été prêté encore.");
                return;
            }

            // Si le livre a été prêteé à quelqu'un d'autre
            for(PretDTO unPretDTO : listeDesPrets) {
                if(!unMembreDTO.equals(unPretDTO.getMembreDTO())) {
                    System.err.println("Le livre : "
                        + unLivreDTO.getIdLivre()
                        + " est déjà prêté au membre : "
                        + unMembreDTO.getIdMembre());
                    return;
                }
            }

            PretDTO unPretDTO = new PretDTO();
            unPretDTO.setLivreDTO(unLivreDTO);
            getPretDAO().update(unPretDTO);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        } catch(NullPointerException nullPointerException) {
            throw new ServiceException(nullPointerException);
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
