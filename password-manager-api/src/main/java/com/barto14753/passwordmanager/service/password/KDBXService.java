package com.barto14753.passwordmanager.service.password;

import com.barto14753.passwordmanager.dto.response.kdbx.KDBXResponse;
import com.barto14753.passwordmanager.model.KDBX;
import com.barto14753.passwordmanager.model.User;
import com.barto14753.passwordmanager.repo.password.KDBXRepo;
import com.barto14753.passwordmanager.validator.AuthValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class KDBXService {
    private final KDBXRepo kdbxRepo;
    private final AuthValidator authValidator;

    public KDBXResponse createKDBXResponse(byte[] file, String name, String password) {
        User user = authValidator.validateUser();
        KDBX kdbx = KDBX.builder()
                .name(name)
                .owner(user)
                .password(password)
                .data(file)
                .created(System.currentTimeMillis())
                .build();
        kdbxRepo.save(kdbx);
        log.info("User " + user.getEmail() + " created KDBX with id " + kdbx.getId());
        return KDBXResponse.builder()
                .id(kdbx.getId())
                .owner_id(kdbx.getOwner().getId())
                .name(kdbx.getName())
                .password(kdbx.getPassword())
                .created(kdbx.getCreated().toString())
                .build();
    }

    public byte[] getKDBXFile(Long id) {
        KDBX kdbx = kdbxRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("KDBX file with id " + id + " not found"));
        User user = authValidator.validateUser();
        if (!kdbx.getOwner().equals(user)) {
            throw new IllegalArgumentException("KDBX file with id " + id + " not found");
        }
        log.info("User " + user.getEmail() + " downloaded KDBX with id " + kdbx.getId());
        return kdbx.getData();
    }

}
