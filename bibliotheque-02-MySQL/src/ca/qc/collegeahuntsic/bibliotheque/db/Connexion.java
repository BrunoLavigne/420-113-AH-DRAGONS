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
 * Cette classe encapsule une connexion JDBC en fonction d'un type et d'une instance de base de données.
 *
 * La méthode serveursSupportes() indique les types serveurs supportés.
 *
 * Pré-condition: le driver JDBC approprié doit être accessible.
 *
 * Post-condition: la connexion est ouverte en mode autocommit false.
 *
 * @author Dragons Vicieux
 *
 */

public class Connexion extends Object implements AutoCloseable {

    private Connection connection;

    private final static String CONNEXION_MYSQL = "local";

    private final static String CONNEXION_ORACLE = "distant";

    private final static String NOM_DRIVER_MYSQL = "com.mysql.jdbc.Driver";

    private final static String NOM_DRIVER_ORACLE = "oracle.jdbc.driver.OracleDriver";

    private final static String URL_MYSQL = "jdbc:mysql://localhost:3306/";

    private final static String URL_ORACLE = "jdbc:oracle:thin:@collegeahuntsic.info:1521:";

    /**
     * Crée une connexion en mode autocommit false.
     * @param typeServeur Type de serveur SQL de la base de données
     * @param schema Nom du schéma de la base de données
     * @param nomUtilisateur Nom d'utilisateur sur le serveur SQL
     * @param motPasse Mot de passe sur le serveur SQL
     * @throws ConnexionException Si le driver n'existe pas, s'il y a une erreur avec la base de données ou si typeServeur n'est pas valide.
     */
    public Connexion(String typeServeur,
        String schema,
        String nomUtilisateur,
        String motPasse) throws ConnexionException {
        Driver driver;
        try {
            if(typeServeur.equals(CONNEXION_MYSQL)) {
                driver = (Driver) Class.forName(NOM_DRIVER_MYSQL).newInstance();
                DriverManager.registerDriver(driver);
                setConnection(DriverManager.getConnection(URL_MYSQL
                    + schema,
                    nomUtilisateur,
                    motPasse));
            } else if(typeServeur.equals(CONNEXION_ORACLE)) {
                driver = (Driver) Class.forName(NOM_DRIVER_ORACLE).newInstance();
                DriverManager.registerDriver(driver);
                setConnection(DriverManager.getConnection(URL_ORACLE
                    + schema,
                    nomUtilisateur,
                    motPasse));
            } else {
                System.out.println("Erreur de driver");
            }

            // mettre en mode de commit manuel
            getConnection().setAutoCommit(false);

            // mettre en mode sérialisable si possible
            // (plus haut niveau d'integrité l'accès concurrent aux données)
            DatabaseMetaData dbmd = getConnection().getMetaData();
            if(dbmd.supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE)) {
                getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                System.out.println("Ouverture de la connexion en mode sérialisable :\n"
                    + "Estampille "
                    + System.currentTimeMillis()
                    + " "
                    + getConnection());
            } else {
                System.out.println("Ouverture de la connexion en mode read committed (default) :\n"
                    + "Heure "
                    + System.currentTimeMillis()
                    + " "
                    + getConnection());
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
     * @throws ConnexionException {@link java.sql.SQLException} S'il y a une erreur avec la base de données.
     */
    public void fermer() throws ConnexionException {

        try {
            getConnection().rollback();
            getConnection().close();
            System.out.println("Connexion fermée"
                + " "
                + getConnection());
        } catch(SQLException sqlException) {
            throw new ConnexionException(sqlException);
        }
    }

    /**
     * Effectue un commit sur la Connection JDBC
     *
     * @throws ConnexionException {@link java.sql.SQLException} S'il y a une erreur avec la base de données.
     */
    public void commit() throws ConnexionException {

        try {
            getConnection().commit();
        } catch(SQLException sqlException) {
            throw new ConnexionException(sqlException);
        }
    }

    /**
     * Effectue un rollback sur la Connection JDBC
     *
     * @throws ConnexionException {@link java.sql.SQLException} S'il y a une erreur avec la base de données.
     */
    public void rollback() throws ConnexionException {

        try {
            getConnection().rollback();
        } catch(SQLException sqlException) {
            throw new ConnexionException(sqlException);
        }
    }

    /**
     * Retourne la liste des serveurs supportés par ce gestionnaire de connexion:
     * local: MySQL installé localement
     * distant: Oracle installé au Département d'informatique du Collège Ahuntsic
     * postgres: Postgres installé localement
     * access: Microsoft Access installé localement et inscrit dans ODBC
     *
     * @return la liste des serveurs supportés par ce gestionnaire de connexion
     */
    public static String getServeursSupportes() {
        return "local : MySQL installé localement\n"
            + "distant : Oracle installé au Département d'Informatique du Collège Ahuntsic\n"
            + "postgres : Postgres installé localement\n"
            + "access : Microsoft Access installé localement et inscrit dans ODBC";
    }

    /**
     * Getter de la variable d'instance <code>this.connection</code>.
     *
     * @return La variable d'instance <code>this.connection</code>
     */
    public Connection getConnection() {
        return this.connection;
    }

    /**
     * Setter de la variable d'instance <code>this.connection</code>.
     *
     * @param connection La valeur à utiliser pour la variable d'instance <code>this.connection</code>
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /* (non-Javadoc)
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() throws Exception {
        // TODO Auto-generated method stub

    }

}// Classe Connexion
