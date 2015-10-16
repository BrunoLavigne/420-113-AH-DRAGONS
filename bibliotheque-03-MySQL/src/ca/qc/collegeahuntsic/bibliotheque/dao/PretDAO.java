
package ca.qc.collegeahuntsic.bibliotheque.dao;

import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;

public class PretDAO extends DAO {

    private static final long serialVersionUID = 1L;

    /**
     *
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion - La connexion à utiliser
     */
    public PretDAO(Connexion connexion) {
        super(connexion);
    }
}
