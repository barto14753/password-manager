package com.barto14753.passwordmanager.controller.password;

import com.barto14753.passwordmanager.dto.response.kdbx.KDBXResponse;
import com.barto14753.passwordmanager.service.password.KDBXService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/password/kdbx")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
public class KDBXController {
    private final KDBXService kdbxService;

    @GetMapping("/all")
    public ResponseEntity<List<KDBXResponse>> getAllKDBXFiles() {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ByteArrayResource> getKDBXFile(@PathVariable Long id) {
        byte[] file = kdbxService.getKDBXFile(id);
        return ResponseEntity.ok()
                .contentLength(file.length)
                .header("Content-Disposition", "attachment; filename=Database.kdbx")
                .body(new ByteArrayResource(file));
    }

    @PostMapping
    public ResponseEntity<KDBXResponse> createKDBXFile(@RequestBody byte[] file, @RequestParam String name, @RequestParam String password) {
        KDBXResponse response = kdbxService.createKDBXResponse(file, name, password);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}")
    public ResponseEntity<KDBXResponse> decryptFile(@PathVariable Long id, @RequestParam String password) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteKDBXFile(@PathVariable String id) {
        return null;
    }

}
