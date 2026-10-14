package managedbeans;

import beans.User;
import service.UserService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.Serializable;

/**
 * Backing bean for the profile page (profile.xhtml): lets the logged-in
 * user update their username/email, and separately change their password.
 */
@Named("profileBean")
@RequestScoped
public class ProfileBean implements Serializable {

    /* Service used for profile management */
    private final UserService userService = new UserService();

    /* Current username */
    private String username;

    /* Current email address */
    private String email;

    /* User's current password */
    private String currentPassword;

    /* New password entered by the user */
    private String newPassword;

    /* Confirmation of the new password */
    private String confirmPassword;

    /* Redirects to login if no user is logged in, otherwise loads their current details into the form */
    public void checkLoginAndLoad() {

        /* Get the logged-in user */
        User user = getLoggedInUser();

        /* Redirect guests to the login page */
        if (user == null) {
            redirect("login.xhtml");
            return;
        }

        /* Populate the form fields */
        username = user.getUsername();
        email = user.getEmail();
    }

    /* Saves the updated username/email (password is kept unchanged) */
    public void updateDetails() {

        /* Get the logged-in user */
        User user = getLoggedInUser();

        /* Redirect guests to the login page */
        if (user == null) {
            redirect("login.xhtml");
            return;
        }

        /* Create an updated user object */
        User updated = new User();
        updated.setUserId(user.getUserId());
        updated.setUsername(username);
        updated.setEmail(email);
        updated.setPassword(user.getPassword());
        updated.setRole(user.getRole());

        /* Save the updated profile */
        boolean success = userService.updateProfile(updated);

        if (success) {
            /* Refresh the user stored in the session */
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap()
                    .put("user", updated);

            /* Show a success message */
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "הפרטים עודכנו בהצלחה", null));
        } else {
            /* Show an error if the email is already in use */
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "כתובת האימייל כבר בשימוש על ידי משתמש אחר", null));
        }
    }

    /* Validates the current password and the new/confirm pair, then saves the new password */
    public void changePassword() {

        /* Get the logged-in user */
        User user = getLoggedInUser();

        /* Redirect guests to the login page */
        if (user == null) {
            redirect("login.xhtml");
            return;
        }

        /* Verify the current password */
        if (currentPassword == null || !currentPassword.equals(user.getPassword())) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "הסיסמה הנוכחית שגויה", null));
            return;
        }

        /* Require a new password */
        if (newPassword == null || newPassword.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "יש להזין סיסמה חדשה", null));
            return;
        }

        /* Verify password confirmation */
        if (!newPassword.equals(confirmPassword)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "אימות הסיסמה אינו תואם", null));
            return;
        }

        /* Create an updated user object */
        User updated = new User();
        updated.setUserId(user.getUserId());
        updated.setUsername(user.getUsername());
        updated.setEmail(user.getEmail());
        updated.setPassword(newPassword);
        updated.setRole(user.getRole());

        /* Save the new password */
        boolean success = userService.updateProfile(updated);

        if (success) {
            /* Update the session user */
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap()
                    .put("user", updated);

            /* Clear password fields */
            currentPassword = null;
            newPassword = null;
            confirmPassword = null;

            /* Show a success message */
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "הסיסמה שונתה בהצלחה", null));
        }
        else {
            /* Show an error if the update fails */
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "שינוי הסיסמה נכשל", null));
        }
    }

    /* Returns the logged-in user */
    private User getLoggedInUser() {

        /* Read the user from the session */
        return (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
    }

    /* Redirects to the specified page */
    private void redirect(String page) {
        try {
            /* Perform the redirect */
            FacesContext.getCurrentInstance().getExternalContext().redirect(page);
        } catch (IOException e) {
            e.printStackTrace();
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

    /* Returns the current password */
    public String getCurrentPassword() {
        return currentPassword;
    }

    /* Updates the current password */
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    /* Returns the new password */
    public String getNewPassword() {
        return newPassword;
    }

    /* Updates the new password */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /* Returns the password confirmation */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /* Updates the password confirmation */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
