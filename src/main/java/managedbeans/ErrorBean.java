package managedbeans;

import exceptions.BusinessException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.io.Serializable;

/**
 * Backing bean for the custom error page: translates the exception that
 * triggered the error page into a user-facing message.
 */
@Named("errorBean")
@RequestScoped
public class ErrorBean implements Serializable {

    /* Returns an error message for the current request */
    public String getMessage() {

        /* Get the request exception, if any */
        Object exceptionAttr = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestMap()
                .get("jakarta.servlet.error.exception");

        /* Show business error messages directly */
        if (exceptionAttr instanceof BusinessException businessException) {
            return businessException.getMessage();
        }
        /* Hide technical details from users */
        else if (exceptionAttr instanceof Throwable) {
            return "An unexpected error occurred. Please try again later.";
        } else {
            /* Default message when no exception exists */
            return "Page not found.";
        }
    }
}
