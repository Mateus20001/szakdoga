package com.szakdoga.backend.auth.dtos;

import com.szakdoga.backend.auth.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageDto {
    private String text;

    private String from;

    private LocalDateTime dateTime;
    public MessageDto(String from, String text, LocalDateTime dateTime) {
        this.text = text;
        this.from = from;
        this.dateTime = dateTime;
    }
}
