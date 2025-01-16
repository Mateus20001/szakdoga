package com.szakdoga.backend.auth.repositories;

import com.szakdoga.backend.auth.model.PhoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneRepository extends JpaRepository<PhoneEntity, Long> {
}
