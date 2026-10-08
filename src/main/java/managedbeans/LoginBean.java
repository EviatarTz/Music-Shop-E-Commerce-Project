package managedbeans;

import beans.User;
import service.UserService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.io.Serializable;

@Named("loginBean")
@RequestScoped
public class LoginBean implements Serializable {

    private final UserService userService = new UserService();

    private String username;
    private String password;

    public String login() {
        User user = userService.login(username, password);

        if (user != null) {
            // כותב לאותו HttpSession attribute "user" שה-Filters וה-Servlets הקיימים
            // (AuthFilter, AdminFilter, CartServlet וכו') כבר קוראים ממנו - כך שהתאימות
            // בין העולם הישן (JSP/Servlet) לחדש (JSF) נשמרת בזמן המעבר.
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap()
                    .put("user", user);

            return "products?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid username or password", null));
            return null; // נשאר על אותו עמוד
        }
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}