// Fichier BDCreateur.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.util;

import java.sql.SQLException;
import java.sql.Statement;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.exception.BDCreateurException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ConnexionException;

/**
 *
 * Utilitaire de création de la base de données.
 *
 * @author Dragons Vicieux
 */
class BDCreateur {

    /**
     * Crée la base de données nécessaire à l'application bibliothèque.
     *
     * Paramètres:
     * <ul>
     *  <li>0- serveur SQL</li>
     *  <li>1- bd nom de la BD</li>
     *  <li>2- user id pour établir une connexion avec le serveur SQL</li>
     *  <li>3- mot de passe pour le user id</li>
     * </ul>
     *
     * @throws BDCreateurException S'il y a une erreur avec la connexion ou s'il y a une erreur avec la base de données.
     */
    public static void main(String args[]) throws BDCreateurException {

        if(args.length < 3) {
            System.out.println("Usage: java CreerBD <serveur> <bd> <user> <password>");
            return;
        }

        try(
            Connexion connexion = new Connexion(args[0],
                args[1],
                args[2],
                args[3])) {

            try(
                Statement stmt = connexion.getConnection().createStatement()) {

                stmt.executeUpdate("DROP TABLE IF EXISTS reservation CASCADE");
                stmt.executeUpdate("DROP TABLE IF EXISTS livre CASCADE");
                stmt.executeUpdate("DROP TABLE IF EXISTS membre CASCADE");

                stmt.executeUpdate("CREATE TABLE membre ( "
                    + "idMembre        NUMBER(3)   check(idMembre > 0), "
                    + "nom             VARCHAR(10)  NOT NULL, "
                    + "telephone       NUMBER(32) , "
                    + "limitePret      NUMBER(2)   check(limitePret > 0 and limitePret <= 10) , "
                    + "nbpret          NUMBER(2)   default 0 check(nbpret >= 0) , "
                    + "CONSTRAINT      cleMembre    PRIMARY KEY (idMembre), "
                    + "CONSTRAINT      limiteNbPret check(nbpret <= limitePret) "
                    + ")");

                stmt.executeUpdate("CREATE TABLE livre ( "
                    + "idLivre         NUMBER(3)   check(idLivre > 0) , "
                    + "titre           VARCHAR(10)  NOT NULL, "
                    + "auteur          VARCHAR(10)  NOT NULL, "
                    + "dateAcquisition date         NOT NULL, "
                    + "idMembre        NUMBER(3) , "
                    + "datePret        date , "
                    + "CONSTRAINT      cleLivre         PRIMARY KEY (idLivre), "
                    + "CONSTRAINT      refPretMembre    FOREIGN KEY (idMembre) REFERENCES membre (idMembre) "
                    + ")");

                stmt.executeUpdate("CREATE TABLE reservation ( "
                    + "idReservation    NUMBER(3) , "
                    + "idMembre         NUMBER(3) , "
                    + "idLivre          NUMBER(3) , "
                    + "dateReservation  DATE , "
                    + "CONSTRAINT       cleReservation          PRIMARY KEY (idReservation) , "
                    + "CONSTRAINT       cleCandidateReservation UNIQUE (idMembre,idLivre) , "
                    + "CONSTRAINT       refReservationMembre    FOREIGN KEY (idMembre)  REFERENCES membre (idMembre) "
                    + "  ON DELETE CASCADE , "
                    + "CONSTRAINT       refReservationLivre     FOREIGN KEY (idLivre)   REFERENCES livre (idLivre) "
                    + "  ON DELETE CASCADE "
                    + ")");

                stmt.close();
                connexion.fermer();
            }

        } catch(SQLException sqlException) {
            throw new BDCreateurException(sqlException);
        } catch(ConnexionException connexionException) {
            throw new BDCreateurException(connexionException);
        } catch(Exception exception) {
            throw new BDCreateurException(exception);
        }

    }
}
