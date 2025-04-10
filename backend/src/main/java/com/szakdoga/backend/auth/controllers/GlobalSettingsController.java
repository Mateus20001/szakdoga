package com.szakdoga.backend.auth.controllers;

import com.szakdoga.backend.auth.model.GlobalSettings;
import com.szakdoga.backend.auth.services.GlobalSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/global-settings")
@CrossOrigin(origins = "http://localhost:4200")
public class GlobalSettingsController {
    @Autowired
    private GlobalSettingsService globalSettingsService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<GlobalSettings>> getGlobalSettings() {
        List<GlobalSettings> globalSettings = globalSettingsService.get();

        // Return the list of MajorDTOs
        return ResponseEntity.ok(globalSettings);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<GlobalSettings> getGlobalSettings(@PathVariable Long id, @RequestBody GlobalSettings globalSettings) {
        globalSettings.setId(id);
        globalSettingsService.set(globalSettings);
        return ResponseEntity.ok(globalSettings);
    }
}
