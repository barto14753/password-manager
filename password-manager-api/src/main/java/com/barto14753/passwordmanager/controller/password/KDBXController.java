package com.barto14753.passwordmanager.controller.password;

import com.barto14753.passwordmanager.dto.response.kdbx.KDBXResponse;
import com.barto14753.passwordmanager.service.password.KDBXService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/password/kdbx")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
public class KDBXController {
    private final KDBXService kdbxService;

    @GetMapping("/all")
    public ResponseEntity<List<KDBXResponse>> getAllKDBXFiles() {
        List<KDBXResponse> response = kdbxService.getAllKDBXFiles();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ByteArrayResource> getKDBXFile(@PathVariable Long id) {
        byte[] file = kdbxService.getKDBXFile(id);
        return ResponseEntity.ok()
                .contentLength(file.length)
                .header("Content-Disposition", "attachment; filename=Database.kdbx")
                .body(new ByteArrayResource(file));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<KDBXResponse> createKDBXFile(
            @RequestPart("file") MultipartFile file,
            @RequestParam String name,
            @RequestParam(required = false) String password
    ) throws IOException {
        byte[] fileBytes = file.getBytes();
        KDBXResponse response = kdbxService.createKDBXResponse(fileBytes, name, password);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/decrypt")
    public ResponseEntity<KDBXResponse> decryptDatabase(@PathVariable Long id, @RequestParam String password) {
        KDBXResponse response = kdbxService.decryptDatabase(id, password);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteKDBXFile(@PathVariable String id) {
        kdbxService.deleteKDBXFile(Long.parseLong(id));
        return ResponseEntity.ok().build();
    }

}
