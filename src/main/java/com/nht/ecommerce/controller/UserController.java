package com.nht.ecommerce.controller;

import com.nht.ecommerce.exception.UserException;
import com.nht.ecommerce.model.User;
import com.nht.ecommerce.request.ChangePasswordRequest;
import com.nht.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/users/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserByJwt(jwt);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/admin/users")
    public ResponseEntity<List<User>> getAllUsers(@RequestHeader("Authorization") String jwt) throws UserException {
        List<User> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestHeader("Authorization") String jwt, @RequestBody User reqUser) throws UserException {
        User user = userService.findUserByJwt(jwt);
        User updateUser = userService.updateUser(reqUser,jwt);
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @PutMapping("/users/change-pw")
    public ResponseEntity<User> changePassword(@RequestHeader("Authorization") String jwt, @RequestBody ChangePasswordRequest req) throws UserException {
        User user = userService.findUserByJwt(jwt);
        User updateUser = userService.changePassword(jwt,req.getCurrentPassword(),req.getNewPassword());
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }
}
