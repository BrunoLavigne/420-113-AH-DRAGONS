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
     * <li>0- serveur SQL</li>
     * <li>1- bd nom de la BD</li>
     * <li>2- user id pour établir une connexion avec le serveur SQL</li>
     * <li>3- mot de passe pour le user id</li>
     * </ul>
     *
     * @throws BDCreateurException
     *             S'il y a une erreur avec la connexion ou s'il y a une erreur
     *             avec la base de données.
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

                stmt.executeUpdate("DROP    SEQUENCE SEQ_ID_RESERVATION");
                stmt.executeUpdate("DROP    SEQUENCE SEQ_ID_PRET");
                stmt.executeUpdate("DROP    SEQUENCE SEQ_ID_LIVRE");
                stmt.executeUpdate("DROP    SEQUENCE SEQ_ID_MEMBRE");

                stmt.executeUpdate("DROP TABLE membre CASCADE CONSTRAINTS PURGE");
                stmt.executeUpdate("DROP TABLE pret CASCADE CONSTRAINTS PURGE");
                stmt.executeUpdate("DROP TABLE livre CASCADE CONSTRAINTS PURGE");
                stmt.executeUpdate("DROP TABLE reservation CASCADE CONSTRAINTS PURGE");

                stmt.executeUpdate("CREATE  SEQUENCE SEQ_ID_MEMBRE START WITH 1 INCREMENT BY 1");
                stmt.executeUpdate("CREATE  SEQUENCE SEQ_ID_LIVRE START WITH 1 INCREMENT BY 1");
                stmt.executeUpdate("CREATE  SEQUENCE SEQ_ID_PRET START WITH 1 INCREMENT BY 1");
                stmt.executeUpdate("CREATE  SEQUENCE SEQ_ID_RESERVATION START WITH 1 INCREMENT BY 1");

                stmt.executeUpdate("CREATE TABLE membre "
                    + "("
                    + "idMembre NUMBER(3) CHECK(idMembre > 0),"
                    + "nom VARCHAR(100) NOT NULL,telephone NUMBER(10),"
                    + "limitePret NUMBER(2) CHECK(limitePret > 0 AND limitePret <= 10),"
                    + "nbpret NUMBER(2) DEFAULT 0 CHECK(nbpret >= 0),"
                    + "CONSTRAINT cleMembre PRIMARY KEY (idMembre), "
                    + "CONSTRAINT limiteNbPret check(nbpret <= limitePret)"
                    + ")");

                stmt.executeUpdate("CREATE TABLE livre "
                    + "("
                    + "idLivre NUMBER(3) CHECK(idLivre > 0), "
                    + "titre VARCHAR(100) NOT NULL, "
                    + "auteur VARCHAR(100) NOT NULL, "
                    + "dateAcquisition TIMESTAMP NOT NULL, "
                    + "CONSTRAINT cleLivre PRIMARY KEY (idLivre)"
                    + ")");

                stmt.executeUpdate("CREATE TABLE pret "
                    + "("
                    + "idPret NUMBER(3) CHECK(idPret > 0),"
                    + "idMembre NUMBER(3) CHECK(idMembre > 0),"
                    + "idLivre NUMBER(3) CHECK(idLivre > 0),"
                    + "datePret TIMESTAMP(3),dateRetour TIMESTAMP(3),"
                    + "CONSTRAINT clePrimairePret PRIMARY KEY (idPret),"
                    + "CONSTRAINT refPretMembre FOREIGN KEY (idMembre) REFERENCES membre(idMembre),"
                    + "CONSTRAINT refPretLivre FOREIGN KEY (idLivre) REFERENCES livre(idLivre)"
                    + ")");

                stmt.executeUpdate("CREATE TABLE reservation "
                    + "("
                    + "idReservation NUMBER(3) CHECK(idReservation > 0), "
                    + "idMembre NUMBER(3) CHECK(idMembre > 0), "
                    + "idLivre NUMBER(3) CHECK(idLivre > 0), "
                    + "dateReservation TIMESTAMP(3), "
                    + "CONSTRAINT cleReservation PRIMARY KEY (idReservation), "
                    + "CONSTRAINT cleCandidateReservation UNIQUE (idMembre,idLivre), "
                    + "CONSTRAINT refReservationMembre FOREIGN KEY (idMembre) REFERENCES membre(idMembre) ON DELETE CASCADE, "
                    + "CONSTRAINT refReservationLivre FOREIGN KEY (idLivre) REFERENCES livre(idLivre) ON DELETE CASCADE "
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
