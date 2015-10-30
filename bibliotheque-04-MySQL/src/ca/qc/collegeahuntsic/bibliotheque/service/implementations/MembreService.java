// Fichier MembreService.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.service.implementations;

import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.implementations.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.implementations.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOClassException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;
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
     * Ajoute un nouveau membre dans la base de données.
     *
     * @param connexion La connexion à utiliser
     * @param membreDTO Le membre à ajouter
     * @throws InvalidHibernateSessionException Si la connexion est null
     * @throws InvalidDTOException Si le membre est null
     * @throws InvalidDTOClassException Si la classe du membre n'est pas celle que prend en charge le DAO
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void add(Connexion connexion,
        MembreDTO membreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException {

        try {
            getMembreDAO().add(connexion,
                membreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     *
     * Lit un membre.
     *
     * @param idMembre L'ID du membre à lire
     * @return MembreDTO Le membre à lire
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public MembreDTO get(Connexion connexion,
        String idMembre) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        ServiceException {

        try {
            return getMembreDAO().get(connexion,
                idMembre);
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
     * @throws InvalidDTOClassException
     * @throws InvalidDTOException
     * @throws InvalidHibernateSessionException
     */
    public void update(Connexion connexion,
        MembreDTO membreDTO) throws ServiceException,
        InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException {

        try {
            getMembreDAO().update(connexion,
                membreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     *
     * Supprime un membre de la base de données.
     *
     * @param connexion La connexion à utiliser
     * @param membreDTO Le membre à supprimer
     * @throws InvalidHibernateSessionException Si la connexion est null
     * @throws InvalidDTOException Si le membre est null
     * @throws InvalidDTOClassException Si la classe du membre n'est pas celle que prend en charge le DAO
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void delete(Connexion connexion,
        MembreDTO membreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException {

        try {
            getMembreDAO().delete(connexion,
                membreDTO);
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
     * @throws ServiceException S'il y a une erreur avec la base de données
     * @throws InvalidSortByPropertyException
     * @throws InvalidHibernateSessionException
     */
    public List<MembreDTO> getAll(Connexion connexion,
        String sortByPropertyName) throws ServiceException,
        InvalidHibernateSessionException,
        InvalidSortByPropertyException {

        try {
            return getMembreDAO().getAll(connexion,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    // Méthodes métiers

    /**
     *
     * Inscrit un membre.
     *
     * @param membreDTO Le membre à ajouter
     * @throws ServiceException Si le membre existe déjà ou s'il y a une erreur avec la base de données
     * @throws InvalidDTOClassException
     * @throws InvalidDTOException
     * @throws InvalidHibernateSessionException
     * @throws DAOException
     * @throws InvalidPrimaryKeyException
     */
    public void inscrire(Connexion connexion,
        MembreDTO membreDTO) throws ServiceException,
        DAOException,
        InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        InvalidPrimaryKeyException {

        // Vérifier si le membre existe déjà
        if(get(connexion,
            membreDTO.getIdMembre().toString()) != null) {

            System.err.println("Un membre avec l'id "
                + membreDTO.getIdMembre()
                + " existe déjà.");
            return;
            // throw new ServiceException("SRV-0003");

        }

        // S'il n'existe pas, on en crée un nouveau
        getMembreDAO().add(connexion,
            membreDTO);

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
    public void desinscrire(Connexion connexion,
        MembreDTO membreDTO) throws ServiceException {

        try {

            // Si le membre n'existe pas
            if(get(connexion,
                membreDTO.getIdMembre()) != null) {
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
            List<ReservationDTO> listeReservations = getReservationDAO().get(connexion, membreDTO.getIdMembre())

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
            getMembreDAO().delete(connexion,
                membreDTO);

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
