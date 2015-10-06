// Fichier MembreService.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
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

    /**
     *
     * Crée le service de la table <code>membre</code>
     *
     * @param membreDAO Le DAO de la table <code>membre</code>
     * @param livreDAO Le DAO de la table <code>livre</code>
     * @param reservationDAO Le DAO de la table <code>reservation</code>
     */
    public MembreService(MembreDAO membreDAO,
        LivreDAO livreDAO,
        ReservationDAO reservationDAO) {
        setMembreDAO(membreDAO);
        setLivreDAO(livreDAO);
        setReservationDAO(reservationDAO);
    }

    // Opérations CRUD

    /**
     *
     * Ajoute un nouveau membre
     *
     * @param membreDTO Le membre à ajouter
     * @throws ServiceException S'il y a une erreur avec la base de données
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
     * @param idMembre L'ID du membre à lire
     * @return MembreDTO Le membre à lire
     * @throws ServiceException S'il y a une erreur avec la base de données
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
     * @param membreDTO Le membre à mettre à jour
     * @throws ServiceException S'il y a une erreur avec la base de données
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
     * @param membreDTO Le membre à supprimer
     * @throws ServiceException Si le membre a encore des prêts, s'il a des réservations ou s'il y a une erreur avec la base de données
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
    public void emprunter(MembreDTO membreDTO,
        LivreDTO livreDTO) throws ServiceException {

        try {
            // Voir si le membre existe réellement
            MembreDTO unMembreDTO = read(membreDTO.getIDMembre())
            if(unMembreDTO == null) {
                throw new ServiceException(le membre existe pas)
            }

            // Vérifier sur livre

            // Voir si livre a été prêté
            MembreDTO emprunter = read(unLivreDTO.getIdMembre());
            if(emprunteur != null) {
                throw new ServiceException(Le livre a été prêté);
            }

            // Voir si le livre a des réservations


        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    } */

    /*
    public void renouveler(MembreDTO unMembreDTO, LivreDTO unLivreDTO) {

        // Il faut que le livre existe
        //LivreDTO unLivreDTO getLivreDAO().read(livreDTO.get)

        // Il faut que le livre ait un emprunteur

        // Il faut que celui qui renouvele ait le même ID que le membre
        // tjrs throw new ServiceException

        // VÉrifier si a déjà des réservations...drop l'emprunt s'il a des réservations

        // faudrait faire getLivreDao().emprunter(unLivreDTO);
    }

    public void retourner(MembreDTO unMembreDTO, LivreDTO unLivreDTO) {

        // à la fin
        // getLivreDAO().retourner(unLivreDTO);
        // getMembreDAO().retourner(unMembreDTO);
    }*/

    /**
     * Suppression d'un membre dans la base de données.
     *
     * @param idMembre
     * @throws ServiceException
     */
    public void desinscrire(MembreDTO membreDTO) throws ServiceException {

        // Instance du membre
        MembreDTO tupleMembre = read(membreDTO.getIdMembre());

        // Vérifie si le membre existe
        if(tupleMembre == null) {
            throw new ServiceException("Membre inexistant: "
                + membreDTO.getIdMembre());
        }

        // Vérifier si le membre a encore des prêts en cours
        if(tupleMembre.getNbPret() > 0) {
            throw new ServiceException("Le membre "
                + tupleMembre.getIdMembre()
                + " a encore des prêts.");
        }

        //if(!getReservationDAO().getByMembre())

        /*
         * !!! À compléter !!!
         *
         * Vérifier si le membre a encore des réservations
         *
         if (!getReservationDAO() != null) {
             throw new ServiceException("Membre " + idMembre +
                 " a des réservations");
         } */

        /* Suppression du membre */
        delete(membreDTO.getIdMembre());

    }

    /**
     *
     * Retourne le membre
     *
     * @return MembreDAO Le membre DAO
     */
    private MembreDAO getMembreDAO() {
        return this.membreDAO;
    }

    /**
     *
     * Set le membre
     *
     * @param membreDAO Le membre DAO
     */
    private void setMembreDAO(MembreDAO membreDAO) {
        this.membreDAO = membreDAO;
    }

    private void setLivreDAO(LivreDAO livreDAO) {
    }

    private void setReservationDAO(ReservationDAO reservationDAO) {
    }
}
