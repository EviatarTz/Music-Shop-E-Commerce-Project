package exceptions;

/**
 * An unexpected/technical failure (e.g. a database or I/O error) whose details
 * should NOT be shown to the user as-is - as opposed to BusinessException,
 * which represents a safe-to-display business-rule error.
 */
public class SystemException extends RuntimeException {

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
}