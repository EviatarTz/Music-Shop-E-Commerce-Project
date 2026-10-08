package managedbeans;

import beans.User;
import service.UserService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.Serializable;

@Named("profileBean")
@RequestScoped
public class ProfileBean implements Serializable {

    private final UserService userService = new UserService();

    private String username;
    private String email;

    private String currentPassword;
    private String newPassword;
    private String confirmPassword;

    public void checkLoginAndLoad() {
        User user = getLoggedInUser();
        if (user == null) {
            redirect("login.xhtml");
            return;
        }
        username = user.getUsername();
        email = user.getEmail();
    }

    public void updateDetails() {
        User user = getLoggedInUser();
        if (user == null) {
            redirect("login.xhtml");
            return;
        }

        User updated = new User();
        updated.setUserId(user.getUserId());
        updated.setUsername(username);
        updated.setEmail(email);
        updated.setPassword(user.getPassword());
        updated.setRole(user.getRole());

        boolean success = userService.updateProfile(updated);

        if (success) {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap()
                    .put("user", updated);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "הפרטים עודכנו בהצלחה", null));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "כתובת האימייל כבר בשימוש על ידי משתמש אחר", null));
        }
    }

    public void changePassword() {
        User user = getLoggedInUser();
        if (user == null) {
            redirect("login.xhtml");
            return;
        }

        if (currentPassword == null || !currentPassword.equals(user.getPassword())) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "הסיסמה הנוכחית שגויה", null));
            return;
        }

        if (newPassword == null || newPassword.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "יש להזין סיסמה חדשה", null));
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "אימות הסיסמה אינו תואם", null));
            return;
        }

        User updated = new User();
        updated.setUserId(user.getUserId());
        updated.setUsername(user.getUsername());
        updated.setEmail(user.getEmail());
        updated.setPassword(newPassword);
        updated.setRole(user.getRole());

        boolean success = userService.updateProfile(updated);

        if (success) {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap()
                    .put("user", updated);

            currentPassword = null;
            newPassword = null;
            confirmPassword = null;

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "הסיסמה שונתה בהצלחה", null));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "שינוי הסיסמה נכשל", null));
        }
    }

    private User getLoggedInUser() {
        return (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
    }

    private void redirect(String page) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCurrentPassword() { return currentPassword; }
    public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}