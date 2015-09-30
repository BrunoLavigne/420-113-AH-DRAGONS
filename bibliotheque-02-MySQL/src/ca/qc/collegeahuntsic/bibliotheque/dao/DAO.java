// Fichier DAO.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.io.Serializable;
import java.sql.Connection;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;

/**
 * TODO Auto-generated class javadoc
 *
 * @author Dragons Vicieux
 */
public class DAO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Connexion connexion;

    /**
     * Getter de la variable d'instance <code>this.connection</code>.
     *
     * @return La variable d'instance <code>this.connection</code>
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
     * Retourne la {@link java connexion}}
     *
     * @return La connexion java
     */
    protected Connection getConnection() {
        return getConnexion().getConnection();
    }

}
