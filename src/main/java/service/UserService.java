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
        /* If the user's email exists in the database (a user exists) - the user cannot be added to the database  */
        if(userDAO.userExists(user.getEmail())){
            return false;
        }
        /* If the user doesn't exist, create a new one in the database */
        return userDAO.addUser(user);
    }

    /* Looks up a user by username + password */
    public User login(String username, String password){
        /* If the user exists in the database - return it.otherwise, returns null */
        return userDAO.login(username, password);
    }

    /* Updates a user's profile; fails if the new email is already used by another user */
    public boolean updateProfile(User user){
        /* If there is another user with the same email */
        if (userDAO.emailTakenByOther(user.getEmail(), user.getUserId())){
            /* The user's email cannot be updated because there's another user has this email and cannot update both  */
            return false;
        }
        /* If the email is exclusive, update the user's data in the corresponding record in the users table */
        return userDAO.updateUser(user);
    }

}