package beans;

/**
 * Represents a row in the "users" table: an account that can log in,
 * with a role ("CUSTOMER" or "Administrator") that controls access to
 * admin-only pages and actions.
 */
public class User {

    private int userId;
    private String username;
    private String password;
    private String email;
    private String role;

    /* An empty constructor */
    public User(){}

    /* A parameterized constructor */
    public User(int userId, String username, String password, String email, String role){
            this.userId = userId;
            this.username = username;
            this.password = password;
            this.email = email;
            this.role = role;
    }

    /* Gets the ID of the user */
    public int getUserId(){
        return userId;
    }

    /* Gets the username of the user */
    public String getUsername(){
        return username;
    }

    /* Gets the password of the user */
    public String getPassword(){
        return password;
    }

    /* Gets the email of the user */
    public String getEmail(){
        return email;
    }

    /* Gets the Role of the user */
    public String getRole(){
        return role;
    }

    /* Sets the ID of the user */
    public void setUserId(int id) {
        this.userId = id;
    }

    /* Sets the username of the user */
    public void setUsername(String username) {
        this.username = username;
    }

    /* Sets the password of the user */
    public void setPassword(String password) {
        this.password = password;
    }

    /* Sets the email of the user */
    public void setEmail(String email) {
        this.email = email;
    }

    /* Sets the Role of the user */
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString(){
        return username + " " + email;
    }
}
