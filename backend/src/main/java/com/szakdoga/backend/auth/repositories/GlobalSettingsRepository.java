package com.szakdoga.backend.auth.repositories;

import com.szakdoga.backend.auth.model.GlobalSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GlobalSettingsRepository extends JpaRepository<GlobalSettings, Long> {
    Optional<Object> findByName(String name);
}
