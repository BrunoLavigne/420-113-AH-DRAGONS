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
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.BibliothequeException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.MissingDTOException;
import ca.qc.collegeahuntsic.bibliotheque.util.BibliothequeCreateur;
import ca.qc.collegeahuntsic.bibliotheque.util.FormatteurDate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * Interface du système de gestion d'une bibliothèque
 *
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
 * Pré-condition
 *   La base de données de la bibliothèque doit exister
 *
 * Post-condition
 *   Le programme effectue les mises à jour associées à chaque transaction
 *
 */
public class Bibliotheque {

    private static BibliothequeCreateur bibliothequeCreateur;

    private static Log LOGGER = LogFactory.getLog(Bibliotheque.class);

    /**
     *
     * Ouverture de la BD, traitement des transactions et fermeture de la BD.
     * @throws Exception
     */
    public static void main(String[] argv) throws Exception {
        // validation du nombre de paramètres
        if(argv.length < 1) {
            Bibliotheque.LOGGER.info("Usage: java Biblio <fichier-transactions>");
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
            Bibliotheque.LOGGER.info(" **** "
                + exception.getMessage());
        }
    }

    /**
     *
     * Traitement des transactions de la bibliothèque
     *
     * @param reader
     * @throws BibliothequeException
     */
    static void traiterTransactions(BufferedReader reader) throws BibliothequeException {
        afficherAide();
        String transaction;

        transaction = lireTransaction(reader);

        while(!finTransaction(transaction)) {

            StringTokenizer tokenizer = new StringTokenizer(transaction,
                " ");
            if(tokenizer.hasMoreTokens()) {
                try {
                    executerTransaction(tokenizer);
                    transaction = lireTransaction(reader);
                } catch(BibliothequeException bibliothequeException) {
                    Bibliotheque.LOGGER.error(bibliothequeException.getMessage());
                    transaction = lireTransaction(reader);
                    continue;
                }
            }

        }
    }

    /**
     *
     * Lecture d'une transaction
     *
     * @param reader
     * @return lireTransactionString
     * @throws BibliothequeException
     */
    static String lireTransaction(BufferedReader reader) throws BibliothequeException {
        try {
            Bibliotheque.LOGGER.info("> ");
            String transaction = reader.readLine();

            if(transaction != null) {
                Bibliotheque.LOGGER.info(transaction);
            }

            return transaction;
        } catch(IOException ioException) {
            throw new BibliothequeException(ioException);
        }
    }

    /**
     *
     * Décodage et traitement d'une transaction
     *
     * @param tokenizer
     * @throws BibliothequeException
     */
    static void executerTransaction(StringTokenizer tokenizer) throws BibliothequeException {

        try {

            String command = tokenizer.nextToken();

            /* ******************* */
            /*         HELP        */
            /* ******************* */

            if("aide".startsWith(command)) {

                afficherAide();

            } else if("acquerir".startsWith(command)) {
                // TRANSACTION ACQUERIR ( <titre> <auteur> )

                Bibliotheque.bibliothequeCreateur.beginTransaction();

                LivreDTO livreDTO = new LivreDTO();
                livreDTO.setTitre(readString(tokenizer));
                livreDTO.setAuteur(readString(tokenizer));
                livreDTO.setDateAcquisition(new Timestamp(System.currentTimeMillis()));
                Bibliotheque.bibliothequeCreateur.getLivreFacade().acquerirLivre(Bibliotheque.bibliothequeCreateur.getSession(),
                    livreDTO);

                Bibliotheque.bibliothequeCreateur.commitTransaction();

            } else if("vendre".startsWith(command)) {
                // TRANSACTION VENDRE ( <idLivre> )
                Bibliotheque.bibliothequeCreateur.beginTransaction();
                String idLivre = readString(tokenizer);
                LivreDTO livreDTO = Bibliotheque.bibliothequeCreateur.getLivreFacade().getLivre(Bibliotheque.bibliothequeCreateur.getSession(),
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
                String idLivre = readString(tokenizer);
                LivreDTO livreDTO = Bibliotheque.bibliothequeCreateur.getLivreFacade().getLivre(Bibliotheque.bibliothequeCreateur.getSession(),
                    idLivre);
                if(livreDTO == null) {
                    throw new MissingDTOException("Le livre "
                        + idLivre
                        + " n'existe pas");
                }
                // récupération du membre
                String idMembre = readString(tokenizer);
                MembreDTO membreDTO = Bibliotheque.bibliothequeCreateur.getMembreFacade().getMembre(Bibliotheque.bibliothequeCreateur.getSession(),
                    idMembre);
                if(membreDTO == null) {
                    throw new MissingDTOException("Le membre "
                        + idMembre
                        + " n'existe pas");
                }
                // création du nouveau prêt
                PretDTO pretDTO = new PretDTO();
                pretDTO.setLivreDTO(livreDTO);
                pretDTO.setMembreDTO(membreDTO);

                Bibliotheque.bibliothequeCreateur.getPretFacade().commencerPret(Bibliotheque.bibliothequeCreateur.getSession(),
                    pretDTO);

                Bibliotheque.bibliothequeCreateur.commitTransaction();

            } else if("renouveler".startsWith(command)) {

                // TRANSACTION RENOUVELER ( <idPret> )
                Bibliotheque.bibliothequeCreateur.beginTransaction();
                String idPret = readString(tokenizer);

                PretDTO pretDTO = Bibliotheque.bibliothequeCreateur.getPretFacade().getPret(Bibliotheque.bibliothequeCreateur.getSession(),
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

                String idPret = readString(tokenizer);
                PretDTO pretDTO = Bibliotheque.bibliothequeCreateur.getPretFacade().getPret(Bibliotheque.bibliothequeCreateur.getSession(),
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
                MembreDTO membreDTO = new MembreDTO();
                membreDTO.setNom(readString(tokenizer));
                membreDTO.setTelephone(readLong(tokenizer));
                membreDTO.setLimitePret(readInt(tokenizer));
                membreDTO.setNbPret(0);

                Bibliotheque.bibliothequeCreateur.getMembreFacade().inscrireMembre(Bibliotheque.bibliothequeCreateur.getSession(),
                    membreDTO);

                Bibliotheque.bibliothequeCreateur.commitTransaction();

            } else if("desinscrire".startsWith(command)) {

                Bibliotheque.bibliothequeCreateur.beginTransaction();

                String idMembre = readString(tokenizer);
                MembreDTO membreDTO = Bibliotheque.bibliothequeCreateur.getMembreFacade().getMembre(Bibliotheque.bibliothequeCreateur.getSession(),
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

                ReservationDTO reservationDTO = new ReservationDTO();

                String idMembre = readString(tokenizer);
                MembreDTO membreDTO = Bibliotheque.bibliothequeCreateur.getMembreFacade().getMembre(Bibliotheque.bibliothequeCreateur.getSession(),
                    idMembre);

                if(membreDTO == null) {
                    throw new MissingDTOException("Le membre "
                        + idMembre
                        + " n'existe pas");
                }

                String idLivre = readString(tokenizer);
                LivreDTO livreDTO = Bibliotheque.bibliothequeCreateur.getLivreFacade().getLivre(Bibliotheque.bibliothequeCreateur.getSession(),
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
                String idReservation = readString(tokenizer);

                ReservationDTO reservationDTO = Bibliotheque.bibliothequeCreateur.getReservationFacade()
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
                String idReservation = readString(tokenizer);

                ReservationDTO reservationDTO = Bibliotheque.bibliothequeCreateur.getReservationFacade()
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
                Bibliotheque.LOGGER.info("  Transactions non reconnue.  Essayer \"aide\"");
            }

        } catch(Exception exception) {
            Bibliotheque.LOGGER.error(" **** "
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
        Bibliotheque.LOGGER.info("");
        Bibliotheque.LOGGER.info("Chaque transaction comporte un nom et une liste d'arguments");
        Bibliotheque.LOGGER.info("separes par des espaces. La liste peut etre vide.");
        Bibliotheque.LOGGER.info(" Les dates sont en format yyyy-mm-dd.");
        Bibliotheque.LOGGER.info("");
        Bibliotheque.LOGGER.info("Les transactions sont:");
        Bibliotheque.LOGGER.info("  aide");
        Bibliotheque.LOGGER.info("  exit");
        Bibliotheque.LOGGER.info("  acquerir <titre> <auteur>");
        Bibliotheque.LOGGER.info("  preter <idLivre> <idMembre>");
        Bibliotheque.LOGGER.info("  renouveler <idPret>");
        Bibliotheque.LOGGER.info("  retourner <idPret>");
        Bibliotheque.LOGGER.info("  vendre <idLivre>");
        Bibliotheque.LOGGER.info("  inscrire <nom> <telephone> <limitePret>");
        Bibliotheque.LOGGER.info("  desinscrire <idMembre>");
        Bibliotheque.LOGGER.info("  reserver <idMembre> <idLivre> ");
        Bibliotheque.LOGGER.info("  utiliser <idReservation>");
        Bibliotheque.LOGGER.info("  annuler <idReservation>");

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

        StringTokenizer tokenizer = new StringTokenizer(transaction,
            " ");

        /* ligne ne contenant que des espaces */
        if(!tokenizer.hasMoreTokens()) {
            return false;
        }

        /* commande "exit" */
        String commande = tokenizer.nextToken();

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
            String token = tokenizer.nextToken();
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
            String token = tokenizer.nextToken();
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
            String token = tokenizer.nextToken();
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
