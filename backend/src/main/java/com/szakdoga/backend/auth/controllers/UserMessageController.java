package com.szakdoga.backend.auth.controllers;

import com.szakdoga.backend.auth.dtos.MessageDto;
import com.szakdoga.backend.auth.dtos.MessageDtoSend;
import com.szakdoga.backend.auth.model.MessageEntity;
import com.szakdoga.backend.auth.services.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-messages")
@CrossOrigin(origins = "http://localhost:4200")
public class UserMessageController {
    @Autowired
    private UserMessageService userMessageService;
    @GetMapping(path = "/me/notifications", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<MessageDto>> getCurrentUserNotifications() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Return 401 if not authenticated
        }

        // Extract principal (user details)
        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UserDetails)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Return 403 if principal is not a valid UserDetails instance
        }

        UserDetails userDetails = (UserDetails) principal;
        String userId = userDetails.getUsername();

        List<MessageDto> list = userMessageService.getAllNotificationByUser(userId);
        return ResponseEntity.ok(list);
    }
    @PostMapping("/create-new")
    ResponseEntity<MessageDto> postMessage(@RequestBody MessageDtoSend messageDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Return 401 if not authenticated
        }

        // Extract principal (user details)
        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UserDetails)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Return 403 if principal is not a valid UserDetails instance
        }

        UserDetails userDetails = (UserDetails) principal;
        String userId = userDetails.getUsername();
        userMessageService.createNewMessage(messageDto.getTo(), userId, messageDto.getText());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
