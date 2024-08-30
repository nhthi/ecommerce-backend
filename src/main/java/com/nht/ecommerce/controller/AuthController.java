package com.nht.ecommerce.controller;

import com.nht.ecommerce.config.JwtProvider;
import com.nht.ecommerce.exception.UserException;
import com.nht.ecommerce.model.Cart;
import com.nht.ecommerce.model.User;
import com.nht.ecommerce.model.Verify;
import com.nht.ecommerce.repository.CartRepository;
import com.nht.ecommerce.repository.UserRepository;
import com.nht.ecommerce.repository.VerifyRepository;
import com.nht.ecommerce.request.LoginRequest;
import com.nht.ecommerce.request.ResetPasswordRequest;
import com.nht.ecommerce.response.AuthResponse;
import com.nht.ecommerce.response.MessageResponse;
import com.nht.ecommerce.service.CartService;
import com.nht.ecommerce.service.CustomerUserDeatilsServiceImp;
import com.nht.ecommerce.service.EmailService;
import com.nht.ecommerce.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Random;

@RestController
@RequestMapping("/auth")

public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private CustomerUserDeatilsServiceImp customerUserDeatilsServiceImp;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private VerifyRepository verifyRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;


    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(@RequestParam("email") String email) throws Exception {
        User user = userService.findUserByEmail(email);
        String code = generateRandomNumber();
        Verify verify = verifyRepository.findByEmail(email);
        if (verify == null) {
            verify = new Verify();
            verify.setEmail(email);
        }
        verify.setVerifyCode(code);
        verifyRepository.save(verify);
        String subject = "Verify Your Email";
        String htmlContent = "<!DOCTYPE html>"
                + "<html lang='en'>"
                + "<head>"
                + "<meta charset='UTF-8'>"
                + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                + "<title>Email Verification</title>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }"
                + ".email-container { max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }"
                + ".header { text-align: center; padding-bottom: 20px; }"
                + ".header img { width: 100px; border-radius: 50%; }"
                + ".header h1 { color: #333333; font-size: 24px; margin: 20px 0 10px; }"
                + ".content { color: #666666; font-size: 16px; line-height: 1.6; }"
                + ".verification-code { background-color: #007BFF; color: #ffffff; font-size: 22px; font-weight: bold; text-align: center; padding: 15px; border-radius: 5px; margin: 20px 0; letter-spacing: 2px; }"
                + ".cta-button { display: inline-block; background-color: #28a745; color: #FFFFFF; padding: 5px 20px; text-align: center; text-decoration: none; border-radius: 5px; font-size: 15px; font-weight: bold; transition: background-color 0.3s ease; }"
                + ".footer { text-align: center; padding-top: 20px; color: #999999; font-size: 14px; }"
                + ".footer a { color: #007BFF; text-decoration: none; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='email-container'>"
                + "<div class='header'>"
                + "<img src='https://logolook.net/wp-content/uploads/2021/07/Naruto-Logo.png' alt='Company Logo'>"
                + "<h1>Email Verification</h1>"
                + "</div>"
                + "<div class='content'>"
                + "<p>Hello,</p>"
                + "<p>Thank you for registering with us! To complete your registration, please verify your email address by using the verification code below:</p>"
                + "<div class='verification-code'>" + code + "</div>"
                + "<p>If you did not sign up for this account, please ignore this email or contact our support team.</p>"
                + "<a href='http://localhost:3000/account/forgotpassword?open=true' class='cta-button'>Verify Here</a>"
                + "</div>"
                + "<div class='footer'>"
                + "<p>If you have any questions, feel free to <a href='https://example.com/support'>contact us</a>.</p>"
                + "<p>&copy; 2024 Company Name. All rights reserved.</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
        emailService.sendHtmlEmail(email, subject, htmlContent);
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Send verify code success (^.^)");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(@RequestBody ResetPasswordRequest req) throws Exception {
        Verify verify = verifyRepository.findByEmail(req.getEmail());
        MessageResponse res = new MessageResponse();
        if (verify != null) {
            if (verify.getVerifyCode().equals(req.getCode())) {
                res.setMessage(UpdatePassword(req.getEmail(), req.getNewPassword()));
                verifyRepository.delete(verify);
            } else res.setMessage("Invalid verification code (-_-)");
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    public String UpdatePassword(String email, String newPassword) throws Exception {
        User user = userService.findUserByEmail(email);
        user.setPassword(passwordEncoder.encode(newPassword));
        return "Update password success (^_^)";
    }


    public static String generateRandomNumber() {
        Random random = new Random();
        int number = 1000 + random.nextInt(9000);
        return String.valueOf(number);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {
        System.out.println("reg user: " + user);
        User isEmailExist = userRepository.findByEmail(user.getEmail());
        if (isEmailExist != null) {
            throw new UserException("Email is already used with another account");
        }
        User createUser = new User();
        createUser.setEmail(user.getEmail());
        createUser.setPassword(passwordEncoder.encode(user.getPassword()));
        createUser.setFirstName(user.getFirstName());
        createUser.setLastName(user.getLastName());
        createUser.setMobile(user.getMobile());
        createUser.setCreateAt(LocalDateTime.now());
        createUser.setRole("ROLE_CUSTOMER");
        User savedUser = userRepository.save(createUser);

        Cart cart = new Cart();
        cart.setUser(createUser);
        cartRepository.save(cart);


        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse authRespone = new AuthResponse();
        authRespone.setJwt(jwt);
        authRespone.setMessage("Register Success");
        authRespone.setRole(savedUser.getRole());
        return new ResponseEntity<>(authRespone, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest req) throws UserException {
        String username = req.getEmail();
        String password = req.getPassword();
        Authentication authentication = authenticate(username, password);

//        Collection<?extends GrantedAuthority> authorities = authentication.getAuthorities();
//        String role = authorities.isEmpty()?null:authorities.iterator().next().getAuthority();

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);
        AuthResponse authRespone = new AuthResponse();
        authRespone.setJwt(jwt);
        authRespone.setMessage("Signin Success");
//        authRespone.setRole(USER_ROLE.valueOf(role));
        return new ResponseEntity<>(authRespone, HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password) throws UserException {
        UserDetails userDetails = customerUserDeatilsServiceImp.loadUserByUsername(username);
        if (userDetails == null) {
            throw new UserException("Email does not exist !");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new UserException("Password is incorrect !");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
