// Fichier DTOException.java
// Auteur : Dragons Vicieux
// Date de création : 2015-09-21

package ca.qc.collegeahuntsic.bibliotheque.exception.dto;

/**
 * Cette exception est utilisée en cas de problème avec l'object {@link ca.qc.collegeahuntsic.bibliotheque.dto.DTO}.
 *
 * @author Dragons Vicieux
 */
public class DTOException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     *
     * Constructs a new exception with null as its detail message. The cause is not
     *  initialized, and may subsequently be initialized by a call to
     * {@link Throwable#initCause(java.lang.Throwable)}.
     *
     */
    public DTOException() {
        super();
    }

    /**
     *
     * Constructs a new exception with the specified detail message. The cause is not initialized, and may subsequently be
     * initialized by a call to {@link Throwable#initCause(java.lang.Throwable)}.
     *
     * @param message - The detail message. The detail message is saved for later retrieval by the {@link java.lang.Throwable#getMessage()} method.
     */
    public DTOException(String message) {
        super(message);
    }

    /**
     *
     * Constructs a new exception with the specified detail message and cause.
     * Note that the detail message associated with cause is not automatically incorporated in this exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link Throwable#getMessage()} method).
     * @param cause  the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public DTOException(String message,
        Throwable cause) {
        super(message,
            cause);
    }

    /**
     *
     * Constructs a new exception with the specified cause and a detail message of
     * <code>(cause==null ? null : cause.toString())</code> (which typically contains the class and
     * detail message of cause). This constructor is useful for exceptions that are
     * little more than wrappers for other throwables (for example, PrivilegedActionException).
     *
     * @param cause the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public DTOException(Throwable cause) {
        super(cause);
    }

    /**
     *
     * Constructs a new exception with the specified detail message, cause, suppression enabled or disabled, and writable stack trace enabled or disabled.
     *
     * @param message the detail message.
     * @param cause - The cause. A null value is permitted, and indicates that the cause is nonexistent or unknown
     * @param enableSuppression - Whether or not suppression is enabled or disabled
     * @param writableStackTrace - Whether or not the stack trace should be writable
     */
    protected DTOException(String message,
        Throwable cause,
        boolean enableSuppression,
        boolean writableStackTrace) {
        super(message,
            cause,
            enableSuppression,
            writableStackTrace);
    }

}
