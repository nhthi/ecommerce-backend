package com.nht.ecommerce.service;

import com.nht.ecommerce.exception.UserException;
import com.nht.ecommerce.model.User;

import java.util.List;

public interface UserService {

    public User findUserById(Long id) throws UserException;

    public User findUserByJwt(String jwt) throws UserException;

    public List<User> findAllUsers() throws UserException;

    public User updateUser(User user,String jwt) throws UserException;

    public User changePassword(String jwt, String currentPassword,String newPassword) throws UserException;

    public User findUserByEmail(String email) throws Exception;
}
