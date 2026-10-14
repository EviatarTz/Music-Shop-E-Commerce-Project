package managedbeans;

import beans.User;
import service.UserService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.io.Serializable;

/**
 * Backing bean for the login page (login.xhtml).
 */
@Named("loginBean")
@RequestScoped
public class LoginBean implements Serializable {

    /* Service used for user authentication */
    private final UserService userService = new UserService();

    /* Username entered in the login form */
    private String username;

    /* Password entered in the login form */
    private String password;

    /* Authenticates the user and, on success, stores them in the session and navigates to products */
    public String login() {
        /* Verify the supplied credentials */
        User user = userService.login(username, password);

        /* Continue if authentication succeeds */
        if (user != null) {

            /* Stored under the same HttpSession attribute "user" that the existing
            * filters and servlets (AuthFilter, AdminFilter, CartServlet, etc.) already
            * read from - this keeps the old (JSP/Servlet) and new (JSF) worlds
             compatible during the migration */
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap()
                    .put("user", user);
            /* Redirect to the products page */
            return "products?faces-redirect=true";
        }
        else {
            /* Display a login error message */
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid username or password", null));
            /* Stay on the login page */
            return null;
        }
    }

    /* Returns the entered username */
    public String getUsername() {
        return username;
    }
    /* Updates the username */
    public void setUsername(String username) {
        this.username = username;
    }
    /* Returns the entered password */
    public String getPassword() {
        return password;
    }
    /* Updates the password */
    public void setPassword(String password) {
        this.password = password;
    }
}
