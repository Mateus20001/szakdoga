package com.szakdoga.backend.auth.repositories;

import com.szakdoga.backend.auth.model.MajorDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MajorDetailsRepository extends JpaRepository<MajorDetails, Integer> {
}
