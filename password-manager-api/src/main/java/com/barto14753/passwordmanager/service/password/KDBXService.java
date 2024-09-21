package com.barto14753.passwordmanager.service.password;

import com.barto14753.passwordmanager.dto.response.kdbx.KDBXResponse;
import com.barto14753.passwordmanager.model.KDBX;
import com.barto14753.passwordmanager.model.Password;
import com.barto14753.passwordmanager.model.User;
import com.barto14753.passwordmanager.repo.password.KDBXRepo;
import com.barto14753.passwordmanager.repo.password.PasswordRepo;
import com.barto14753.passwordmanager.validator.AuthValidator;
import de.slackspace.openkeepass.KeePassDatabase;
import de.slackspace.openkeepass.domain.KeePassFile;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class KDBXService {
    private final KDBXRepo kdbxRepo;
    private final AuthValidator authValidator;
    private final PasswordRepo passwordRepo;

    public List<KDBXResponse> getAllKDBXFiles() {
        User user = authValidator.validateUser();
        List<KDBX> kdbxList = kdbxRepo.findAllByOwner(user);
        log.info("User " + user.getEmail() + " fetched all KDBX files");
        return kdbxList.stream()
                .map(kdbx -> KDBXResponse.builder()
                        .id(kdbx.getId())
                        .owner_id(kdbx.getOwner().getId())
                        .name(kdbx.getName())
                        .password(kdbx.getPassword())
                        .created(kdbx.getCreated().toString())
                        .build())
                .collect(Collectors.toList());
    }

    public KDBXResponse createKDBXResponse(byte[] file, String name, String password) {
        User user = authValidator.validateUser();
        KeePassDatabase database = loadKeePassDatabase(user, file);
        boolean isOpen = false;
        try {
            openKeePassDatabase(user, database, password);
            isOpen = true;
        } catch (Exception e) {
            log.info("User {} failed to open KDBX file {}", user.getEmail(), name);
        }
        KDBX kdbx = KDBX.builder()
                .name(name)
                .owner(user)
                .password(password)
                .open(isOpen)
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

    public KDBXResponse decryptDatabase(Long id, String password) {
        KDBX kdbx = kdbxRepo.findById(id).orElseThrow(
                () -> new IllegalArgumentException("KDBX file with id " + id + " not found"));
        User user = authValidator.validateUser();
        if (!kdbx.getOwner().equals(user)) {
            throw new IllegalArgumentException("KDBX file with id " + id + " not found");
        } else if (kdbx.isOpen()) {
            throw new IllegalArgumentException("KDBX file with id " + id + " is already open");
        }
        KeePassDatabase database = loadKeePassDatabase(user, kdbx.getData());
        KeePassFile keePassFile = openKeePassDatabase(user, database, password);
        kdbx.setPassword(password);
        kdbx.setOpen(true);
        log.info("User {} decrypted KDBX with id {}", user.getEmail(), kdbx.getId());
        List<Password> passwords = createPasswordFromKeePassDatabase(user, keePassFile);
        log.info("User {} create {} passwords from KDBX file", user.getEmail(), passwords.size());
        return KDBXResponse.builder()
                .id(kdbx.getId())
                .owner_id(kdbx.getOwner().getId())
                .name(kdbx.getName())
                .password(kdbx.getPassword())
                .created(kdbx.getCreated().toString())
                .build();
    }

    public void deleteKDBXFile(Long id) {
        KDBX kdbx = kdbxRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("KDBX file with id " + id + " not found"));
        User user = authValidator.validateUser();
        if (!kdbx.getOwner().equals(user)) {
            throw new IllegalArgumentException("KDBX file with id " + id + " not found");
        }
        kdbxRepo.delete(kdbx);
        log.info("User " + user.getEmail() + " deleted KDBX with id " + kdbx.getId());
    }

    private KeePassDatabase loadKeePassDatabase(User user, byte[] file) {
        try {
            Path path = Paths.get("database.kdbx");
            Files.write(path, file);
            KeePassDatabase database = KeePassDatabase.getInstance(path.toFile());
            Files.delete(path);
            return database;
        } catch (Exception e) {
            log.info("User {} failed to load KDBX file", user.getEmail());
            throw new IllegalArgumentException("Error while loading KDBX file");
        }
    }

    private KeePassFile openKeePassDatabase(User user, KeePassDatabase database, String password) {
        try {
            return database.openDatabase(password);
        } catch (Exception e) {
            log.info("User {} failed to open KDBX database", user.getEmail());
            throw new IllegalArgumentException("Wrong password for KDBX database");
        }
    }

    private List<Password> createPasswordFromKeePassDatabase(User owner, KeePassFile keePassFile) {
        return keePassFile.getEntries().stream()
                .map(entry -> {
                    Password password = Password.builder()
                            .name(entry.getTitle())
                            .encryptedValue(entry.getPassword())
                            .owner(owner)
                            .created(System.currentTimeMillis())
                            .modified(System.currentTimeMillis())
                            .build();
                    log.info("User {} create password {} from KDBX file", owner.getEmail(), password.getName());
                    return passwordRepo.save(password);
                }).collect(Collectors.toList());
    }
}
