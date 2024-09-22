package com.barto14753.passwordmanager.service.password;

import com.barto14753.passwordmanager.dto.response.kdbx.KDBXResponse;
import com.barto14753.passwordmanager.model.KDBX;
import com.barto14753.passwordmanager.model.User;
import com.barto14753.passwordmanager.repo.password.KDBXRepo;
import com.barto14753.passwordmanager.repo.password.PasswordRepo;
import com.barto14753.passwordmanager.validator.AuthValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KDBXServiceTest {
    @Mock
    private KDBXRepo kdbxRepo;
    @Mock
    private PasswordRepo passwordRepo;
    @Mock
    private AuthValidator authValidator;
    @InjectMocks
    private KDBXService kdbxService;

    private User testUser;
    private KDBX testKDBX;
    private byte[] fileBytes;
    private String filePassword;

    @BeforeEach
    void setUp() throws IOException {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        String fileName = "src/test/resources/Database.kdbx";
        File testFile = new File(fileName);
        Path path = testFile.toPath();
        fileBytes = Files.readAllBytes(path);
        filePassword = "1234";
        testKDBX = KDBX.builder()
                .id(1L)
                .owner(testUser)
                .name("Test KDBX")
                .password(filePassword)
                .open(false)
                .data(fileBytes)
                .created(System.currentTimeMillis())
                .build();
        passwordRepo = mock(PasswordRepo.class);
    }

    @Test
    void getAllKDBXFiles_ShouldReturnListOfKDBXResponses() {
        // Given
        when(authValidator.validateUser()).thenReturn(testUser);
        when(kdbxRepo.findAllByOwner(testUser)).thenReturn(Collections.singletonList(testKDBX));

        // When
        List<KDBXResponse> result = kdbxService.getAllKDBXFiles();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testKDBX.getId(), result.getFirst().getId());
        assertEquals(testKDBX.getName(), result.getFirst().getName());
    }

    @Test
    void createKDBXResponse_ShouldCreateAndReturnKDBXResponse() {
        // Given
        String testName = "New KDBX";
        String testPassword = "1234";
        when(authValidator.validateUser()).thenReturn(testUser);
        when(kdbxRepo.save(any(KDBX.class))).thenReturn(testKDBX);

        // When
        KDBXResponse result = kdbxService.createKDBXResponse(fileBytes, testName, testPassword);

        // Then
        assertNotNull(result);
        assertEquals(testName, result.getName());
        assertEquals(testPassword, result.getPassword());
        verify(kdbxRepo).save(argThat(kdbx ->
                Arrays.equals(fileBytes, kdbx.getData()) &&
                        kdbx.getName().equals(testName) &&
                        kdbx.getPassword().equals(testPassword)
        ));
    }

    @Test
    void getKDBXFile_ShouldReturnKDBXFileData() {
        // Given
        when(authValidator.validateUser()).thenReturn(testUser);
        when(kdbxRepo.findById(1L)).thenReturn(Optional.of(testKDBX));

        // When
        byte[] result = kdbxService.getKDBXFile(1L);

        // Then
        assertNotNull(result);
        assertArrayEquals(testKDBX.getData(), result);
    }

    @Test
    void getKDBXFile_ShouldThrowException_WhenKDBXNotFound() {
        // Given
        when(kdbxRepo.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> kdbxService.getKDBXFile(1L));
    }

    @Test
    void decryptDatabase_ShouldDecryptAndReturnKDBXResponse() {
        // Given
        KDBX closedKDBX = KDBX.builder()
                .id(1L)
                .owner(testUser)
                .name("Closed KDBX")
                .password(null)
                .open(false)
                .data(fileBytes)
                .created(System.currentTimeMillis())
                .build();
        when(authValidator.validateUser()).thenReturn(testUser);
        when(kdbxRepo.findById(1L)).thenReturn(Optional.of(closedKDBX));

        // When
        KDBXResponse result = kdbxService.decryptDatabase(1L, filePassword);

        // Then
        assertNotNull(result);
        assertEquals(closedKDBX.getId(), result.getId());
        assertEquals(closedKDBX.getName(), result.getName());
        assertEquals(filePassword, result.getPassword());
        assertTrue(closedKDBX.isOpen());
    }

    @Test
    void decryptDatabase_ShouldThrowException_WhenAlreadyOpen() {
        // Given
        KDBX openKDBX = KDBX.builder()
                .id(1L)
                .owner(testUser)
                .name("Open KDBX")
                .password("alreadyOpen")
                .open(true)
                .data(fileBytes)
                .created(System.currentTimeMillis())
                .build();
        when(authValidator.validateUser()).thenReturn(testUser);
        when(kdbxRepo.findById(1L)).thenReturn(Optional.of(openKDBX));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> kdbxService.decryptDatabase(1L, "password"));
    }

    @Test
    void deleteKDBXFile_ShouldDeleteKDBXFile() {
        // Given
        when(authValidator.validateUser()).thenReturn(testUser);
        when(kdbxRepo.findById(1L)).thenReturn(Optional.of(testKDBX));

        // When
        kdbxService.deleteKDBXFile(1L);

        // Then
        verify(kdbxRepo, times(1)).delete(testKDBX);
    }

    @Test
    void deleteKDBXFile_ShouldThrowException_WhenKDBXNotFound() {
        // Given
        when(kdbxRepo.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> kdbxService.deleteKDBXFile(1L));
    }
}