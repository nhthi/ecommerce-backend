package com.nht.ecommerce.service;

import com.nht.ecommerce.config.JwtProvider;
import com.nht.ecommerce.exception.UserException;
import com.nht.ecommerce.model.User;
import com.nht.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;



    @Override
    public User findUserById(Long id) throws UserException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserException("User not found with id " + id);
        }
        return user.get();
    }

    @Override
    public User findUserByJwt(String jwt) throws UserException {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        User user = userRepository.findByEmail(email);
        return user;
    }

    @Override
    public List<User> findAllUsers() throws UserException {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User updateUser, String jwt) throws UserException {
        User user = findUserByJwt(jwt);
        if (user == null) throw new UserException("User not found ");
        if(updateUser.getFirstName()!=null){
            user.setFirstName(updateUser.getFirstName());
        }
        if (updateUser.getLastName()!=null){
            user.setLastName(updateUser.getLastName());
        }
        if (updateUser.getEmail()!=null){
            user.setEmail(updateUser.getEmail());
        }
        if(updateUser.isGender() != user.isGender()){
            user.setGender(updateUser.isGender());
        }
        return userRepository.save(user);
    }

    @Override
    public User changePassword(String jwt, String currentPassword, String newPassword) throws UserException {
        User user = findUserByJwt(jwt);
        if (user == null) throw new UserException("User not found ");
        if(!passwordEncoder.matches(currentPassword,user.getPassword())){
            throw new UserException("Current Passowrd is Incorrect !");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if(user==null){
            throw new Exception("User not found with email:  "+email);
        }
        return user;
    }

}
