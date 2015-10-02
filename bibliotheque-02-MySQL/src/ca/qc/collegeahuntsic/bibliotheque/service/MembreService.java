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

	private MembreDAO membre;

	private ReservationDAO reservation;

	/**
	 * Création d'une instance
	 *
	 * @param membre
	 * @param reservation
	 */
	public MembreService(MembreDAO membre, ReservationDAO reservation) {
		setMembre(membre);
		setReservation(reservation);
	}

	/**
	 *
	 * Vérifie si un membre existe
	 *
	 * @param idMembre
	 * @return
	 * @throws ServiceException
	 */
	public boolean existe(int idMembre) throws ServiceException {

		// Un membre est-il trouvé? Si null, non: donc false
		try {
			return getMembre().read(idMembre) != null ? true : false;
		} catch (DAOException daoException) {
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
	public void inscrire(int idMembre, String nom, long telephone, int limitePret) throws ServiceException {

		try {

			if (getMembre().read(idMembre) != null) {

				MembreDTO nouveauMembre = new MembreDTO();
				nouveauMembre.setIdMembre(idMembre);
				nouveauMembre.setNom(nom);
				nouveauMembre.setTelephone(telephone);
				nouveauMembre.setLimitePret(limitePret);

				add(nouveauMembre);
			} else {
				throw new ServiceException("Un membre avec l'id " + idMembre + " existe déjà");
			}
		} catch (DAOException daoException) {
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
			MembreDTO tupleMembre = getMembre().read(idMembre);

			// Vérifie si le membre existe
			if (tupleMembre == null) {
				throw new ServiceException("Membre inexistant: " + idMembre);
			}

			// Vérifier si le membre a encore des prêts en cours
			if (tupleMembre.getNbPret() > 0) {
				throw new ServiceException("Le membre " + idMembre + " a encore des prêts.");
			}

			/*
			 * !!! À compléter !!!
			 *
			 * Vérifier si le membre a encore des réservations
			 *
			 * if (getReservation().getReservationMembre(idMembre) != null) {
			 * throw new ServiceException("Membre " + idMembre +
			 * " a des réservations"); }
			 */

			/* Suppression du membre */
			getMembre().delete(idMembre);

		} catch (DAOException daoException) {
			throw new ServiceException(daoException);
		}
	}

	/**
	 * Getter de la variable d'instance <code>this.membre</code>.
	 *
	 * @return La variable d'instance <code>this.membre</code>
	 */
	public MembreDAO getMembre() {
		return this.membre;
	}

	/**
	 * Setter de la variable d'instance <code>this.membre</code>.
	 *
	 * @param membre
	 *            La valeur à utiliser pour la variable d'instance
	 *            <code>this.membre</code>
	 */
	private void setMembre(MembreDAO membre) {
		this.membre = membre;
	}

	/**
	 * Getter de la variable d'instance <code>this.reservation</code>.
	 *
	 * @return La variable d'instance <code>this.reservation</code>
	 */
	public ReservationDAO getReservation() {
		return this.reservation;
	}

	/**
	 * Setter de la variable d'instance <code>this.reservation</code>.
	 *
	 * @param reservation
	 *            La valeur à utiliser pour la variable d'instance
	 *            <code>this.reservation</code>
	 */
	private void setReservation(ReservationDAO reservation) {
		this.reservation = reservation;
	}

	/**
	 *
	 * Ajoute un membre à la table membre
	 *
	 * @param membreDTO
	 * @throws ServiceException
	 */
	public void add(MembreDTO membreDTO) throws ServiceException {

		try {
			getMembre().add(membreDTO);
		} catch (DAOException daoException) {
			throw new ServiceException(daoException);
		}
	}

	/**
	 *
	 * Lit et renvoie un membre de la table membre
	 *
	 * @param idMembre
	 * @return MembreDTO Le membre dont L'ID est spécifié
	 * @throws ServiceException
	 */
	public MembreDTO read(int idMembre) throws ServiceException {

		try {
			return getMembre().read(idMembre);
		} catch (DAOException daoException) {
			throw new ServiceException(daoException);
		}
	}

	/**
	 *
	 * Met à jour un membre de la table membre
	 *
	 * @param membreDTO
	 * @throws ServiceException
	 */
	public void update(MembreDTO membreDTO) throws ServiceException {

		try {
			getMembre().update(membreDTO);
		} catch (DAOException daoException) {
			throw new ServiceException(daoException);
		}
	}

	/**
	 *
	 * Supprime un membre de la table membre
	 *
	 * @param idMembre
	 * @throws ServiceException
	 */
	public void delete(int idMembre) throws ServiceException {

		try {
			getMembre().delete(idMembre);
		} catch (DAOException daoException) {
			throw new ServiceException(daoException);
		}
	}

	/**
	 *
	 * Méthode retournant une liste de type <code>List</code> contenant des
	 * objets <code>MembreDTO</code>. La liste contient tous les membres
	 * enregistrés dans la base de données.
	 *
	 * @return liste une liste d'objets de type <code>MembreDTO</code>
	 *         représentant les membres enregistrés dans la base de données
	 *
	 * @throws ServiceException
	 */
	public List<MembreDTO> getAll() throws ServiceException {

		try {
			return getMembre().getAll();
		} catch (DAOException daoException) {
			throw new ServiceException(daoException);
		}
	}
}
