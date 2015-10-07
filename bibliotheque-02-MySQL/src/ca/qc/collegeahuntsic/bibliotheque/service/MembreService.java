// Fichier MembreService.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
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

    private LivreDAO livreDAO;

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
        LivreDAO livreDAO,
        ReservationDAO reservationDAO) {
        setMembreDAO(membreDAO);
        setLivreDAO(livreDAO);
        setReservationDAO(reservationDAO);
    }

    // Opérations CRUD

    private LivreDAO getLivreDAO() {
        return this.livreDAO;
    }

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

        try {

            // Vérifier si le membre existe déjà
            if(getMembreDAO().read(membreDTO.getIdMembre()) != null) {
                System.err.println("Un membre avec l'id "
                    + membreDTO.getIdMembre()
                    + " existe déjà.");
                return;
                //throw new ServiceException("Un membre avec l'id " + membreDTO.getIdMembre() + " existe déjà.");

            }

            // S'il n'existe pas, on en crée un nouveau
            MembreDTO nouveauMembre = new MembreDTO();

            nouveauMembre.setIdMembre(membreDTO.getIdMembre());
            nouveauMembre.setNom(membreDTO.getNom());
            nouveauMembre.setTelephone(membreDTO.getTelephone());
            nouveauMembre.setLimitePret(membreDTO.getLimitePret());

            add(nouveauMembre);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     *
     * Emprunte un livre.
     *
     * @param membreDTO
     *            Le membre qui emprunte
     * @param livreDTO
     *            Le livre à emprunter
     * @throws ServiceException
     *             Si le membre n'existe pas, si le livre n'existe pas, si le
     *             livre a été prêté, si le livre a été réservé, si le membre a
     *             atteint sa limite de prêt ou s'il y a une erreur avec la base
     *             de données
     */
    public void emprunter(MembreDTO membreDTO,
        LivreDTO livreDTO) throws ServiceException {

        try {

            // Si le membre n'existe pas, exception
            if(getMembreDAO().read(membreDTO.getIdMembre()) == null) {
                System.err.println("Le membre avec l'ID "
                    + membreDTO.getIdMembre()
                    + " n'existe pas.");
                return;
                // throw new ServiceException("Le membre avec l'ID " + membreDTO.getIdMembre() + " n'existe pas.");
            }

            // Si le membre a atteint sa limite de prêts
            if(membreDTO.getNbPret() >= membreDTO.getLimitePret()) {
                System.err.println("Le membre avec l'ID "
                    + membreDTO.getIdMembre()
                    + " a atteint sa limite de prêts.");
                return;
                /* throw new ServiceException(
                		"Le membre avec l'ID " + membreDTO.getIdMembre() + " a atteint sa limite de prêts."); */
            }

            // Si le livre n'existe pas, exception
            if(!getLivreDAO().checkLivreExist(livreDTO.getIdLivre())) {
                System.err.println("Le livre avec l'ID "
                    + livreDTO.getIdLivre()
                    + " n'existe pas.");
                return;
                // throw new ServiceException("Le livre avec l'ID " + livreDTO.getIdLivre() + " n'existe pas.");
            }

            // Voir si livre a été prêté
            MembreDTO emprunteur = read(livreDTO.getIdMembre());
            if(emprunteur != null) {
                System.err.println("Impossible d'emprunter - le livre a été prêté.");
                return;
                // throw new ServiceException("Impossible d'emprunter - le livre a été prêté.");
            }

            // Voir si le livre a des réservations
            List<ReservationDTO> listeReservations = getReservationDAO().findByLivre(livreDTO);
            if(listeReservations.size() > 0) {
                System.err.println("Impossible d'emprunter - ce livre a des réservations.");
                return;
                // throw new ServiceException("Impossible d'emprunter - ce livre a des réservations.");
            }

            // Après vérifications, le livre peut être emprunté au membre
            // livreDTO.setDatePret(datePret); !!! à faire mettre timestamp
            livreDTO.setIdMembre(membreDTO.getIdMembre());
            getLivreDAO().emprunter(livreDTO);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     *
     * Renouvelle le prêt d'un livre.
     *
     * @param membreDTO
     *            Le membre qui renouvelle
     * @param livreDTO
     *            Le livre à renouveler
     * @throws ServiceException
     *             Si le membre n'existe pas, si le livre n'existe pas, si le
     *             livre n'a pas encore été prêté, si le livre a été prêté à
     *             quelqu'un d'autre ou s'il y a une erreur avec la base de
     *             données
     * @throws DAOException
     *             On ne devrait pas avoir cette exception normalement.
     */
    public void renouveler(MembreDTO membreDTO,
        LivreDTO livreDTO) throws ServiceException {

        try {

            MembreDTO unMembreDTO = getMembreDAO().read(membreDTO.getIdMembre());

            // Si le membre n'existe pas
            if(unMembreDTO == null) {
                System.err.println("Le membre : "
                    + membreDTO.getIdMembre()
                    + " n'existe pas");
                return;
                // throw new ServiceException("Le membre : " + membreDTO.getIdMembre() + " n'existe pas");
            }

            LivreDTO unLivreDTO = getLivreDAO().read(livreDTO.getIdLivre());

            // Si le livre n'existe pas
            if(unLivreDTO == null) {
                System.err.println("Le livre : "
                    + livreDTO.getIdLivre()
                    + " n'existe pas");
                return;
                // throw new ServiceException("Le livre : " + livreDTO.getIdLivre() + " n'existe pas");
            }

            // Si le livre est prêté ou non
            // Association du membre et du livre, passés en paramètre
            MembreDTO emprunteur = getMembreDAO().read(unLivreDTO.getIdMembre());

            if(emprunteur == null) {
                System.err.println("Le livre : "
                    + unLivreDTO.getIdLivre()
                    + " n'a pas été prêté encore. Faire un emprunt au lieu d'un réservation");
                return;
                /* throw new ServiceException("Le livre : " + unLivreDTO.getIdLivre()
                		+ " n'a pas été prêté encore. Faire un emprunt au lieu d'un réservation"); */
            }

            // Si le livre est prêté à quelqu'un d'autre
            if(unMembreDTO.getIdMembre() != emprunteur.getIdMembre()) {
                System.err.println("Le livre : "
                    + unLivreDTO.getIdLivre()
                    + " est déjà prêté au membre : "
                    + unMembreDTO.getIdMembre());
                return;
                /* throw new ServiceException("Le livre : " + unLivreDTO.getIdLivre() + " est déjà prêté au membre : "
                		+ unMembreDTO.getIdMembre()); */
            }

            // unLivreDTO.setDatePret("aujourd'hui");

            emprunter(unMembreDTO,
                unLivreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /**
     *
     * Retourne un livre.
     *
     * @param membreDTO
     *            Le membre qui retourne
     * @param livreDTO
     *            Le livre à retourner
     *
     * @throws ServiceException
     *             Si le membre n'existe pas, si le livre n'existe pas, si le
     *             livre n'a pas encore été prêté, si le livre a été prêté à
     *             quelqu'un d'autre ou s'il y a une erreur avec la base de
     *             données
     */
    public void retourner(MembreDTO membreDTO,
        LivreDTO livreDTO) throws ServiceException {

        try {

            MembreDTO unMembreDTO = getMembreDAO().read(membreDTO.getIdMembre());

            // Si le membre n'existe pas
            if(unMembreDTO == null) {
                System.err.println("Le membre : "
                    + membreDTO.getIdMembre()
                    + " n'existe pas");
                return;
                // throw new ServiceException("Le membre : " + membreDTO.getIdMembre() + " n'existe pas");
            }

            LivreDTO unLivreDTO = getLivreDAO().read(livreDTO.getIdLivre());

            // Si le livre n'existe pas
            if(unLivreDTO == null) {
                System.err.println("Le livre : "
                    + livreDTO.getIdLivre()
                    + " n'existe pas");
                return;
                // throw new ServiceException("Le livre : " + livreDTO.getIdLivre() + " n'existe pas");
            }

            // Si le livre est prêté ou non
            // Association du membre et du livre, passés en paramètre
            MembreDTO emprunteur = getMembreDAO().read(unLivreDTO.getIdMembre());

            if(emprunteur == null) {
                System.err.println("Le livre : "
                    + unLivreDTO.getIdLivre()
                    + " n'a pas été prêté encore. Faire un emprunt au lieu d'un réservation");
                return;
                /* throw new ServiceException("Le livre : " + unLivreDTO.getIdLivre()
                		+ " n'a pas été prêté encore. Faire un emprunt au lieu d'un réservation"); */
            }

            // Si le livre est prêté à quelqu'un d'autre
            if(unMembreDTO.getIdMembre() != emprunteur.getIdMembre()) {
                System.err.println("Le livre : "
                    + unLivreDTO.getIdLivre()
                    + " est déjà prêté au membre : "
                    + unMembreDTO.getIdMembre());
                return;
                /* throw new ServiceException("Le livre : " + unLivreDTO.getIdLivre() + " est déjà prêté au membre : "
                		+ unMembreDTO.getIdMembre()); */
            }
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
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
                // throw new ServiceException("Le membre avec l'ID " + membreDTO.getIdMembre() + " n'existe pas.");
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
                /* throw new ServiceException(
                		"Le membre avec l'ID" + membreDTO.getIdMembre() + " a encore " + membreDTO.getNbPret()
                				+ " prêts." + "\nVeuillez retourner les livres avant de supprimer le compte."); */
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
                /* throw new ServiceException("Le membre avec l'ID " + membreDTO.getIdMembre() + " a encore "
                		+ listeReservations.size() + " réservations."); */
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
     * @param membreDAO
     *            Le membre DAO
     */
    private void setMembreDAO(MembreDAO membreDAO) {
        this.membreDAO = membreDAO;
    }

    private void setLivreDAO(LivreDAO livreDAO) {
        this.livreDAO = livreDAO;
    }

    private void setReservationDAO(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    private ReservationDAO getReservationDAO() {
        return this.reservationDAO;
    }
}
