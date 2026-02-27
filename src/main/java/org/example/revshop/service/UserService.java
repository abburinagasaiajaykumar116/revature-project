package org.example.revshop.service;



import org.example.revshop.model.User;

public interface UserService {

    public void register(org.example.revshop.model.User user);
    User getByEmail(String email);


    public User login(String email, String password);

    public void changePassword(Integer userId, String newPassword);

    public void forgotPassword(String email, String answer, String newPassword) ;

    public String getSecurityQuestion(String email) ;
}

