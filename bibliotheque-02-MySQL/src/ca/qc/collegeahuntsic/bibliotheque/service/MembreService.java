// Fichier MembreService.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 * Gestion des transactions reliées à la création et la suppression des membres
 * dans une bibliothèque.
 *
 * Ce programme permet de gérer les transactions reliées à la création et la
 * suppression des membres.
 *
 * <pre>
 * Pré-condition:
 *   la base de données de la bibliothèque doit exister
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
     * Création d'une instance
     *
     * @param membre
     * @param reservation
     */
    public MembreService(MembreDAO membre,
        ReservationDAO reservation) {
        setMembreDAO(membre);
        setReservationDAO(reservation);
    }

    // Opérations CRUD

    /**
     *
     * Appel la méthode ADD de membreDAO
     *
     * @param membreDTO Le membre à ajouter à la base de données
     * @throws ServiceException En cas d'erreur lors de l'appel au DAO, une exception est levée.
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
     * Appelle la méthode READ de le membreDAO
     *
     * @param idMembre Le ID du membre pour lequel nous désirons avoir des informations
     * @return MembreDTO Le membre dont L'ID est spécifié
     * @throws ServiceException En cas d'erreur lors de l'appel au DAO, une exception est levée.
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
     * Appel de la méthode UPDATE de membreDAO
     *
     * @param membreDTO Le membre pour lequel nous désirons effectuer des modifications
     * @throws ServiceException En cas d'erreur lors de l'appel au DAO, une exception est levée.
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
     * Appel la methode DELETE de membreDAO
     *
     * @param idMembre Le ID du membre que nous désirons enlever de la base de données
     * @throws ServiceException En cas d'erreur lors de l'appel au DAO, une exception est levée.
     */
    public void delete(int idMembre) throws ServiceException {

        try {
            getMembreDAO().delete(idMembre);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    // Opérations de recherche

    /**
     *
     * Méthode retournant une liste de type <code>List</code> contenant des
     * objets <code>MembreDTO</code>. La liste contient tous les membres
     * enregistrés dans la base de données.
     *
     * @return liste une liste d'objets de type <code>MembreDTO</code>
     *         représentant les membres enregistrés dans la base de données
     *
     * @throws ServiceException En cas d'erreur d'appel au DAO, une exception est levée.
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
     * Vérifie si un membre existe
     *
     * @param idMembre
     * @return
     * @throws ServiceException En cas d'erreur d'appel au DAO, une exception est levée
     */
    public boolean existe(int idMembre) throws ServiceException {

        // Un membre est-il trouvé? Si null, non: donc false
        try {
            return getMembreDAO().read(idMembre) != null ? true : false;
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Ajout d'un nouveau membre dans la base de données. Si elle existe déjà,
     * une exception est levée.
     *
     * @param idMembre
     * @param nom
     * @param telephone
     * @param limitePret
     * @throws ServiceException
     */
    public void inscrire(int idMembre,
        String nom,
        long telephone,
        int limitePret) throws ServiceException {

        try {

            if(getMembreDAO().read(idMembre) != null) {

                MembreDTO nouveauMembre = new MembreDTO();
                nouveauMembre.setIdMembre(idMembre);
                nouveauMembre.setNom(nom);
                nouveauMembre.setTelephone(telephone);
                nouveauMembre.setLimitePret(limitePret);

                add(nouveauMembre);
            } else {
                throw new ServiceException("Un membre avec l'id "
                    + idMembre
                    + " existe déjà");
            }
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /*
     *
     * !!! À compléter !!!
     *
     * public void emprunter(MembreDTO membreDTO) {
     *
     * // Voir si le membre existe réellement
     * if(getMembre().read(membreDTO.getIdMembre()) != null) {
     *
     * } }
     */

    /**
     * Suppression d'un membre dans la base de données.
     *
     * @param idMembre
     * @throws ServiceException
     */
    public void desinscrire(int idMembre) throws ServiceException {
        try {

            // Instance du membre
            MembreDTO tupleMembre = getMembreDAO().read(idMembre);

            // Vérifie si le membre existe
            if(tupleMembre == null) {
                throw new ServiceException("Membre inexistant: "
                    + idMembre);
            }

            // Vérifier si le membre a encore des prêts en cours
            if(tupleMembre.getNbPret() > 0) {
                throw new ServiceException("Le membre "
                    + idMembre
                    + " a encore des prêts.");
            }

            /*
             * !!! À compléter !!!
             *
             * Vérifier si le membre a encore des réservations
             *
             if (getReservationDAO() != null) {
                 throw new ServiceException("Membre " + idMembre +
                     " a des réservations");
             } */

            /* Suppression du membre */
            getMembreDAO().delete(idMembre);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
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
     * @param membre
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
    public ReservationDAO getReservationDAO() {
        return this.reservationDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @param reservation
     *            La valeur à utiliser pour la variable d'instance
     *            <code>this.reservationDAO</code>
     */
    private void setReservationDAO(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

}
