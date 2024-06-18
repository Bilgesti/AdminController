package com.example.login.service;


import com.example.login.model.User;
import com.example.login.model.UserDTO;

import java.util.List;
/**
 * UserService är ett gränssnitt för användartjänster, såsom att hitta, registrera och ta bort användare.
 */
public interface UserService {
    List<User> findAll();
    void deleteUser(String email);
    User registerUser(UserDTO userDTO);
    User findByEmail(String email);
}
