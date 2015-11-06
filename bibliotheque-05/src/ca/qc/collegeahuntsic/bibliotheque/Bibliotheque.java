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
import java.util.Calendar;
import java.util.StringTokenizer;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.BibliothequeException;
import ca.qc.collegeahuntsic.bibliotheque.exception.db.ConnexionException;
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
 * <ul>
 * <li>0- Site du serveur SQL ("local" ou "distant")</li>
 * <li>1- Nom de la BD</li>
 * <li>2- User id pour établir une connexion avec le serveur SQL</li>
 * <li>3- Mot de passe pour le user id</li>
 * <li>4- Fichier de transaction</li>
 * </ul>
 *
 * Pré-condition
 *   La base de données de la bibliothèque doit exister
 *
 * Post-condition
 *   Le programme effectue les mises à jour associées à chaque transaction
 *
 */
public class Bibliotheque {

    private static BibliothequeCreateur gestionBibliotheque;

    private static Log LOGGER = LogFactory.getLog(Bibliotheque.class);

    /**
     *
     * Ouverture de la BD, traitement des transactions et fermeture de la BD.
     * @throws Exception
     */
    public static void main(String argv[]) throws Exception {
        // validation du nombre de paramètres
        if(argv.length < 5) {
            Bibliotheque.LOGGER.info("Usage: java Biblio <serveur> <bd> <user> <password> <fichier-transactions>");
            Bibliotheque.LOGGER.info(Connexion.getServeursSupportes());
            return;
        }

        try(
            InputStream sourceTransaction = Bibliotheque.class.getResourceAsStream("/"
                + argv[4])) {
            // ouverture du fichier de transactions

            try(
                BufferedReader reader = new BufferedReader(new InputStreamReader(sourceTransaction))) {
                setGestionBiblio(new BibliothequeCreateur(argv[0],
                    argv[1],
                    argv[2],
                    argv[3]));
                traiterTransactions(reader);
                reader.close();
            }
        } catch(Exception exception) {
            throw new BibliothequeException(exception);
        } finally {
            getGestionBiblio().getConnexion().close();
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
            /* découpage de la transaction en mots*/
            // TODO Remplacer par un SPLIT. StringTokenizer est déprécier
            StringTokenizer tokenizer = new StringTokenizer(transaction,
                " ");
            if(tokenizer.hasMoreTokens()) {
                try {
                    executerTransaction(tokenizer);
                    transaction = lireTransaction(reader);
                } catch(BibliothequeException bibliothequeException) {
                    System.err.println(bibliothequeException.getMessage());
                    //bibliothequeException.printStackTrace();
                    /*
                    try {
                        Thread.sleep(300); //1000 milliseconds is one second.
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                     */
                    transaction = lireTransaction(reader);
                    continue;
                }
            }
            /*
             * TODO Big time, changer le tokenizer pour un split va prendre du temps;
             * il faut modifier aussi la methodeexecuterTransaction...
             *
            String[] parts = transaction.split(" ");
            for (int i=0; i<parts.length; i++){
                executerTransaction(parts[i]);
            }
             */
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

                LivreDTO livreDTO = new LivreDTO();

                //livreDTO.setIdLivre(readInt(tokenizer));
                livreDTO.setTitre(readString(tokenizer));
                livreDTO.setAuteur(readString(tokenizer));
                livreDTO.setDateAcquisition(new Timestamp(System.currentTimeMillis()));

                getGestionBiblio().getLivreFacade().acquerir(getGestionBiblio().getConnexion(),
                    livreDTO);

            } else if("vendre".startsWith(command)) {

                LivreDTO livreDTO = new LivreDTO();
                livreDTO.setIdLivre(readString(tokenizer));

                getGestionBiblio().getLivreFacade().vendre(getGestionBiblio().getConnexion(),
                    livreDTO);

            } else if("preter".startsWith(command)) {

                // TRANSACTION PRETER ( <idLivre> <idMembre> )

                LivreDTO livreDTO = new LivreDTO();
                livreDTO.setIdLivre(readString(tokenizer));

                MembreDTO membreDTO = new MembreDTO();
                membreDTO.setIdMembre(readString(tokenizer));

                PretDTO pretDTO = new PretDTO();
                pretDTO.setLivreDTO(livreDTO);
                pretDTO.setMembreDTO(membreDTO);
                pretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));

                getGestionBiblio().getPretFacade().commencer(getGestionBiblio().getConnexion(),
                    pretDTO);

            } else if("renouveler".startsWith(command)) {

                // TRANSACTION RENOUVELER ( <idPret> )

                PretDTO pretDTO = new PretDTO();
                pretDTO.setIdPret(readString(tokenizer));

                getGestionBiblio().getPretFacade().renouveler(getGestionBiblio().getConnexion(),
                    pretDTO);

            } else if("retourner".startsWith(command)) {

                // TRANSACTION RETOURNER ( <idPret> )

                PretDTO pretDTO = new PretDTO();
                pretDTO.setIdPret(readString(tokenizer));

                getGestionBiblio().getPretFacade().terminer(getGestionBiblio().getConnexion(),
                    pretDTO);

            } else if("inscrire".startsWith(command)) {

                // TRANSACTION INSCRIRE ( <nom> <telephone> <limitePret> )

                MembreDTO membreDTO = new MembreDTO();

                membreDTO.setNom(readString(tokenizer));
                membreDTO.setTelephone(readLong(tokenizer));
                membreDTO.setLimitePret(readInt(tokenizer));

                getGestionBiblio().getMembreFacade().inscrire(getGestionBiblio().getConnexion(),
                    membreDTO);

            } else if("desinscrire".startsWith(command)) {

                // TRANSACTION DESINSCRIRE ( <idMembre> )

                MembreDTO membreDTO = new MembreDTO();
                membreDTO.setIdMembre(readString(tokenizer));

                getGestionBiblio().getMembreFacade().desinscrire(getGestionBiblio().getConnexion(),
                    membreDTO);

            } else if("reserver".startsWith(command)) {

                // TRANSACTION RESERVER ( <idMembre> <idLivre> )

                ReservationDTO reservationDTO = new ReservationDTO();

                MembreDTO membreDTO = new MembreDTO();
                membreDTO.setIdMembre(readString(tokenizer));

                LivreDTO livreDTO = new LivreDTO();
                livreDTO.setIdLivre(readString(tokenizer));

                Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());

                reservationDTO.setLivreDTO(livreDTO);
                reservationDTO.setMembreDTO(membreDTO);
                reservationDTO.setDateReservation(currentTimestamp);

                getGestionBiblio().getReservationFacade().placer(getGestionBiblio().getConnexion(),
                    reservationDTO);

            } else if("utiliser".startsWith(command)) {

                // TRANSACTION UTILISER ( <idReservation> )

                ReservationDTO reservationDTO = new ReservationDTO();
                reservationDTO.setIdReservation(readString(tokenizer));

                getGestionBiblio().getReservationFacade().utiliser(getGestionBiblio().getConnexion(),
                    reservationDTO);

            } else if("annuler".startsWith(command)) {

                // TRANSACTION ANNULER ( <idReservation> )

                ReservationDTO reservationDTO = new ReservationDTO();
                reservationDTO.setIdReservation(readString(tokenizer));

                getGestionBiblio().getReservationFacade().annuler(getGestionBiblio().getConnexion(),
                    reservationDTO);

            } else if("listerLivres".startsWith(command)) {

                /*
                 * getGestionBiblio().getLivreDAO().getAll(getGestionBiblio().getConnexion(),
                 * LivreDTO.ID_COLUMN_NAME);
                 */

            } else if("listerLivresTitre".startsWith(command)) {

                /*
                 * getGestionBiblio().getLivreDAO().findByTitre(getGestionBiblio().getConnexion(),
                 * readString(tokenizer),
                 * LivreDTO.TITRE_COLUMN_NAME);
                 */

            }
            /*
             * else if("listerPretsEnRetard".startsWith(command)) {

                GregorianCalendar calendrier = new GregorianCalendar();
                calendrier.add(Calendar.WEEK_OF_MONTH,
                    10); // on prétend que la date actuelle est en avance de 5 semaines

                Date dateJour = calendrier.getTime(); // prendre date (avancée) du jour
                List<LivreDTO> listeRetards = getGestionBiblio().getLivreService().findPretsEnRetard(dateJour);

                if(listeRetards.isEmpty()) {
                    System.out.println("Aucun prêt n'est en retard!");
                } else {

                    for(LivreDTO livre : listeRetards) {
                        System.out.println("> Id: "
                            + livre.getIdLivre()
                            + "\n\tTitre: "
                            + livre.getTitre());
                    }
                }

            }
             * */

            else if("--".startsWith(command)) {

                // TODO empty block
            }// ne rien faire; c'est un commentaire

            /* ***********************   */
            /* TRANSACTION NON RECONNUEE */
            /* ***********************   */
            else {
                Bibliotheque.LOGGER.info("  Transactions non reconnue.  Essayer \"aide\"");
            }

            getGestionBiblio().getConnexion().commit();
            //System.out.println("TEST OUTPUT : commiting to database...");

        } catch(Exception exception) {
            try {
                getGestionBiblio().getConnexion().rollback();
                // System.err.println("TEST OUTPUT : Error! database rollback...");
            } catch(ConnexionException connexionException) {
                //connexionException.printStackTrace();
                Bibliotheque.LOGGER.error("Erreur de connexion");
                return;
            }
            throw new BibliothequeException(exception.getMessage(),
                exception);
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

    /**
     * Getter de la variable d'instance <code>gestionBiblio</code>.
     *
     * @return La variable d'instance <code>gestionBiblio</code>
     */
    private static BibliothequeCreateur getGestionBiblio() {
        return gestionBibliotheque;
    }

    /**
     * Setter de la variable d'instance <code>Bibliotheque.gestionBiblio</code>.
     *
     * @param BibliothequeCreateur La valeur à utiliser pour la variable d'instance <code>Bibliotheque.gestionBiblio</code>
     */
    private static void setGestionBiblio(BibliothequeCreateur gestionBibliotheque) {
        Bibliotheque.gestionBibliotheque = gestionBibliotheque;
    }

    /**
     *
     * Boolean de la variable lectureAuClavier
     *
     * @return <code>lectureAuClavier</code>
     */

}//class
