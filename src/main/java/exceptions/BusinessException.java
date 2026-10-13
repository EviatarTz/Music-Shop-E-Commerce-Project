package exceptions;

/**
 * A business-rule error that is safe to show to the user as-is
 * (e.g. invalid input, a rule violation) - as opposed to SystemException,
 * which represents an unexpected/technical failure.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}