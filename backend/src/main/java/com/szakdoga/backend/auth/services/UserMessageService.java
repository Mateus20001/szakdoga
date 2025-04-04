package com.szakdoga.backend.auth.services;

import com.szakdoga.backend.auth.dtos.MessageDto;
import com.szakdoga.backend.auth.model.MessageEntity;
import com.szakdoga.backend.auth.repositories.UserMessageRepository;
import com.szakdoga.backend.courses.models.Grade;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserMessageService {
    @Autowired
    private UserMessageRepository userMessageRepository;

    public void createGradeNotification(Grade grade) {
        MessageEntity message = new MessageEntity();
        message.setText("Önnek " + grade.getGradeValue() + " értékelést írtak be " + grade.getCourseApplicationEntity().getCourseDateEntity().getName() + " tárgyból. \n Az értékelő: " + grade.getGradedBy().getName() + "(" + grade.getGradedBy().getId() + ").");
        message.setTo(grade.getCourseApplicationEntity().getUser());
        message.setFrom("SZTE karbantartás");
        message.setCreationDate(grade.getCreationDate());
        userMessageRepository.save(message);
    }
    @Transactional
    public List<MessageDto> getAllNotificationByUser(String id) {
        Optional<List<MessageEntity>> optionalMessages = userMessageRepository.findAllByToId(id);

        return optionalMessages
                .orElse(Collections.emptyList())
                .stream()
                .map(message -> new MessageDto(
                        message.getFrom(), // or message.getFrom().getId() or getName()
                        message.getText(),
                        message.getCreationDate()
                ))
                .collect(Collectors.toList());
    }
}
