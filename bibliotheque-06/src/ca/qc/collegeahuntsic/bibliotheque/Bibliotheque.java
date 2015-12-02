// Fichier Bibliotheque.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.StringTokenizer;
import ca.qc.collegeahuntsic.bibliotheque.exception.BibliothequeException;
import ca.qc.collegeahuntsic.bibliotheque.util.BibliothequeCreateur;
import ca.qc.collegeahuntsic.bibliotheque.util.FormatteurDate;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dto.MissingDTOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.facade.FacadeException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.InvalidLoanLimitException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.MissingLoanException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Interface du système de gestion d'une bibliothèque.
 *
 * Ce programme permet d'appeler les transactions de base d'une
 * bibliothèque. Il gère des livres, des membres et des
 * réservations. Les données sont conservées dans une base de
 * données relationnelles accédée avec JDBC. Pour une liste des
 * transactions traitées, voir la méthode afficherAide().
 *
 * Paramètres
 * 0 - Fichier de transaction
 *
 * Pré-condition : La base de données de la bibliothèque doit exister
 *
 * Post-condition : Le programme effectue les mises à jour associées à chaque transaction
 *
 * @author Dragons Vicieux
 *
 */
public final class Bibliotheque {

    private static BibliothequeCreateur bibliothequeCreateur;

    private static Log logger = LogFactory.getLog(Bibliotheque.class);

    /**
     * Constructeur privé pour empêcher toute instanciation.
     */
    private Bibliotheque() {
        super();
    }

    /**
     * Utilitaire de test de l'application bibliothèque.
     *
     * @param argv Les paramètres du main
     */
    public static void main(String[] argv) {
        // validation du nombre de paramètres
        if(argv.length < 1) {
            Bibliotheque.logger.info("Usage: java Biblio <fichier-transactions>");
            return;
        }

        try(
            // ouverture du fichier de transactions
            InputStream sourceTransaction = Bibliotheque.class.getResourceAsStream("/"
                + argv[0])) {

            try(
                BufferedReader reader = new BufferedReader(new InputStreamReader(sourceTransaction))) {
                Bibliotheque.bibliothequeCreateur = new BibliothequeCreateur();
                traiterTransactions(reader);
            }
        } catch(Exception exception) {
            Bibliotheque.logger.info(" **** "
                + exception.getMessage());
        }
    }

    /**
     * Traitement des transactions de la bibliothèque.
     *
     * @param reader Le reader à utiliser
     * @throws BibliothequeException S'il y a une erreur d'exécution
     * @throws IOException S'il y a une erreur de lecture
     */
    static void traiterTransactions(BufferedReader reader) throws BibliothequeException,
    IOException {
        afficherAide();
        String transaction;

        transaction = lireTransaction(reader);

        while(!finTransaction(transaction)) {

            final StringTokenizer tokenizer = new StringTokenizer(transaction,
                " ");
            if(tokenizer.hasMoreTokens()) {
                try {
                    executerTransaction(tokenizer);
                    transaction = lireTransaction(reader);
                } catch(BibliothequeException bibliothequeException) {
                    Bibliotheque.logger.error(bibliothequeException.getMessage());
                    transaction = lireTransaction(reader);
                }
            }
        }
    }

    /**
     * Lecture d'une transaction.
     *
     * @param reader Le reader à utiliser
     * @return lireTransactionString La transaction lue
     * @throws IOException S'il y a une erreur de lecture
     */
    private static String lireTransaction(BufferedReader reader) throws IOException {

        Bibliotheque.logger.info("> ");
        final String transaction = reader.readLine();

        if(transaction != null) {
            Bibliotheque.logger.info(transaction);
        }

        return transaction;
    }

    /**
     * Décodage et traitement d'une transaction.
     *
     * @param tokenizer Le tokenizer à utiliser
     * @throws BibliothequeException S'il y a une erreur d'exécution
     */
    private static void executerTransaction(StringTokenizer tokenizer) throws BibliothequeException {

        final String command = tokenizer.nextToken();

        switch(command) {
            case "aide":
                Bibliotheque.afficherAide();
                break;
            case "acquerir":
                Bibliotheque.acquerirLivre(tokenizer);
                break;
            case "vendre":
                Bibliotheque.vendreLivre(tokenizer);
                break;
            case "preter":
                Bibliotheque.commencerPret(tokenizer);
                break;
            case "renouveler":
                Bibliotheque.renouvelerPret(tokenizer);
                break;
            case "retourner":
                Bibliotheque.terminerPret(tokenizer);
                break;
            case "inscrire":
                Bibliotheque.inscrireMembre(tokenizer);
                break;
            case "desinscrire":
                Bibliotheque.desinscrireMembre(tokenizer);
                break;
            case "reserver":
                Bibliotheque.placerReservation(tokenizer);
                break;
            case "utiliser":
                Bibliotheque.utiliserReservation(tokenizer);
                break;
            case "annuler":
                Bibliotheque.annulerReservation(tokenizer);
                break;
            case "--":
                break;
            default:
                Bibliotheque.logger.info("  Transactions non reconnue.  Essayer \"aide\"");
                break;
        }

    }

    /**
     * Affiche le menu des transactions acceptées par le système.
     */
    private static void afficherAide() {
        Bibliotheque.logger.info("");
        Bibliotheque.logger.info("Chaque transaction comporte un nom et une liste d'arguments");
        Bibliotheque.logger.info("separes par des espaces. La liste peut etre vide.");
        Bibliotheque.logger.info(" Les dates sont en format yyyy-mm-dd.");
        Bibliotheque.logger.info("");
        Bibliotheque.logger.info("Les transactions sont:");
        Bibliotheque.logger.info("  aide");
        Bibliotheque.logger.info("  exit");
        Bibliotheque.logger.info("  acquerir <titre> <auteur>");
        Bibliotheque.logger.info("  preter <idLivre> <idMembre>");
        Bibliotheque.logger.info("  renouveler <idPret>");
        Bibliotheque.logger.info("  retourner <idPret>");
        Bibliotheque.logger.info("  vendre <idLivre>");
        Bibliotheque.logger.info("  inscrire <nom> <telephone> <limitePret>");
        Bibliotheque.logger.info("  desinscrire <idMembre>");
        Bibliotheque.logger.info("  reserver <idMembre> <idLivre> ");
        Bibliotheque.logger.info("  utiliser <idReservation>");
        Bibliotheque.logger.info("  annuler <idReservation>");
    }

    /**
     * Place une réservation.
     *
     * @param tokenizer Le tokenizer à utiliser
     * @throws BibliothequeException S'il y a une erreur
     */
    private static void placerReservation(StringTokenizer tokenizer) throws BibliothequeException {

        try {
            Bibliotheque.bibliothequeCreateur.beginTransaction();
            final ReservationDTO reservationDTO = new ReservationDTO();
            final String idMembre = readString(tokenizer);
            MembreDTO membreDTO;

            membreDTO = Bibliotheque.bibliothequeCreateur.getMembreFacade().getMembre(Bibliotheque.bibliothequeCreateur.getSession(),
                idMembre);

            if(membreDTO == null) {
                throw new MissingDTOException("Le membre "
                    + idMembre
                    + " n'existe pas");
            }
            final String idLivre = readString(tokenizer);
            final LivreDTO livreDTO = Bibliotheque.bibliothequeCreateur.getLivreFacade().getLivre(Bibliotheque.bibliothequeCreateur.getSession(),
                idLivre);
            if(livreDTO == null) {
                throw new MissingDTOException("Le livre "
                    + idLivre
                    + " n'existe pas");
            }
            reservationDTO.setLivreDTO(livreDTO);
            reservationDTO.setMembreDTO(membreDTO);

            Bibliotheque.bibliothequeCreateur.getReservationFacade().placerReservation(Bibliotheque.bibliothequeCreateur.getSession(),
                reservationDTO);
            Bibliotheque.bibliothequeCreateur.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidPrimaryKeyException
            | FacadeException
            | MissingDTOException
            | InvalidDTOException
            | MissingLoanException
            | ExistingLoanException
            | ExistingReservationException exception) {
            Bibliotheque.logger.error(" **** "
                + exception.getMessage());
            Bibliotheque.bibliothequeCreateur.rollbackTransaction();
        }
    }

    /**
     * Utilise une réservation.
     *
     * @param tokenizer Le tokenizer à utiliser
     * @throws BibliothequeException S'il y a une erreur
     */
    private static void utiliserReservation(StringTokenizer tokenizer) throws BibliothequeException {

        try {
            Bibliotheque.bibliothequeCreateur.beginTransaction();
            final String idReservation = readString(tokenizer);
            final ReservationDTO reservationDTO = Bibliotheque.bibliothequeCreateur.getReservationFacade()
                .getReservation(Bibliotheque.bibliothequeCreateur.getSession(),
                    idReservation);
            if(reservationDTO == null) {
                throw new MissingDTOException("La reservation "
                    + idReservation
                    + " n'existe pas");
            }
            Bibliotheque.bibliothequeCreateur.getReservationFacade().utiliserReservation(Bibliotheque.bibliothequeCreateur.getSession(),
                reservationDTO);
            Bibliotheque.bibliothequeCreateur.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidPrimaryKeyException
            | FacadeException
            | MissingDTOException
            | InvalidDTOException
            | ExistingReservationException
            | ExistingLoanException
            | InvalidLoanLimitException exceptions) {
            Bibliotheque.logger.error(" **** "
                + exceptions.getMessage());
            Bibliotheque.bibliothequeCreateur.rollbackTransaction();
        }
    }

    /**
     * Annule une réservation.
     *
     * @param tokenizer Le tokenizer à utiliser
     * @throws BibliothequeException S'il y a une erreur
     */
    private static void annulerReservation(StringTokenizer tokenizer) throws BibliothequeException {

        try {
            Bibliotheque.bibliothequeCreateur.beginTransaction();
            final String idReservation = readString(tokenizer);
            final ReservationDTO reservationDTO = Bibliotheque.bibliothequeCreateur.getReservationFacade()
                .getReservation(Bibliotheque.bibliothequeCreateur.getSession(),
                    idReservation);
            if(reservationDTO == null) {
                throw new MissingDTOException("La reservation "
                    + idReservation
                    + " n'existe pas");
            }
            Bibliotheque.bibliothequeCreateur.getReservationFacade().annulerReservation(Bibliotheque.bibliothequeCreateur.getSession(),
                reservationDTO);
            Bibliotheque.bibliothequeCreateur.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidPrimaryKeyException
            | FacadeException
            | MissingDTOException
            | InvalidDTOException exceptions) {
            Bibliotheque.logger.error(" **** "
                + exceptions.getMessage());
            Bibliotheque.bibliothequeCreateur.rollbackTransaction();
        }
    }

    /**
     * Acquiert un livre.
     *
     * @param tokenizer Le tokenizer à utiliser
     * @throws BibliothequeException S'il y a une erreur
     */
    private static void acquerirLivre(StringTokenizer tokenizer) throws BibliothequeException {

        try {
            Bibliotheque.bibliothequeCreateur.beginTransaction();

            final LivreDTO livreDTO = new LivreDTO();
            livreDTO.setTitre(readString(tokenizer));
            livreDTO.setAuteur(readString(tokenizer));
            livreDTO.setDateAcquisition(readDate(tokenizer));
            Bibliotheque.bibliothequeCreateur.getLivreFacade().acquerirLivre(Bibliotheque.bibliothequeCreateur.getSession(),
                livreDTO);
            Bibliotheque.bibliothequeCreateur.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidDTOException
            | FacadeException exceptions) {
            Bibliotheque.logger.error(" **** "
                + exceptions.getMessage());
            Bibliotheque.bibliothequeCreateur.rollbackTransaction();
        }

    }

    /**
     * Vend un livre.
     *
     * @param tokenizer Le tokenizer à utiliser
     * @throws BibliothequeException S'il y a une erreur
     */
    private static void vendreLivre(StringTokenizer tokenizer) throws BibliothequeException {
        try {
            Bibliotheque.bibliothequeCreateur.beginTransaction();
            final String idLivre = readString(tokenizer);
            final LivreDTO livreDTO = Bibliotheque.bibliothequeCreateur.getLivreFacade().getLivre(Bibliotheque.bibliothequeCreateur.getSession(),
                idLivre);
            if(livreDTO == null) {
                throw new MissingDTOException("Le livre "
                    + idLivre
                    + " n'existe pas");
            }
            Bibliotheque.bibliothequeCreateur.getLivreFacade().vendreLivre(Bibliotheque.bibliothequeCreateur.getSession(),
                livreDTO);
            Bibliotheque.bibliothequeCreateur.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidPrimaryKeyException
            | FacadeException
            | MissingDTOException
            | InvalidDTOException
            | ExistingLoanException
            | ExistingReservationException exceptions) {
            Bibliotheque.logger.error(" **** "
                + exceptions.getMessage());
            Bibliotheque.bibliothequeCreateur.rollbackTransaction();
        }
    }

    /**
     * Commence un prêt.
     *
     * @param tokenizer Le tokenizer à utiliser
     * @throws BibliothequeException S'il y a une erreur
     */
    private static void commencerPret(StringTokenizer tokenizer) throws BibliothequeException {
        try {
            Bibliotheque.bibliothequeCreateur.beginTransaction();

            final String idLivre = readString(tokenizer);
            final LivreDTO livreDTO = Bibliotheque.bibliothequeCreateur.getLivreFacade().getLivre(Bibliotheque.bibliothequeCreateur.getSession(),
                idLivre);
            if(livreDTO == null) {
                throw new MissingDTOException("Le livre "
                    + idLivre
                    + " n'existe pas");
            }

            final String idMembre = readString(tokenizer);
            final MembreDTO membreDTO = Bibliotheque.bibliothequeCreateur.getMembreFacade().getMembre(Bibliotheque.bibliothequeCreateur.getSession(),
                idMembre);
            if(membreDTO == null) {
                throw new MissingDTOException("Le membre "
                    + idMembre
                    + " n'existe pas");
            }

            final PretDTO pretDTO = new PretDTO();
            pretDTO.setLivreDTO(livreDTO);
            pretDTO.setMembreDTO(membreDTO);
            Bibliotheque.bibliothequeCreateur.getPretFacade().commencerPret(Bibliotheque.bibliothequeCreateur.getSession(),
                pretDTO);
            Bibliotheque.bibliothequeCreateur.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidPrimaryKeyException
            | FacadeException
            | MissingDTOException
            | InvalidDTOException
            | ExistingLoanException
            | InvalidLoanLimitException
            | ExistingReservationException exceptions) {
            Bibliotheque.logger.error(" **** "
                + exceptions.getMessage());
            Bibliotheque.bibliothequeCreateur.rollbackTransaction();
        }
    }

    /**
     * Renouvelle un prêt.
     *
     * @param tokenizer Le tokenizer à utiliser
     * @throws BibliothequeException S'il y a une erreur
     */
    private static void renouvelerPret(StringTokenizer tokenizer) throws BibliothequeException {
        try {
            Bibliotheque.bibliothequeCreateur.beginTransaction();
            final String idPret = readString(tokenizer);
            final PretDTO pretDTO = Bibliotheque.bibliothequeCreateur.getPretFacade().getPret(Bibliotheque.bibliothequeCreateur.getSession(),
                idPret);
            if(pretDTO == null) {
                throw new MissingDTOException("Le pret "
                    + idPret
                    + " n'existe pas");
            }
            Bibliotheque.bibliothequeCreateur.getPretFacade().renouvelerPret(Bibliotheque.bibliothequeCreateur.getSession(),
                pretDTO);
            Bibliotheque.bibliothequeCreateur.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidPrimaryKeyException
            | FacadeException
            | MissingDTOException
            | InvalidDTOException
            | MissingLoanException
            | ExistingReservationException exceptions) {
            Bibliotheque.logger.error(" **** "
                + exceptions.getMessage());
            Bibliotheque.bibliothequeCreateur.rollbackTransaction();
        }
    }

    /**
     * Termine un prêt.
     *
     * @param tokenizer Le tokenizer à utiliser
     * @throws BibliothequeException S'il y a une erreur
     */
    private static void terminerPret(StringTokenizer tokenizer) throws BibliothequeException {
        try {
            Bibliotheque.bibliothequeCreateur.beginTransaction();
            final String idPret = readString(tokenizer);
            final PretDTO pretDTO = Bibliotheque.bibliothequeCreateur.getPretFacade().getPret(Bibliotheque.bibliothequeCreateur.getSession(),
                idPret);
            if(pretDTO == null) {
                throw new MissingDTOException("Le pret "
                    + idPret
                    + " n'existe pas");
            }
            Bibliotheque.bibliothequeCreateur.getPretFacade().terminerPret(Bibliotheque.bibliothequeCreateur.getSession(),
                pretDTO);
            Bibliotheque.bibliothequeCreateur.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidPrimaryKeyException
            | FacadeException
            | MissingDTOException
            | InvalidDTOException
            | MissingLoanException exceptions) {
            Bibliotheque.logger.error(" **** "
                + exceptions.getMessage());
            Bibliotheque.bibliothequeCreateur.rollbackTransaction();
        }

    }

    /**
     * Inscrit un membre.
     *
     * @param tokenizer Le tokenizer à utiliser
     * @throws BibliothequeException S'il y a une erreur
     */
    private static void inscrireMembre(StringTokenizer tokenizer) throws BibliothequeException {
        try {
            Bibliotheque.bibliothequeCreateur.beginTransaction();
            final MembreDTO membreDTO = new MembreDTO();
            membreDTO.setNom(readString(tokenizer));
            membreDTO.setTelephone(Long.valueOf(readString(tokenizer)).longValue());
            membreDTO.setLimitePret(Integer.parseInt(readString(tokenizer)));
            membreDTO.setNbPret(0);

            Bibliotheque.bibliothequeCreateur.getMembreFacade().inscrireMembre(Bibliotheque.bibliothequeCreateur.getSession(),
                membreDTO);
            Bibliotheque.bibliothequeCreateur.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidDTOException
            | FacadeException exceptions) {
            Bibliotheque.logger.error(" **** "
                + exceptions.getMessage());
            Bibliotheque.bibliothequeCreateur.rollbackTransaction();
        }
    }

    /**
     * Désinscrit un membre.
     *
     * @param tokenizer Le tokenizer à utiliser
     * @throws BibliothequeException S'il y a une erreur
     */
    private static void desinscrireMembre(StringTokenizer tokenizer) throws BibliothequeException {

        try {
            Bibliotheque.bibliothequeCreateur.beginTransaction();
            final String idMembre = readString(tokenizer);
            final MembreDTO membreDTO = Bibliotheque.bibliothequeCreateur.getMembreFacade().getMembre(Bibliotheque.bibliothequeCreateur.getSession(),
                idMembre);
            if(membreDTO == null) {
                throw new MissingDTOException("Le membre "
                    + idMembre
                    + " n'existe pas");
            }
            Bibliotheque.bibliothequeCreateur.getMembreFacade().desinscrireMembre(Bibliotheque.bibliothequeCreateur.getSession(),
                membreDTO);
            Bibliotheque.bibliothequeCreateur.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidPrimaryKeyException
            | FacadeException
            | MissingDTOException
            | InvalidDTOException
            | ExistingLoanException
            | ExistingReservationException exceptions) {
            Bibliotheque.logger.error(" **** "
                + exceptions.getMessage());
            Bibliotheque.bibliothequeCreateur.rollbackTransaction();
        }
    }

    /**
     * Vérifie si la fin du traitement des transactions est atteinte.
     *
     * @param transaction La transaction à vérifier
     * @return boolean <code>true</code> si la fin du traitement des transactions est atteinte ; <code>false</code> sinon
     */
    private static boolean finTransaction(String transaction) {
        boolean finTransaction = false;

        if(transaction == null) {
            finTransaction = true;
        }

        final StringTokenizer tokenizer = new StringTokenizer(transaction,
            " ");
        if(!tokenizer.hasMoreTokens()) {
            finTransaction = false;
        }

        return finTransaction;

    }

    /**
     *
     * Lecture d'une chaîne de caractères de la transaction entrée à l'écran.
     *
     * @param tokenizer Le tokenizer à utiliser
     * @return String readString La ligne
     * @throws BibliothequeException S'il y a une erreur
     */
    private static String readString(StringTokenizer tokenizer) throws BibliothequeException {
        if(tokenizer.hasMoreElements()) {
            return tokenizer.nextToken();
        }
        throw new BibliothequeException("autre paramètre attendu");
    }

    /**
     *
     * Lecture d'une date en format YYYY-MM-DD.
     *
     * @param tokenizer Le tokenizer à utiliser
     * @return String readDate La date en format YYYY-MM-DD
     * @throws BibliothequeException S'il y a un problème de lecture
     */
    private static Timestamp readDate(StringTokenizer tokenizer) throws BibliothequeException {
        if(tokenizer.hasMoreElements()) {
            final String token = tokenizer.nextToken();
            try {
                return FormatteurDate.timestampValue(token);
            } catch(ParseException e) {
                throw new BibliothequeException("Date en format YYYY-MM-DD attendue à la place  de \""
                    + token
                    + "\"");
            }
        }
        throw new BibliothequeException("autre paramètre attendu");
    }
}
