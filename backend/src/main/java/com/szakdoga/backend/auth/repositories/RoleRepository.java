package com.szakdoga.backend.auth.repositories;

import com.szakdoga.backend.auth.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
}
