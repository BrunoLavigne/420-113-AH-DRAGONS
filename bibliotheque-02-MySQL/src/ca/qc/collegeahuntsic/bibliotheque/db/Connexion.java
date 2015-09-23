// Fichier Connexion.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ConnexionException;

/**
 * Gestionnaire d'une connexion avec une BD relationnelle via JDBC.
 *
 * Ce programme ouvrir une connexion avec une BD via JDBC.
 * La méthode serveursSupportes() indique les serveurs supportés.
 *
 * Pré-condition
 *   le driver JDBC approprié doit être accessible.
 *
 * Post-condition
 *   la connexion est ouverte en mode autocommit false et sérialisable,
 *   (s'il est supporté par le serveur).
 *
 */
public class Connexion {

    private Connection conn;

    /**
     * Ouverture d'une connexion en mode autocommit false et sérialisable (si supporté)
     * @param serveur serveur SQL de la BD
     * @param bd nom de la base de données
     * @param user userid sur le serveur SQL
     * @param pass mot de passe sur le serveur SQL
     * @throws ConnexionException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public Connexion(String serveur,
        String bd,
        String user,
        String pass) throws ConnexionException,
        InstantiationException,
        IllegalAccessException {
        Driver d;
        try {
            if(serveur.equals("local")) {
                d = (Driver) Class.forName("com.mysql.jdbc.Driver").newInstance();
                DriverManager.registerDriver(d);
                setConn(DriverManager.getConnection("jdbc:mysql://localhost:3306/"
                    + bd,
                    user,
                    pass));
            } else if(serveur.equals("distant")) {
                d = (Driver) Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                DriverManager.registerDriver(d);
                setConn(DriverManager.getConnection("jdbc:oracle:thin:@collegeahuntsic.info:1521:"
                    + bd,
                    user,
                    pass));
            } else {
                System.out.println("Erreur de driver");
            }

            // mettre en mode de commit manuel
            getConn().setAutoCommit(false);

            // mettre en mode sérialisable si possible
            // (plus haut niveau d'integrité l'accès concurrent aux données)
            DatabaseMetaData dbmd = this.conn.getMetaData();
            if(dbmd.supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE)) {
                getConn().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                System.out.println("Ouverture de la connexion en mode sérialisable :\n"
                    + "Estampille "
                    + System.currentTimeMillis()
                    + " "
                    + getConn());
            } else {
                System.out.println("Ouverture de la connexion en mode read committed (default) :\n"
                    + "Heure "
                    + System.currentTimeMillis()
                    + " "
                    + getConn());
            }
        } catch(ClassNotFoundException classNotFoundException) {
            throw new ConnexionException(classNotFoundException);
        } catch(SQLException sqlException) {
            throw new ConnexionException(sqlException);
        } catch(IllegalArgumentException illegalArgumentException) {
            throw new ConnexionException(illegalArgumentException);
        }
    }

    /**
     *
     * Fermeture d'une connexion
     *
     * @throws SQLException
     */
    public void fermer() throws SQLException {
        getConn().rollback();
        getConn().close();
        System.out.println("Connexion fermée"
            + " "
            + getConn());
    }

    /**
     *
     * Commit
     *
     * @throws SQLException
     */
    public void commit() throws SQLException {
        getConn().commit();
    }

    /**
     *
     * Rollback
     *
     * @throws SQLException
     */
    public void rollback() throws SQLException {
        getConn().rollback();
    }

    /**
     *
     * Retourne la Connection JDBC
     *
     * @return La Connection JDBC
     */
    public Connection getConnection() {
        return getConn();
    }

    /**
     * Retourne la liste des serveurs supportés par ce gestionnaire de connexions
     */
    public static String serveursSupportes() {
        return "local : MySQL installé localement\n"
            + "distant : Oracle installé au Département d'Informatique du Collège Ahuntsic\n"
            + "postgres : Postgres installé localement\n"
            + "access : Microsoft Access installé localement et inscrit dans ODBC";
    }

    private Connection getConn() {
        return this.conn;
    }

    private void setConn(Connection conn) {
        this.conn = conn;
    }

}// Classe Connexion
