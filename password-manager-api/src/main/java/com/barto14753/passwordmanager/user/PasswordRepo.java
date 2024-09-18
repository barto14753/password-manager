package com.barto14753.passwordmanager.repo.user;

import com.barto14753.passwordmanager.model.Password;
import com.barto14753.passwordmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PasswordRepo extends JpaRepository<Password, Integer> {
    Optional<Password> findById(Long id);
    List<Password> findAllByOwner(User owner);
}
