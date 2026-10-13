package dao;


import beans.User;
import util.sql.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Data-access class for the "users" table.
 */
public class UserDAO {

    /* Fetches a user by email, or null if no such user exists */
    public User getUserByEmail(String email){

        String sql = "SELECT * FROM users WHERE email = ?";

        /* Tries to connect to the database and creates a prepared statement */
        try(Connection conn = ConnectionManager.getConnection();PreparedStatement statement = conn.prepareStatement(sql)){

            statement.setString(1, email);

            /* Execute the query and save the resulted rows in the result set */
            ResultSet res = statement.executeQuery();

            /* if there is a row in the result set, create an Order object and fill it's fields with the row's data */
            if (res.next()){

                User user = new User();

                user.setUserId(res.getInt("user_id"));
                user.setUsername(res.getString("username"));
                user.setPassword(res.getString("password"));
                user.setEmail(res.getString("email"));
                user.setRole(res.getString("role"));

                /* Returns the user */
                return user;
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        /* If the connection to the database has failed, return null */
        return null;
    }

    /* Checks whether a user with this email already exists (used during registration) */
    public boolean userExists(String email){

        String sql = "SELECT 1 FROM users WHERE email = ?";

        /* Tries to connect to the database and creates a prepared statement */
        try(Connection conn = ConnectionManager.getConnection();PreparedStatement statement = conn.prepareStatement(sql)){

            statement.setString(1, email);

            /* Execute the query and saves the resulted rows in the result set */
            ResultSet res = statement.executeQuery();

            /* Returns true if there is a resulted row (if the user exists) */
            return res.next();

        }catch(Exception e){
            e.printStackTrace();
        }
        /* Returns false if the connection to the database has failed */
        return false;
    }

    /*
        Inserts a new user. Note: the "username" column has a UNIQUE constraint in the
        database, so if the insert's username already exists, this will throw and the
        catch block below will return false - duplicate usernames can't actually be
        created even though this class has no separate "usernameExists" check.
    */
    public boolean addUser(User user){

        String sql = """
                INSERT INTO users(username,password,email,role)
                VALUES (? ,? ,? ,?)
                """;

        /* Tries to connect to the database and creates a prepared statement */
        try(Connection conn = ConnectionManager.getConnection();PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setString(1,user.getUsername());
            statement.setString(2,user.getPassword());
            statement.setString(3,user.getEmail());
            statement.setString(4, user.getRole());

            /* Gets the number of affected rows (should be one row - one user) */
            int rowsAffected = statement.executeUpdate();

            /* Returns the number of rows affected (should be one for each user) */
            return rowsAffected  > 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        /* Returns false if the connection to the database has failed */
        return false;
    }

    /* Looks up a user by matching username and password (plain-text comparison) */
    public User login(String username,String password){

        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        /* Tries to connect to the database and creates a prepared statement */
        try(Connection conn = ConnectionManager.getConnection();PreparedStatement statement = conn.prepareStatement(sql)){

            statement.setString(1,username);
            statement.setString(2,password);

            /* Execute the query and save the resulted rows in the result set */
            ResultSet res = statement.executeQuery();

            /* If a resulted row exists (the query yields a row) */
            if(res.next()){

                User user = new User();

                user.setUserId(res.getInt("user_id"));
                user.setUsername(res.getString("username"));
                user.setPassword(res.getString("password"));
                user.setEmail(res.getString("email"));
                user.setRole(res.getString("role"));

                /* returns the user object along with it's values from the query */
                return user;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        /* If the connection to the database hs failed */
        return null;
    }

    /* Updates a user's username, email and password */
    public boolean updateUser(User user){

        String sql = "UPDATE users SET username = ?, email = ?, password = ? WHERE user_id = ?";

        /* Tries to connect to the database and creates a prepared statement */
        try(Connection conn = ConnectionManager.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setInt(4, user.getUserId());

            /* Returns true if there are more than one row affected */
            return statement.executeUpdate() > 0;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        /* Returns false if the connection to the database hs failed */
        return false;
    }

    /* Checks whether the given email is already used by a different user (for profile updates) */
    public boolean emailTakenByOther(String email, int userId){

        String sql = "SELECT 1 FROM users WHERE email = ? AND user_id <> ?";

        /* Tries to connect to the database and creates a prepared statement */
        try(Connection conn = ConnectionManager.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)){

            statement.setString(1, email);
            statement.setInt(2, userId);

            /* Execute the query and saves the resulted rows in the result set */
            ResultSet res = statement.executeQuery();

            /* Returns true if the query yields a result row (a user with the given email) */
            return res.next();

        }
        catch(Exception e){
            e.printStackTrace();
        }

        /* Returns false if the connection to the database hs failed */
        return false;
    }

}