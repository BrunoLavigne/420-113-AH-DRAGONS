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
	public MembreService(MembreDAO membreDAO, LivreDAO livreDAO, ReservationDAO reservationDAO) {
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
		} catch (DAOException daoException) {
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
		} catch (DAOException daoException) {
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
		} catch (DAOException daoException) {
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
		} catch (DAOException daoException) {
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
		} catch (DAOException daoException) {
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
			if (getMembreDAO().read(membreDTO.getIdMembre()) != null) {

				throw new ServiceException("Un membre avec l'id " + membreDTO.getIdMembre() + " existe déjà.");

			}

			// S'il n'existe pas, on en crée un nouveau
			MembreDTO nouveauMembre = new MembreDTO();

			nouveauMembre.setIdMembre(membreDTO.getIdMembre());
			nouveauMembre.setNom(membreDTO.getNom());
			nouveauMembre.setTelephone(membreDTO.getTelephone());
			nouveauMembre.setLimitePret(membreDTO.getLimitePret());

			add(nouveauMembre);

		} catch (DAOException daoException) {
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
	public void emprunter(MembreDTO membreDTO, LivreDTO livreDTO) throws ServiceException {

		try {

			// Si le membre n'existe pas, exception
			if (getMembreDAO().read(membreDTO.getIdMembre()) == null) {
				throw new ServiceException("Le membre avec l'ID " + membreDTO.getIdMembre() + " n'existe pas.");
			}

			// Si le membre a atteint sa limite de prêts
			if (membreDTO.getNbPret() >= membreDTO.getLimitePret()) {
				throw new ServiceException(
						"Le membre avec l'ID " + membreDTO.getIdMembre() + " a atteint sa limite de prêts.");
			}

			// Si le livre n'existe pas, exception
			if (!getLivreDAO().checkLivreExist(livreDTO.getIdLivre())) {
				throw new ServiceException("Le livre avec l'ID " + livreDTO.getIdLivre() + " n'existe pas.");
			}

			// Voir si livre a été prêté
			MembreDTO emprunteur = read(livreDTO.getIdMembre());
			if (emprunteur != null) {
				throw new ServiceException("Impossible d'emprunter - le livre a été prêté.");
			}

			// Voir si le livre a des réservations
			List<ReservationDTO> listeReservations = getReservationDAO().findByLivre(livreDTO);
			if (listeReservations.size() > 0) {
				throw new ServiceException("Impossible d'emprunter - ce livre a des réservations.");
			}

			// Après vérifications, le livre peut être emprunté au membre
			// livreDTO.setDatePret(datePret); !!! à faire mettre timestamp
			livreDTO.setIdMembre(membreDTO.getIdMembre());
			getLivreDAO().emprunter(livreDTO);

		} catch (DAOException daoException) {
			throw new ServiceException(daoException);
		}
	}

	/**
	 *
	 * Renouvelle le prêt d'un livre
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
	public void renouveler(MembreDTO membreDTO, LivreDTO livreDTO) throws ServiceException, DAOException {

		// Si membre n'existe pas
		if (!existe(membreDTO.getIdMembre())) {
			throw new ServiceException("Aucun membre avec l'ID " + membreDTO.getIdMembre() + " trouvé.");
		}

		// Si livre n'existe pas
		if (!getLivreDAO().checkLivreExist(livreDTO.getIdLivre())) {
			throw new ServiceException("Aucun livre avec l'ID " + livreDTO.getIdLivre() + " trouvé");
		}

		// Si livre n'a pas été prêté
		if (getReservationDAO().findByLivre(livreDTO).isEmpty()) {
			throw new ServiceException(
					"Aucune réservation pour le livre avec l'ID " + livreDTO.getIdLivre() + " trouvée.");
		}

		// S'il y a eu réservation, vérifier que c'est avec le membre qui veut
		// renouveler
		if (getReservationDAO().findByLivre(livreDTO).get(0).getIdMembre() != membreDTO.getIdMembre()) {
			throw new ServiceException("Le membre avec l'ID " + membreDTO.getIdMembre()
					+ " n'a pas de réservation pour le livre avec l'ID " + livreDTO.getIdLivre());
		}

		// Il faut que celui qui renouvele ait le même ID que le membre
		// tjrs throw new ServiceException

		// VÉrifier si a déjà des réservations...drop l'emprunt s'il a des
		// réservations

		// faudrait faire getLivreDao().emprunter(unLivreDTO);
	}

	/*
	 * public void retourner(MembreDTO unMembreDTO, LivreDTO unLivreDTO) {
	 *
	 * // à la fin // getLivreDAO().retourner(unLivreDTO); //
	 * getMembreDAO().retourner(unMembreDTO); }
	 */

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
		if (tupleMembre == null) {
			throw new ServiceException("Membre inexistant: " + membreDTO.getIdMembre());
		}

		// Vérifier si le membre a encore des prêts en cours
		if (tupleMembre.getNbPret() > 0) {
			throw new ServiceException("Le membre " + tupleMembre.getIdMembre() + " a encore des prêts.");
		}

		// if(!getReservationDAO().getByMembre())

		/*
		 * !!! À compléter !!!
		 *
		 * Vérifier si le membre a encore des réservations
		 *
		 * if (!getReservationDAO() != null) { throw new ServiceException(
		 * "Membre " + idMembre + " a des réservations"); }
		 */

		/* Suppression du membre */
		delete(membreDTO);
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

	/**
	 *
	 * Vérifie si un membre existe
	 *
	 * @param idMembre
	 * @return
	 * @throws ServiceException
	 *             En cas d'erreur d'appel au DAO, une exception est levée
	 */
	public boolean existe(int idMembre) throws ServiceException {

		// Un membre est-il trouvé? Si null, non: donc false
		try {
			return getMembreDAO().read(idMembre) != null ? true : false;
		} catch (DAOException daoException) {
			throw new ServiceException(daoException);
		}
	}
}
