package dao;


import beans.User;
import util.sql.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class UserDAO {

    public User getUserByEmail(String email){

        String sql = "SELECT * FROM users WHERE email = ?";

        try(Connection conn = ConnectionManager.getConnection();PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setString(1, email);
            ResultSet res = statement.executeQuery();
            if (res.next()){

                User user = new User();

                user.setUserId(res.getInt("user_id"));
                user.setUsername(res.getString("username"));
                user.setPassword(res.getString("password"));
                user.setEmail(res.getString("email"));
                user.setRole(res.getString("role"));

                return user;
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public boolean userExists(String email){

        String sql = "SELECT 1 FROM users WHERE email = ?";

        try(Connection conn = ConnectionManager.getConnection();PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setString(1, email);
            ResultSet res = statement.executeQuery();
            return res.next();
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean addUser(User user){
        String sql = """
                INSERT INTO users(username,password,email,role)
                VALUES (? ,? ,? ,?)
                """;
        try(Connection conn = ConnectionManager.getConnection();PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setString(1,user.getUsername());
            statement.setString(2,user.getPassword());
            statement.setString(3,user.getEmail());
            statement.setString(4, user.getRole());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected  > 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public User login(String username,String password){

        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try(Connection conn = ConnectionManager.getConnection();PreparedStatement statement = conn.prepareStatement(sql)){

            statement.setString(1,username);
            statement.setString(2,password);

            ResultSet res = statement.executeQuery();

            if(res.next()){
                User user = new User();

                user.setUserId(res.getInt("user_id"));
                user.setUsername(res.getString("username"));
                user.setPassword(res.getString("password"));
                user.setEmail(res.getString("email"));
                user.setRole(res.getString("role"));

                return user;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateUser(User user){
        String sql = "UPDATE users SET username = ?, email = ?, password = ? WHERE user_id = ?";

        try(Connection conn = ConnectionManager.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setInt(4, user.getUserId());

            return statement.executeUpdate() > 0;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean emailTakenByOther(String email, int userId){
        String sql = "SELECT 1 FROM users WHERE email = ? AND user_id <> ?";

        try(Connection conn = ConnectionManager.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setString(1, email);
            statement.setInt(2, userId);
            ResultSet res = statement.executeQuery();
            return res.next();
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

}