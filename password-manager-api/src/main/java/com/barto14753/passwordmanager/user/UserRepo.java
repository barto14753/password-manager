package com.barto14753.passwordmanager.repo.user;

import com.barto14753.passwordmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

}