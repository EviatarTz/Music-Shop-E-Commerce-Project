package beans;

public class User {

    private int userId;
    private String username;
    private String password;
    private String email;
    private String role;


    public User(){}

    public User(int userId, String username, String password, String email, String role){
            this.userId = userId;
            this.username = username;
            this.password = password;
            this.email = email;
            this.role = role;
    }

    public int getUserId(){ return userId; }
    public String getUsername(){ return username; }
    public String getPassword(){ return password; }
    public String getEmail(){ return email; }
    public String getRole(){ return role; }

    public void setUserId(int id) { this.userId = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString(){
        return username + " " + email;
    }
}
