package com.todolist.todolist;

import com.todolist.todolist.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
public class UserDetailsImplementation implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private String email;


    public static UserDetailsImplementation build(User user) {

        return new UserDetailsImplementation(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getUsername());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

}
