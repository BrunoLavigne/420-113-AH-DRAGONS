import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Permet de valider le format d'une date en YYYY-MM-DD et de la convertir en un
 * objet Date.
 *
 */
public class FormatDate {
    private static SimpleDateFormat formatAMJ;
    static {
        formatAMJ = new SimpleDateFormat("yyyy-MM-dd"); // CHECK c'est quoi cet objet?
        formatAMJ.setLenient(false); // CHECK c'est quoi ça?
    }

    /**
     * Convertit une String du format YYYY-MM-DD en un objet de la classe Date.
     */
    public static Date convertirDate(String dateString) throws ParseException {
        // Pas très fonctionnel. l'exception va passer quand la fonction est invoquée...
        return formatAMJ.parse(dateString);
    }

    public static String toString(Date date) {
        return formatAMJ.format(date);
    }
}
