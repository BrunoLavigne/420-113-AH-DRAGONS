// Fichier MembreService.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ServiceException;

/**
 * Gestion des transactions reliées à la création et la suppression des membres
 * dans une bibliothèque.
 *
 * Ce programme permet de gérer les transactions reliées à la création et la
 * suppression des membres.
 *
 * <pre>
 * Pré-condition: la base de données de la bibliothèque doit exister
 * </pre>
 *
 * <post> Post-condition: le programme effectue les *mises à jour* associées à
 * chaque transaction </post>
 */
public class MembreService extends Services {

    private static final long serialVersionUID = 1L;

    private MembreDAO membreDAO;

    private ReservationDAO reservationDAO;

    /**
     *
     * Crée le service de la table <code>membre</code>
     *
     * @param membreDAO
     *            Le DAO de la table <code>membre</code>
     * @param livreDAO
     *            Le DAO de la table <code>livre</code>
     * @param reservationDAO
     *            Le DAO de la table <code>reservation</code>
     */
    public MembreService(MembreDAO membreDAO,
        ReservationDAO reservationDAO) {
        setMembreDAO(membreDAO);
        setReservationDAO(reservationDAO);
    }

    // Opérations CRUD

    /**
     *
     * Ajoute un nouveau membre
     *
     * @param membreDTO
     *            Le membre à ajouter
     * @throws ServiceException
     *             S'il y a une erreur avec la base de données
     */
    public void add(MembreDTO membreDTO) throws ServiceException {

        try {
            getMembreDAO().add(membreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     *
     * Lit un membre.
     *
     * @param idMembre
     *            L'ID du membre à lire
     * @return MembreDTO Le membre à lire
     * @throws ServiceException
     *             S'il y a une erreur avec la base de données
     */
    public MembreDTO read(int idMembre) throws ServiceException {

        try {
            return getMembreDAO().read(idMembre);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     *
     * Met à jour un membre.
     *
     * @param membreDTO
     *            Le membre à mettre à jour
     * @throws ServiceException
     *             S'il y a une erreur avec la base de données
     */
    public void update(MembreDTO membreDTO) throws ServiceException {

        try {
            getMembreDAO().update(membreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     *
     * Supprime un membre
     *
     * @param membreDTO
     *            Le membre à supprimer
     * @throws ServiceException
     *             Si le membre a encore des prêts, s'il a des réservations ou
     *             s'il y a une erreur avec la base de données
     */
    public void delete(MembreDTO membreDTO) throws ServiceException {

        try {
            getMembreDAO().delete(membreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    // Opérations de recherche

    /**
     *
     * Trouve tous les membres
     *
     * @return List<MembreDTO> La liste des membres; une liste vide sinon
     * @throws ServiceException
     *             S'il y a une erreur avec la base de données
     */
    public List<MembreDTO> getAll() throws ServiceException {

        try {
            return getMembreDAO().getAll();
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    // Méthodes métiers

    /**
     *
     * Inscrit un membre.
     *
     * @param membreDTO
     *            Le membre à ajouter
     * @throws ServiceException
     *             Si le membre existe déjà ou s'il y a une erreur avec la base
     *             de données
     */
    public void inscrire(MembreDTO membreDTO) throws ServiceException {

        // Vérifier si le membre existe déjà
        if(read(membreDTO.getIdMembre()) != null) {

            System.err.println("Un membre avec l'id "
                + membreDTO.getIdMembre()
                + " existe déjà.");
            return;
            // throw new ServiceException("SRV-0003");

        }
        // S'il n'existe pas, on en crée un nouveau
        add(membreDTO);

    }

    /**
     *
     * Désincrit un membre.
     *
     * @param membreDTO
     *            Le membre à désinscrire
     * @throws ServiceException
     *             Si le membre n'existe pas, si le membre a encore des prêts,
     *             s'il a des réservations ou s'il y a une erreur avec la base
     *             de données
     */
    public void desinscrire(MembreDTO membreDTO) throws ServiceException {

        try {

            // Si le membre n'existe pas
            if(!existe(membreDTO.getIdMembre())) {
                System.err.println("Le membre avec l'ID "
                    + membreDTO.getIdMembre()
                    + " n'existe pas.");
                return;
                // throw new ServiceException("Le membre avec l'ID " +
                // membreDTO.getIdMembre() + " n'existe pas.");
            }

            // Si le membre a encore des prêts
            if(membreDTO.getNbPret() > 0) {
                System.err.println("Le membre avec l'ID"
                    + membreDTO.getIdMembre()
                    + " a encore "
                    + membreDTO.getNbPret()
                    + " prêts."
                    + "\nVeuillez retourner les livres avant de supprimer le compte.");
                return;
                /*
                 * throw new ServiceException( "Le membre avec l'ID" +
                 * membreDTO.getIdMembre() + " a encore " +
                 * membreDTO.getNbPret() + " prêts." +
                 * "\nVeuillez retourner les livres avant de supprimer le compte."
                 * );
                 */
            }

            // Si le membre a encore des réservations

            // Obtenir la liste de réservations du membre
            List<ReservationDTO> listeReservations = getReservationDAO().findByMembre(membreDTO);

            if(listeReservations.size() > 0) {
                System.err.println("Le membre avec l'ID "
                    + membreDTO.getIdMembre()
                    + " a encore "
                    + listeReservations.size()
                    + " réservations.");
                return;
                /*
                 * throw new ServiceException("Le membre avec l'ID " +
                 * membreDTO.getIdMembre() + " a encore " +
                 * listeReservations.size() + " réservations.");
                 */
            }

            // On peut supprimer le membre
            delete(membreDTO);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     *
     * Vérifie si un membre existe.
     *
     * @param idMembre
     *            L'ID du membre recherché
     * @return boolean <code>true</code> si le membre existe, <code>false</code>
     *         si ce n'est pas le cas.
     * @throws ServiceException
     *             En cas d'erreur d'appel au DAO, une exception est levée
     */
    public boolean existe(int idMembre) throws ServiceException {

        // Un membre est-il trouvé? Si null, non: donc false
        try {
            return getMembreDAO().read(idMembre) != null ? true : false;
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    // Getters & setters

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
     * @param membreDAO
     *            La valeur à utiliser pour la variable d'instance
     *            <code>this.membreDAO</code>
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
     * @param reservationDAO
     *            La valeur à utiliser pour la variable d'instance
     *            <code>this.reservationDAO</code>
     */
    private void setReservationDAO(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }
}
