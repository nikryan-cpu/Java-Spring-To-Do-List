package com.todolist.todolist.controllers;

import com.todolist.todolist.models.User;
import com.todolist.todolist.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import requests.SignInRequest;
import requests.SignUpRequest;

import java.util.Locale;
import java.util.Objects;


@RestController
@RequestMapping("/auth")
public class SecurityController {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;


    @Autowired
    public void setUserRepository (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/create_signup")
    public ModelAndView createSignup(){
        return new ModelAndView("signup");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup (String email, String username, String password) {
        SignUpRequest signUpRequest = new SignUpRequest(email, username, password);
        System.out.println("Received signup request: " + signUpRequest);
        if(Objects.equals(signUpRequest.getUsername().toLowerCase(Locale.ROOT), "freddy")){
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/images/Freddy.jpg"); // Укажите целевой URL
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }
        if(userRepository.existsUserByUsername(signUpRequest.getUsername())){
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/auth/create_wrong_signup_username"); // Укажите целевой URL
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }
        if (userRepository.existsUserByEmail(signUpRequest.getEmail())) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/auth/create_wrong_signup_email"); // Укажите целевой URL
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }
        String hashed = passwordEncoder.encode(signUpRequest.getPassword());
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(hashed);
        user.setUsername(signUpRequest.getUsername());
        userRepository.save(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/auth/create_signin");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }


    @GetMapping("/create_signin")
    public ModelAndView createSignin(){
        return new ModelAndView("signin");
    }

    @GetMapping("/create_wrongsignin")
    public ModelAndView createWrongSignin(){
        return new ModelAndView("wrongsignin");
    }

    @GetMapping("/create_wrong_signup_email")
    public ModelAndView createWrongSignupEmail(){
        return new ModelAndView("wrong_signup_email");
    }

    @GetMapping("/create_wrong_signup_username")
    public ModelAndView createWrongSignupUsername(){
        return new ModelAndView("wrong_signup_username");
    }

@PostMapping("/signin")
public ResponseEntity<?> signin(String username, String password) {
    SignInRequest signInRequest = new SignInRequest(username, password);
    Authentication authentication;
    try {
        authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInRequest.getUsername(), signInRequest.getPassword()));
    } catch (BadCredentialsException e) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/auth/create_wrongsignin"); // Укажите целевой URL
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    SecurityContextHolder.getContext().setAuthentication(authentication);
    System.out.println("User authenticated: " + username); // Логирование
    return ResponseEntity.ok("User signed in successfully");
    }
}
