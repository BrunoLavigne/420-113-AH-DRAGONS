// Fichier FormatteurDate.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-18

package ca.qc.collegeahuntsic.bibliotheque.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Permet de valider le format d'une date en YYYY-MM-DD et de la convertir en un objet Date.
 *
 */
public class FormatteurDate {

    private static SimpleDateFormat formatAMJ;

    static {
        setFormatAMJ(new SimpleDateFormat("yyyy-MM-dd"));
        getFormatAMJ().setLenient(false);
    }

    /**
     *
     * Convertit une String du format YYYY-MM-DD en un objet de la classe Date.
     *
     * @param dateString
     * @return Date convertirDate
     * @throws ParseException
     */
    public static Date convertirDate(String dateString) throws ParseException {
        return getFormatAMJ().parse(dateString);
    }

    /**
     *
     * ToString de la date
     *
     * @param date
     * @return La date en string
     */
    public static String toString(Date date) {
        return getFormatAMJ().format(date);
    }

    public static SimpleDateFormat getFormatAMJ() {
        return formatAMJ;
    }

    public static void setFormatAMJ(SimpleDateFormat formatAMJ) {
        FormatteurDate.formatAMJ = formatAMJ;
    }

}
