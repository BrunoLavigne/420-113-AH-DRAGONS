// Fichier GestionInterrogationService.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.exception.ConnexionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 * Gestion des transactions d'interrogation dans une bibliothèque.*
 *
 * Ce programme permet de faire diverses interrogations sur l'état de la bibliothèque.
 *
 * <pre>
 * Pré-condition:
 * la base de données de la bibliothèque doit exister.
 * </pre>
 *
 * <post>
 * Post-condition:
 * Le programme effectue les majuscules associées à chaque transaction.
 * </post>
 */

public class GestionInterrogationService {

    private PreparedStatement stmtLivresTitreMot;

    private PreparedStatement stmtListeTousLivres;

    private Connexion cx;

    /**
     *
     * Creation d'une instance
     *
     * @param cx
     * @throws ServiceException
     */
    public GestionInterrogationService(Connexion cx) throws ServiceException {
        try {
            getCx().equals(cx);

            getStmtLivresTitreMot().equals(cx.getConnection().prepareStatement("select t1.idLivre, t1.titre, t1.auteur, t1.idmembre, t1.datePret + 14 "
                + "from livre t1 "
                + "where lower(titre) like ?"));

            getStmtListeTousLivres().equals(cx.getConnection().prepareStatement("select t1.idLivre, t1.titre, t1.auteur, t1.idmembre, t1.datePret "
                + "from livre t1"));
        } catch(SQLException sqlException) {
            throw new ServiceException(sqlException);
        }
    }

    /**
     *
     * Affiche les livres contenant un mot dans le titre
     *
     * @param mot
     * @throws ServiceException
     */
    public void listerLivresTitre(String mot) throws ServiceException {

        try {
            getStmtLivresTitreMot().setString(1,
                "%"
                    + mot
                    + "%");

            @SuppressWarnings("resource")
            // TODO fix warning
            ResultSet rset = getStmtLivresTitreMot().executeQuery();

            int idMembre;
            System.out.println("idLivre titre auteur idMembre dateRetour");
            while(rset.next()) {
                System.out.print(rset.getInt(1)
                    + " "
                    + rset.getString(2)
                    + " "
                    + rset.getString(3));
                idMembre = rset.getInt(4);
                if(!rset.wasNull()) {
                    System.out.print(" "
                        + idMembre
                        + " "
                        + rset.getDate(5));
                }
                System.out.println();
            }
            getCx().commit();
        } catch(ConnexionException connexionException) {
            throw new ServiceException(connexionException);
        } catch(SQLException sqlException) {
            throw new ServiceException(sqlException);
        }
    }

    /**
     *
     * Affiche tous les livres de la BD
     *
     * @throws ServiceException
     */
    @SuppressWarnings("resource")
    public void listerLivres() throws ServiceException {

        try {
            ResultSet rset;
            rset = getStmtListeTousLivres().executeQuery();

            System.out.println("idLivre titre auteur idMembre datePret");
            int idMembre;
            while(rset.next()) {
                System.out.print(rset.getInt("idLivre")
                    + " "
                    + rset.getString("titre")
                    + " "
                    + rset.getString("auteur"));
                idMembre = rset.getInt("idMembre");
                if(!rset.wasNull()) {
                    System.out.print(" "
                        + idMembre
                        + " "
                        + rset.getDate("datePret"));
                }
                System.out.println();
            }

            getCx().commit();
        } catch(ConnexionException connexionException) {
            throw new ServiceException(connexionException);
        } catch(SQLException sqlException) {
            throw new ServiceException(sqlException);
        }
    }

    /**
     * Getter de la variable d'instance <code>this.stmtLivresTitreMot</code>.
     *
     * @return La variable d'instance <code>this.stmtLivresTitreMot</code>
     */
    public PreparedStatement getStmtLivresTitreMot() {
        return this.stmtLivresTitreMot;
    }

    /**
     * Getter de la variable d'instance <code>this.stmtListeTousLivres</code>.
     *
     * @return La variable d'instance <code>this.stmtListeTousLivres</code>
     */
    public PreparedStatement getStmtListeTousLivres() {
        return this.stmtListeTousLivres;
    }

    /**
     * Getter de la variable d'instance <code>this.cx</code>.
     *
     * @return La variable d'instance <code>this.cx</code>
     */
    public Connexion getCx() {
        return this.cx;
    }

}
