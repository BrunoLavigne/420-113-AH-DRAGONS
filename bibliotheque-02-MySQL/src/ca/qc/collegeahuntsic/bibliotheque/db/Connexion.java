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

    private final static String CONNEXIONMYSQL = "local";

    private final static String CONNEXIONORACLE = "distant";

    private final static String NOM_DRIVER_MYSQL = "com.mysql.jdbc.Driver";

    private final static String NOM_DRIVER_ORACLE = "oracle.jdbc.driver.OracleDriver";

    private final static String URL_MYSQL = "jdbc:mysql://localhost:3306/";

    private final static String URL_ORACLE = "jdbc:oracle:thin:@collegeahuntsic.info:1521:";

    /**
     * Ouverture d'une connexion en mode autocommit false et sérialisable (si supporté)
     * @param serveur serveur SQL de la BD
     * @param bd nom de la base de données
     * @param user userid sur le serveur SQL
     * @param pass mot de passe sur le serveur SQL
     * @throws ConnexionException
     */
    public Connexion(String serveur,
        String bd,
        String user,
        String pass) throws ConnexionException {
        Driver d;
        try {
            if(serveur.equals(CONNEXIONMYSQL)) {
                d = (Driver) Class.forName(NOM_DRIVER_MYSQL).newInstance();
                DriverManager.registerDriver(d);
                setConn(DriverManager.getConnection(URL_MYSQL
                    + bd,
                    user,
                    pass));
            } else if(serveur.equals(CONNEXIONORACLE)) {
                d = (Driver) Class.forName(NOM_DRIVER_ORACLE).newInstance();
                DriverManager.registerDriver(d);
                setConn(DriverManager.getConnection(URL_ORACLE
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
        } catch(IllegalAccessException illegalAccessException) {
            throw new ConnexionException(illegalAccessException);
        } catch(InstantiationException instantiationException) {
            throw new ConnexionException(instantiationException);
        }
    }

    /**
     *
     * Fermeture d'une connexion
     *
     * @throws ConnexionException
     */
    public void fermer() throws ConnexionException {

        try {
            getConn().rollback();
            getConn().close();
            System.out.println("Connexion fermée"
                + " "
                + getConn());
        } catch(SQLException sqlException) {
            throw new ConnexionException(sqlException);
        }
    }

    /**
     *
     * Commit
     *
     * @throws ConnexionException
     */
    public void commit() throws ConnexionException {

        try {
            getConn().commit();
        } catch(SQLException sqlException) {
            throw new ConnexionException(sqlException);
        }
    }

    /**
     *
     * Rollback
     *
     * @throws ConnexionException
     */
    public void rollback() throws ConnexionException {

        try {
            getConn().rollback();
        } catch(SQLException sqlException) {
            throw new ConnexionException(sqlException);
        }
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
