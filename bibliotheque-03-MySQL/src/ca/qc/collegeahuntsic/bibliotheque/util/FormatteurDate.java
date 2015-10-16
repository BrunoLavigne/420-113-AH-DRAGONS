// Fichier FormatteurDate.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Permet de valider le format d'une date en YYYY-MM-DD et de la convertir en un objet Date.
 *
 * @author Dragons Vicieux
 *
 */
public class FormatteurDate {

    private static final String FORMAT_DATE = "yyyy-MM-dd";

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(FormatteurDate.FORMAT_DATE);

    static {
        FormatteurDate.SIMPLE_DATE_FORMAT.setLenient(false);
    }

    /**
     *
     * Convertit une String du format YYYY-MM-DD en un objet de la classe Date.
     *
     * @param dateString La chaîne de caractères
     * @return Date La date convertie
     * @throws ParseException Si la chaîne de caractères n'est pas formatée correctement
     */
    public static Date convertirDate(String dateString) throws ParseException {
        return FormatteurDate.SIMPLE_DATE_FORMAT.parse(dateString);
    }

    /**
     *
     * Convertit un Timestamp en une chaîne de caractères selon le format yyyy-MM-dd.
     *
     * @param timestamp Le Timestamp
     * @return La chaîne de caractères issue de la conversion
     * @throws ParseException Si le Timestamp n'est pas formaté correctement
     */
    public static String stringValue(Timestamp timestamp) throws ParseException {
        // TODO to be added
        return null;
    }

}
