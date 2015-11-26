// Fichier ReservationDAO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliothequeBackEnd.dao.implementations;

import java.util.List;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dao.interfaces.IReservationDAO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidCriterionValueException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dto.InvalidDTOClassException;
import org.hibernate.Session;

/**
 *
 * DAO pour effectuer des CRUDs avec la table <code>reservation</code>.
 *
 * @author Dragons Vicieux
 */

public class ReservationDAO extends DAO implements IReservationDAO {

    // TODO changer la visibilité a package quand nous aurons la version avec Spring
    /**
     * Crée le DAO de la table <code>reservation</code>.
     *
     * @param reservationDTOClass La classe de réservation DTO à utiliser
     * @throws InvalidDTOClassException Si la classe de DTO est <code>null</code>
     */
    public ReservationDAO(Class<ReservationDTO> reservationDTOClass) throws InvalidDTOClassException {
        super(reservationDTOClass);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ReservationDTO> findByLivre(Session session,
        String idLivre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        DAOException {

        if(session == null) {
            throw new InvalidHibernateSessionException("la session ne peut pas être null");
        }

        if(idLivre == null) {
            throw new InvalidCriterionException("Le critère ne peut pas être null");
        }

        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété sortByPropertyName ne peut pas être null");
        }

        return (List<ReservationDTO>) find(session,
            ReservationDTO.ID_LIVRE_COLUMN_NAME,
            idLivre,
            sortByPropertyName);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ReservationDTO> findByMembre(Session session,
        String idMembre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        DAOException {

        if(session == null) {
            throw new InvalidHibernateSessionException("la session ne peut pas être null");
        }

        if(idMembre == null) {
            throw new InvalidCriterionException("Le critère ne peut pas être null");
        }

        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété sortByPropertyName ne peut pas être null");
        }

        return (List<ReservationDTO>) find(session,
            ReservationDTO.ID_MEMBRE_COLUMN_NAME,
            idMembre,
            sortByPropertyName);
    }

}
