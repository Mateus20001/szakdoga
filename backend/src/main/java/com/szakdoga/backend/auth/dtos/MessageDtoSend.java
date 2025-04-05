package com.szakdoga.backend.auth.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageDtoSend {
    private String text;

    private String to;

    private LocalDateTime dateTime;
    public MessageDtoSend(String to, String text, LocalDateTime dateTime) {
        this.text = text;
        this.to = to;
        this.dateTime = dateTime;
    }
}
