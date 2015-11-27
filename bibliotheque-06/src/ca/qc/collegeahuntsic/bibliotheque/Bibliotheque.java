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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Interface du système de gestion d'une bibliothèque.
 *
 * @author Dragon Vicieux
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
     * Utilitaire de test de l'application biliotèque.
     *
     * @param argv Les paramètres du main
     * @throws Exception
     */
    public static void main(String[] argv) throws Exception {
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
     * @throws BibliothequeException
     */
    static void traiterTransactions(BufferedReader reader) throws BibliothequeException {
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
                    continue;
                }
            }

        }
    }

    /**
     * Lecture d'une transaction.
     *
     * @param reader Le reader à utiliser
     * @return lireTransactionString La transaction lue
     * @throws BibliothequeException
     */
    static String lireTransaction(BufferedReader reader) throws BibliothequeException {
        try {
            Bibliotheque.logger.info("> ");
            final String transaction = reader.readLine();

            if(transaction != null) {
                Bibliotheque.logger.info(transaction);
            }

            return transaction;
        } catch(IOException ioException) {
            throw new BibliothequeException(ioException);
        }
    }

    /**
     * Décodage et traitement d'une transaction.
     *
     * @param tokenizer Le tokenizer à utiliser
     * @throws BibliothequeException S'il y a une erreur d'exécution
     */
    static void executerTransaction(StringTokenizer tokenizer) throws BibliothequeException {

        try {

            final String command = tokenizer.nextToken();

            /* ******************* */
            /*         HELP        */
            /* ******************* */

            if("aide".startsWith(command)) {

                afficherAide();

            } else if("acquerir".startsWith(command)) {
                // TRANSACTION ACQUERIR ( <titre> <auteur> )

                Bibliotheque.bibliothequeCreateur.beginTransaction();

                final LivreDTO livreDTO = new LivreDTO();
                livreDTO.setTitre(readString(tokenizer));
                livreDTO.setAuteur(readString(tokenizer));
                livreDTO.setDateAcquisition(new Timestamp(System.currentTimeMillis()));
                Bibliotheque.bibliothequeCreateur.getLivreFacade().acquerirLivre(Bibliotheque.bibliothequeCreateur.getSession(),
                    livreDTO);

                Bibliotheque.bibliothequeCreateur.commitTransaction();

            } else if("vendre".startsWith(command)) {
                // TRANSACTION VENDRE ( <idLivre> )
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

            } else if("preter".startsWith(command)) {

                // TRANSACTION PRETER ( <idLivre> <idMembre> )
                // récupération du livre

                Bibliotheque.bibliothequeCreateur.beginTransaction();
                final String idLivre = readString(tokenizer);
                final LivreDTO livreDTO = Bibliotheque.bibliothequeCreateur.getLivreFacade().getLivre(Bibliotheque.bibliothequeCreateur.getSession(),
                    idLivre);
                if(livreDTO == null) {
                    throw new MissingDTOException("Le livre "
                        + idLivre
                        + " n'existe pas");
                }
                // récupération du membre
                final String idMembre = readString(tokenizer);
                final MembreDTO membreDTO = Bibliotheque.bibliothequeCreateur.getMembreFacade().getMembre(Bibliotheque.bibliothequeCreateur.getSession(),
                    idMembre);
                if(membreDTO == null) {
                    throw new MissingDTOException("Le membre "
                        + idMembre
                        + " n'existe pas");
                }
                // création du nouveau prêt
                final PretDTO pretDTO = new PretDTO();
                pretDTO.setLivreDTO(livreDTO);
                pretDTO.setMembreDTO(membreDTO);

                Bibliotheque.bibliothequeCreateur.getPretFacade().commencerPret(Bibliotheque.bibliothequeCreateur.getSession(),
                    pretDTO);

                Bibliotheque.bibliothequeCreateur.commitTransaction();

            } else if("renouveler".startsWith(command)) {

                // TRANSACTION RENOUVELER ( <idPret> )
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

            } else if("retourner".startsWith(command)) {

                // TRANSACTION RETOURNER ( <idPret> )

                // TODO Est-ce que retourner 3 signifie retourner le livre 3 ou le pret 3 ?

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

            } else if("inscrire".startsWith(command)) {

                // TRANSACTION INSCRIRE ( <nom> <telephone> <limitePret> )

                Bibliotheque.bibliothequeCreateur.beginTransaction();
                final MembreDTO membreDTO = new MembreDTO();
                membreDTO.setNom(readString(tokenizer));
                membreDTO.setTelephone(readLong(tokenizer));
                membreDTO.setLimitePret(readInt(tokenizer));
                membreDTO.setNbPret(0);

                Bibliotheque.bibliothequeCreateur.getMembreFacade().inscrireMembre(Bibliotheque.bibliothequeCreateur.getSession(),
                    membreDTO);

                Bibliotheque.bibliothequeCreateur.commitTransaction();

            } else if("desinscrire".startsWith(command)) {

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

            } else if("reserver".startsWith(command)) {

                Bibliotheque.bibliothequeCreateur.beginTransaction();

                final ReservationDTO reservationDTO = new ReservationDTO();

                final String idMembre = readString(tokenizer);
                final MembreDTO membreDTO = Bibliotheque.bibliothequeCreateur.getMembreFacade().getMembre(Bibliotheque.bibliothequeCreateur.getSession(),
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

            } else if("utiliser".startsWith(command)) {

                // TRANSACTION UTILISER ( <idReservation> )

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

            } else if("annuler".startsWith(command)) {

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

            }

            else if("--".startsWith(command)) {
                // TODO empty block
            }// ne rien faire; c'est un commentaire

            /* ***********************   */
            /* TRANSACTION NON RECONNUEE */
            /* ***********************   */
            else {
                Bibliotheque.logger.info("  Transactions non reconnue.  Essayer \"aide\"");
            }

        } catch(Exception exception) {
            Bibliotheque.logger.error(" **** "
                + exception.getMessage());
            Bibliotheque.bibliothequeCreateur.rollbackTransaction();
        }
    }

    /**
     *
     * Affiche le menu des transactions acceptées par le système
     *
     */
    static void afficherAide() {
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
     *
     * Vérifie si la fin du traitement des transactions est atteinte.
     *
     * @param transaction
     * @return boolean finTransaction
     */
    static boolean finTransaction(String transaction) {
        /* fin de fichier atteinte */

        if(transaction == null) {
            return true;
        }

        final StringTokenizer tokenizer = new StringTokenizer(transaction,
            " ");

        /* ligne ne contenant que des espaces */
        if(!tokenizer.hasMoreTokens()) {
            return false;
        }

        /* commande "exit" */
        final String commande = tokenizer.nextToken();

        return commande.equals("exit");
    }

    /**
     *
     * Lecture d'une chaîne de caractères de la transaction entrée à l'écran
     *
     * @param tokenizer
     * @return String readString
     * @throws BibliothequeException
     */
    static String readString(StringTokenizer tokenizer) throws BibliothequeException {
        if(tokenizer.hasMoreElements()) {
            return tokenizer.nextToken();
        }
        throw new BibliothequeException("autre paramètre attendu");
    }

    /**
     *
     * Lecture d'un int java de la transaction entrée à l'écran
     *
     * @param tokenizer
     * @return int readInt
     * @throws BibliothequeException
     */
    static int readInt(StringTokenizer tokenizer) throws BibliothequeException {
        if(tokenizer.hasMoreElements()) {
            final String token = tokenizer.nextToken();
            try {
                return Integer.valueOf(token).intValue();
            } catch(NumberFormatException e) {
                throw new BibliothequeException("Nombre attendu à la place de \""
                    + token
                    + "\"");
            }
        }
        throw new BibliothequeException("autre paramètre attendu");
    }

    /**
     *
     * Lecture d'un long java de la transaction entrée à l'écran
     *
     * @param tokenizer
     * @return long readLong
     * @throws BibliothequeException
     */
    static long readLong(StringTokenizer tokenizer) throws BibliothequeException {
        if(tokenizer.hasMoreElements()) {
            final String token = tokenizer.nextToken();
            try {
                return Long.valueOf(token).longValue();
            } catch(NumberFormatException e) {
                throw new BibliothequeException("Nombre attendu à la place de \""
                    + token
                    + "\"");
            }
        }
        throw new BibliothequeException("autre paramètre attendu");
    }

    /**
     *
     * Lecture d'une date en format YYYY-MM-DD
     *
     * @param tokenizer
     * @return String readDate
     * @throws BibliothequeException
     */
    static String readDate(StringTokenizer tokenizer) throws BibliothequeException {
        if(tokenizer.hasMoreElements()) {
            final String token = tokenizer.nextToken();
            try {
                FormatteurDate.convertirDate(token);
                return token;
            } catch(ParseException e) {
                throw new BibliothequeException("Date en format YYYY-MM-DD attendue à la place  de \""
                    + token
                    + "\"");
            }
        }
        throw new BibliothequeException("autre paramètre attendu");
    }

}//class
