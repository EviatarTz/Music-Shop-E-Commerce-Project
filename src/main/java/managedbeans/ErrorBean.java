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

    public String getMessage() {
        Object exceptionAttr = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestMap()
                .get("jakarta.servlet.error.exception");

        if (exceptionAttr instanceof BusinessException businessException) {
            // Business errors are safe to show to the user as-is
            return businessException.getMessage();
        } else if (exceptionAttr instanceof Throwable) {
            // System/unexpected errors - don't expose technical details
            return "An unexpected error occurred. Please try again later.";
        } else {
            return "Page not found.";
        }
    }
}
