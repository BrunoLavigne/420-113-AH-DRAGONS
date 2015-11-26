// Fichier IPretFacade.java
// Auteur : Dragons Vicieux
// Date de création : 2015-11-07

package ca.qc.collegeahuntsic.bibliothequeBackEnd.facade.interfaces;

import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.facade.FacadeException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.InvalidLoanLimitException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.MissingLoanException;
import org.hibernate.Session;

/**
 * Interface de façade pour manipuler les prêts dans la base de données.
 *
 * @author Dragons Vicieux
 */
public interface IPretFacade extends IFacade {
    /**
     * Lit un prêt à partir de la base de données. Si aucun prêt n'est trouvé, <code>null</code> est retourné.
     *
     * @param session La session Hibernate à utiliser
     * @param idPret L'ID du prêt à lire
     * @return Le prêt
     * @throws InvalidHibernateSessionException Si la session Hibernate est <code>null</code>
     * @throws InvalidPrimaryKeyException Si la clef primaire du prêt est <code>null</code>
     * @throws FacadeException S'il y a une erreur avec la base de données
     */
    PretDTO getPret(Session session,
        String idPret) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        FacadeException;

    /**
     * Commence un prêt.
     *
     * @param session La session Hibernate à utiliser
     * @param pretDTO Le prêt à commencer
     * @throws InvalidHibernateSessionException Si la session Hibernate est <code>null</code>
     * @throws InvalidDTOException Si le prêt est <code>null</code>
     * @throws ExistingLoanException Si le livre a été prêté
     * @throws InvalidLoanLimitException Si le membre a atteint sa limite de prêt
     * @throws ExistingReservationException Si le livre a été réservé
     * @throws FacadeException S'il y a une erreur avec la base de données
     */
    void commencerPret(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ExistingLoanException,
        InvalidLoanLimitException,
        ExistingReservationException,
        FacadeException;

    /**
     * Renouvelle le prêt d'un livre.
     *
     * @param session La session Hibernate à utiliser
     * @param pretDTO Le prêt à renouveler
     * @throws InvalidHibernateSessionException Si la session Hibernate est <code>null</code>
     * @throws InvalidDTOException Si le prêt est <code>null</code>
     * @throws MissingLoanException Si le livre n'a pas encore été prêté
     * @throws ExistingReservationException Si le livre a été réservé
     * @throws FacadeException S'il y a une erreur avec la base de données
     */
    void renouvelerPret(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        MissingLoanException,
        ExistingReservationException,
        FacadeException;

    /**
     * Termine un prêt.
     *
     * @param session La session Hibernate à utiliser
     * @param pretDTO Le prêt à terminer
     * @throws InvalidHibernateSessionException Si la session Hibernate est <code>null</code>
     * @throws InvalidDTOException Si le prêt est <code>null</code>
     * @throws MissingLoanException Si le livre n'a pas encore été prêté
     * @throws FacadeException S'il y a une erreur avec la base de données
     */
    void terminerPret(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        MissingLoanException,
        FacadeException;
}
