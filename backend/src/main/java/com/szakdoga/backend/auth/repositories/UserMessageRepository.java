package com.szakdoga.backend.auth.repositories;

import com.szakdoga.backend.auth.model.MessageEntity;
import com.szakdoga.backend.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserMessageRepository extends JpaRepository<MessageEntity, Long> {
    Optional<List<MessageEntity>> findAllByToId(String userid);
}
