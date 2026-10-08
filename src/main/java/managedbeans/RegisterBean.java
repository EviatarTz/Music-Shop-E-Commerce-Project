package managedbeans;

import beans.User;
import service.UserService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.io.Serializable;

@Named("registerBean")
@RequestScoped
public class RegisterBean implements Serializable {

    private final UserService userService = new UserService();

    private String username;
    private String email;
    private String password;

    public String register() {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole("CUSTOMER");

        boolean success = userService.register(user);

        if (success) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "ההרשמה בוצעה בהצלחה, אפשר להתחבר", null));
            return "login?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "כתובת האימייל כבר רשומה במערכת", null));
            return null;
        }
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}