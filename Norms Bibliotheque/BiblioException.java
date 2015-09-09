
/**
 * L'exception BiblioException est lev�e lorsqu'une transaction est inad�quate.
 * Par exemple -- livre inexistant
 */

public final class BiblioException extends Exception
// TODO Refaire au complet la gestion des erreurs. Raiser plusieurs erreurs avec informations spécialisées.
{
public BiblioException(String message)
{
super(message);
}
}