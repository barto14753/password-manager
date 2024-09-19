package com.barto14753.passwordmanager.repo.password;

import com.barto14753.passwordmanager.model.KDBX;
import com.barto14753.passwordmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KDBXRepo extends JpaRepository<KDBX, Integer> {
    Optional<KDBX> findById(Long id);
    List<KDBX> findAllByOwner(User owner);
    void deleteById(Long id);
}
