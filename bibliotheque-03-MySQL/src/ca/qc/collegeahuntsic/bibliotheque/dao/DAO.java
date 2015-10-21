// Fichier DAO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.io.Serializable;
import java.sql.Connection;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;

/**
 * Classe de base pour tous les DAOs.
 *
 * @author Dragons Vicieux
 */
public class DAO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Connexion connexion;

    /**
     *
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion La connexion à utiliser
     */
    public DAO(Connexion connexion) {
        super();
        setConnexion(connexion);
    }

    /**
     * Getter de la variable d'instance <code>this.connexion</code>.
     *
     * @return La variable d'instance <code>this.connexion</code>
     */
    private Connexion getConnexion() {
        return this.connexion;
    }

    /**
     * Setter de la variable d'instance <code>this.connexion</code>.
     *
     * @param connexion La valeur à utiliser pour la variable d'instance <code>this.connexion</code>
     */
    private void setConnexion(Connexion connexion) {
        this.connexion = connexion;
    }

    /**
     *
     * Retourne la connexion à la base de données
     *
     * @return La connexion à la base de données
     */
    protected Connection getConnection() {
        return getConnexion().getConnection();
    }
    
    /**
     * 
     * TODO Auto-generated method javadoc
     *
     * @return
     * @throws DAOException
     */
    protected int getPrimaryKey() throws DAOException  {
        
        // PreparedStatement createPreparedStatement = getConnection().prepareStatement(sql)
    }

}
