package managedbeans;

import exceptions.BusinessException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.io.Serializable;

@Named("errorBean")
@RequestScoped
public class ErrorBean implements Serializable {

    public String getMessage() {
        Object exceptionAttr = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestMap()
                .get("jakarta.servlet.error.exception");

        if (exceptionAttr instanceof BusinessException businessException) {
            // שגיאות עסקיות בטוח להציג כמו שהן למשתמש
            return businessException.getMessage();
        } else if (exceptionAttr instanceof Throwable) {
            // שגיאות מערכת/לא צפויות - לא חושפים פרטים טכניים
            return "An unexpected error occurred. Please try again later.";
        } else {
            return "Page not found.";
        }
    }
}