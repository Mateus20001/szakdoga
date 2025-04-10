package com.szakdoga.backend.auth.services;

import com.szakdoga.backend.auth.model.GlobalSettings;
import com.szakdoga.backend.auth.repositories.GlobalSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GlobalSettingsService {
    @Autowired
    GlobalSettingsRepository repository;

    public List<GlobalSettings> get() {
        return repository.findAll();
    }
    public void set(GlobalSettings globalSettings) {
        repository.findById(globalSettings.getId()).ifPresent(existing -> {
            // Update only the fields that are allowed to change
            existing.setName(globalSettings.getName());
            existing.setActivated(globalSettings.isActivated());
            existing.setAttribute(globalSettings.getAttribute());

            repository.save(existing);
        });
    }
}
