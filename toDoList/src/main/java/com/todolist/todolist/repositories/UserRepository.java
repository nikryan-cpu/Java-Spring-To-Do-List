package com.todolist.todolist.repositories;

import com.todolist.todolist.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsUserByEmail(String email);

    boolean existsUserByUsername(String username);


    Optional<User> findByEmail(String username);
}
