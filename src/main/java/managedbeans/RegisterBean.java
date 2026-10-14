package managedbeans;

import beans.User;
import service.UserService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.io.Serializable;

/**
 * Backing bean for the registration page (register.xhtml): creates a new
 * "CUSTOMER" user account.
 */
@Named("registerBean")
@RequestScoped
public class RegisterBean implements Serializable {

    /* Service used for user registration and authentication */
    private final UserService userService = new UserService();

    /* Username entered in the registration form */
    private String username;
    /* Email address entered in the registration form */
    private String email;
    /* Password entered in the registration form */
    private String password;

    /* Registers a new customer account; fails if the email or username is already taken */
    public String register() {

        /* Create a user from the entered form data */
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole("CUSTOMER");

        /* Attempt to register the user */
        boolean success = userService.register(user);

        /* Continue to login if registration succeeds */
        if (success) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "ההרשמה בוצעה בהצלחה, אפשר להתחבר", null));
            return "login?faces-redirect=true";
        }
        else {
            /* Show an error if the username or email already exists */
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "שם המשתמש או כתובת האימייל כבר קיימים במערכת", null));
            return null;
        }
    }

    /* Returns the username */
    public String getUsername() {
        return username;
    }

    /* Updates the username */
    public void setUsername(String username) {
        this.username = username;
    }

    /* Returns the email address */
    public String getEmail() {
        return email;
    }

    /* Updates the email address */
    public void setEmail(String email) {
        this.email = email;
    }

    /* Returns the password */
    public String getPassword() {
        return password;
    }

    /* Updates the password */
    public void setPassword(String password) {
        this.password = password;
    }
}
