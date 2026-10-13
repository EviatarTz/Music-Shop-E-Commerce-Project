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

    private final UserService userService = new UserService();

    private String username;
    private String password;

    /* Authenticates the user and, on success, stores them in the session and navigates to products */
    public String login() {
        User user = userService.login(username, password);

        if (user != null) {
            // Stored under the same HttpSession attribute "user" that the existing
            // filters and servlets (AuthFilter, AdminFilter, CartServlet, etc.) already
            // read from - this keeps the old (JSP/Servlet) and new (JSF) worlds
            // compatible during the migration.
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap()
                    .put("user", user);

            return "products?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid username or password", null));
            return null; // Stay on the same page
        }
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
