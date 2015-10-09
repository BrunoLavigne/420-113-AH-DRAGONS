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
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.BibliothequeException;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;
import ca.qc.collegeahuntsic.bibliotheque.util.BibliothequeCreateur;
import ca.qc.collegeahuntsic.bibliotheque.util.FormatteurDate;

/**
 *
 * Interface du système de gestion d'une bibliothèque
 *
 *
 * Ce programme permet d'appeler les transactions de base d'une
 * bibliothèque.  Il gère des livres, des membres et des
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

    /**
     *
     * Ouverture de la BD, traitement des transactions et fermeture de la BD.
     *
     * @throws BibliothequeException En cas d'erreur lors de la gestion de la bibliothèque
     */
    public static void main(String argv[]) throws BibliothequeException {
        // validation du nombre de paramètres
        if(argv.length < 5) {
            System.out.println("Usage: java Biblio <serveur> <bd> <user> <password> <fichier-transactions>");
            System.out.println(Connexion.getServeursSupportes());
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
            // TODO CHECK & TEST runtime error. Erreur présente au préalable qui n'était pas traitée???
            // throw new BibliothequeException(exception);
        } finally {
            getGestionBiblio().fermer();
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
                executerTransaction(tokenizer);
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
            transaction = lireTransaction(reader);
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
            System.out.print("> ");
            String transaction = reader.readLine();

            if(transaction != null) {
                System.out.println(transaction);
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

                LivreDTO newLivre = new LivreDTO();

                newLivre.setIdLivre(readInt(tokenizer));
                newLivre.setTitre(readString(tokenizer));
                newLivre.setAuteur(readString(tokenizer));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");

                Timestamp date;

                try {
                    date = new Timestamp(format.parse(readDate(tokenizer)).getTime());
                } catch(ParseException exception) {
                    throw new BibliothequeException("Erreur de parsing dans le format de date lors de la création d'un objet livre.");
                }

                newLivre.setDateAcquisition(date);
                getGestionBiblio().getLivreService().acquerir(newLivre);

            } else if("vendre".startsWith(command)) {

                getGestionBiblio().getLivreService().vendre(getGestionBiblio().getLivreService().read(readInt(tokenizer)));

            } else if("preter".startsWith(command)) {

                LivreDTO livreDTO = getGestionBiblio().getLivreService().read(readInt(tokenizer));
                MembreDTO membreDTO = getGestionBiblio().getMembreService().read(readInt(tokenizer));

                if(livreDTO == null
                    || membreDTO == null) {
                    return;
                }

                SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
                Timestamp dateEmprunt;

                try {
                    dateEmprunt = new Timestamp(format.parse(readDate(tokenizer)).getTime());
                } catch(ParseException exception) {
                    throw new BibliothequeException("Erreur de parsing dans le format de date lors de la création d'un objet livre.");
                }

                livreDTO.setDatePret(dateEmprunt);

                getGestionBiblio().getMembreService().emprunter(membreDTO,
                    livreDTO);

            } else if("renouveler".startsWith(command)) {

                getGestionBiblio().getPretService().renouveler(readInt(tokenizer) /* idLivre */,
                    readDate(tokenizer) /* dateRenouvellement */);

            } else if("retourner".startsWith(command)) {

                /*
                LivreDTO livreDTO = getGestionBiblio().getLivreService().read(readInt(tokenizer));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
                Date dateRetour;
                try {
                    dateRetour = new Date(format.parse(readDate(tokenizer)).getTime());
                } catch(ParseException exception) {
                    // TODO TES TEST TEST TEST
                    exception.printStackTrace();
                    throw new BibliothequeException("Erreur de parsing dans le format de date lors de son retour.");
                }
                livreDTO.setDatePret(null);
                getGestionBiblio().getLivreService().retourner(livreDTO);
                 */
                getGestionBiblio().getPretService().retourner(readInt(tokenizer) /* idLivre */,
                    readDate(tokenizer) /* dateRetour */);

            } else if("inscrire".startsWith(command)) {

                MembreDTO membreDTO = new MembreDTO();
                membreDTO.setIdMembre(readInt(tokenizer));
                membreDTO.setNom(readString(tokenizer));
                membreDTO.setTelephone(readLong(tokenizer));
                membreDTO.setLimitePret(readInt(tokenizer));
                getGestionBiblio().getMembreService().inscrire(membreDTO);

            } else if("desinscrire".startsWith(command)) {

                MembreDTO membreDTO = new MembreDTO();
                membreDTO = getGestionBiblio().getMembreService().read(readInt(tokenizer));

                getGestionBiblio().getMembreService().desinscrire(membreDTO);

            } else if("reserver".startsWith(command)) {

                ReservationDTO reservationDTO = new ReservationDTO();
                reservationDTO.setIdReservation(readInt(tokenizer));
                reservationDTO.setIdLivre(readInt(tokenizer));
                reservationDTO.setIdMembre(readInt(tokenizer));
                String dateReservation = readDate(tokenizer);

                SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
                Timestamp date;

                try {
                    date = new Timestamp(format.parse(dateReservation).getTime());
                } catch(ParseException exception) {
                    throw new BibliothequeException("Erreur de parsing dans le format de date lors de la création d'un objet livre.");
                }

                reservationDTO.setDateReservation(date);

                LivreDTO livreDTO = getGestionBiblio().getLivreDAO().read(reservationDTO.getIdLivre());
                MembreDTO membreDTO = getGestionBiblio().getMembreService().read(reservationDTO.getIdMembre());
                if(livreDTO == null
                    || membreDTO == null) {
                    return;
                }

                getGestionBiblio().getReservationService().reserver(reservationDTO,
                    livreDTO,
                    membreDTO,
                    dateReservation);

            } else if("prendreRes".startsWith(command)) {

                ReservationDTO reservationDTO = new ReservationDTO();
                reservationDTO = getGestionBiblio().getReservationService().read(readInt(tokenizer));
                //String dateReservation = readDate(tokenizer);

                LivreDTO livreDTO = getGestionBiblio().getLivreService().read(reservationDTO.getIdLivre());
                MembreDTO membreDTO = getGestionBiblio().getMembreService().read(reservationDTO.getIdMembre());

                getGestionBiblio().getReservationService().utiliser(reservationDTO,
                    membreDTO,
                    livreDTO);

            } else if("annulerRes".startsWith(command)) {

                ReservationDTO reservationDTO = new ReservationDTO();
                reservationDTO = getGestionBiblio().getReservationService().read(readInt(tokenizer));
                getGestionBiblio().getReservationService().annuler(reservationDTO);

            } else if("listerLivres".startsWith(command)) {

                getGestionBiblio().getLivreDAO().getAll();

            } else if("listerLivresTitre".startsWith(command)) {

                getGestionBiblio().getLivreDAO().findByTitre(readString(tokenizer) /* mot */);

            } else if("--".startsWith(command)) {

                // TODO empty block
            }// ne rien faire; c'est un commentaire

            /* ***********************   */
            /* TRANSACTION NON RECONNUEE */
            /* ***********************   */

            else {
                System.out.println("  Transactions non reconnue.  Essayer \"aide\"");
            }

        } catch(
            ServiceException
            | DAOException exception) {
            System.err.println("Error in "
                + exception.getClass()
                + " caused by "
                + exception.getCause());
            exception.printStackTrace();
            throw new BibliothequeException(exception);
        }
    }

    /**
     *
     * Affiche le menu des transactions acceptées par le système
     *
     */
    static void afficherAide() {
        System.out.println();
        System.out.println("Chaque transaction comporte un nom et une liste d'arguments");
        System.out.println("separes par des espaces. La liste peut etre vide.");
        System.out.println(" Les dates sont en format yyyy-mm-dd.");
        System.out.println("");
        System.out.println("Les transactions sont:");
        System.out.println("  aide");
        System.out.println("  exit");
        System.out.println("  acquerir <idLivre> <titre> <auteur> <dateAcquisition>");
        System.out.println("  preter <idLivre> <idMembre> <dateEmprunt>");
        System.out.println("  renouveler <idLivre> <dateRenouvellement>");
        System.out.println("  retourner <idLivre> <dateRetour>");
        System.out.println("  vendre <idLivre>");
        System.out.println("  inscrire <idMembre> <nom> <telephone> <limitePret>");
        System.out.println("  desinscrire <idMembre>");
        System.out.println("  reserver <idReservation> <idLivre> <idMembre> <dateReservation>");
        System.out.println("  prendreRes <idReservation> <dateEmprunt>");
        System.out.println("  annulerRes <idReservation>");
        System.out.println("  listerLivresRetard <dateCourante>");
        System.out.println("  listerLivresTitre <mot>");
        System.out.println("  listerLivres");
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
