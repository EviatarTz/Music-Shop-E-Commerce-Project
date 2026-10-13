package service;

import beans.User;
import dao.UserDAO;

/**
 * Business-logic layer on top of UserDAO: registration, login and profile
 * updates, with basic uniqueness checks.
 */
public class UserService {

    private final UserDAO userDAO = new UserDAO();

    /*
        Registers a new user; fails if the email is already taken (the DB's UNIQUE
        constraint on username also protects against duplicate usernames)
    */
    public boolean register(User user){
        if(userDAO.userExists(user.getEmail())){
            return false;
        }
        return userDAO.addUser(user);
    }

    /* Looks up a user by username + password */
    public User login(String username, String password){
        return userDAO.login(username, password);
    }

    /* Updates a user's profile; fails if the new email is already used by another user */
    public boolean updateProfile(User user){
        if (userDAO.emailTakenByOther(user.getEmail(), user.getUserId())){
            return false;
        }
        return userDAO.updateUser(user);
    }

}