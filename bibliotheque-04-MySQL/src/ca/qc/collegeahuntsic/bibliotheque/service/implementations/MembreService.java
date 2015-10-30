// Fichier MembreService.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.service.implementations;

import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.implementations.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.implementations.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IMembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IReservationDAO;
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

    private IMembreDAO membreDAO;

    private IReservationDAO reservationDAO;

    /**
     *
     * Crée le service de la table <code>membre</code>
     *
     * @param membreDAO Le DAO de la table <code>membre</code>
     * @param livreDAO Le DAO de la table <code>livre</code>
     * @param reservationDAO Le DAO de la table <code>reservation</code>
     */
    public MembreService(MembreDAO membreDAO,
        ReservationDAO reservationDAO) {
        setMembreDAO(membreDAO);
        setReservationDAO(reservationDAO);
    }

    // Opérations CRUD

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    public MembreDTO get(Connexion connexion,
        String idMembre) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        ServiceException {

        try {
            return (MembreDTO) getMembreDAO().get(connexion,
                idMembre);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<MembreDTO> getAll(Connexion connexion,
        String sortByPropertyName) throws ServiceException,
        InvalidHibernateSessionException,
        InvalidSortByPropertyException {

        try {
            return (List<MembreDTO>) getMembreDAO().getAll(connexion,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    // Méthodes métiers

    /**
     * {@inheritDoc}
     */
    public void inscrire(Connexion connexion,
        MembreDTO membreDTO) throws ServiceException,
        InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException {

        // Vérifier si la connexion est null
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion est null");
        }

        // Vérifier si le membre est null
        if(membreDTO == null) {
            throw new InvalidDTOException("Le membre est null");
        }

        // On peut ajouter le membre
        add(connexion,
            membreDTO);

    }

    /**
     * {@inheritDoc}
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
            ReservationDTO reservation = getReservationDAO().get(connexion,
                membreDTO.getIdMembre());

            if(reservation != null) {
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
    private IMembreDAO getMembreDAO() {
        return this.membreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @param membreDAO
     *            La valeur à utiliser pour la variable d'instance
     *            <code>this.membreDAO</code>
     */
    private void setMembreDAO(IMembreDAO membreDAO) {
        this.membreDAO = membreDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @return La variable d'instance <code>this.reservationDAO</code>
     */
    private IReservationDAO getReservationDAO() {
        return this.reservationDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @param reservationDAO
     *            La valeur à utiliser pour la variable d'instance
     *            <code>this.reservationDAO</code>
     */
    private void setReservationDAO(IReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }
}
