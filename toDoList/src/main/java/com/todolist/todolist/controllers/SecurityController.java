package com.todolist.todolist.controllers;

import com.todolist.todolist.jwt.JwtCore;
import com.todolist.todolist.models.User;
import com.todolist.todolist.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import requests.SignInRequest;
import requests.SignUpRequest;

@RestController
@RequestMapping("/auth")
public class SecurityController {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    private JwtCore jwtCore;


    @Autowired
    public void setUserRepository (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setJwtCore(JwtCore jwtCore) {
        this.jwtCore = jwtCore;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
        public ResponseEntity<?> signup (@RequestBody SignUpRequest signUpRequest){
            if (userRepository.existsUserByEmail(signUpRequest.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This email already is already in use");
            }
            String hashed = passwordEncoder.encode(signUpRequest.getPassword());
            User user = new User();
            user.setEmail(signUpRequest.getEmail());
            user.setPassword(hashed);
            user.setUsername(signUpRequest.getUsername());
            userRepository.save(user);
            return ResponseEntity.ok("You have successfully registered!");


    }


    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SignInRequest signInRequest){
        Authentication authentication = null;
        try{
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    signInRequest.getUsername(), signInRequest.getPassword()));
        }
        catch(BadCredentialsException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtCore.generateToken(authentication);
        return ResponseEntity.ok(jwt); // Возвращает токен пользователя
    }

}
