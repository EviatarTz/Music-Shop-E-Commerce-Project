package service;

import beans.User;
import dao.UserDAO;

public class UserService {

    private final UserDAO userDAO = new UserDAO();

    public boolean register(User user){
        if(userDAO.userExists(user.getEmail())){
            return false;
        }
        return userDAO.addUser(user);
    }

    public User login(String username, String password){
        return userDAO.login(username, password);
    }

    public boolean updateProfile(User user){
        if (userDAO.emailTakenByOther(user.getEmail(), user.getUserId())){
            return false;
        }
        return userDAO.updateUser(user);
    }

}