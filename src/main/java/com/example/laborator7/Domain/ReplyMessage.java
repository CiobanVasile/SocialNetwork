package com.example.laborator7.Domain;


import java.time.LocalDateTime;
import java.util.List;

public class ReplyMessage extends Message {
    private Message message;

    public ReplyMessage(User from, User to, String message_string, LocalDateTime data, Message message) {
        super(from, to, message_string, data);
        this.message = message;
    }

    public Message getMesaj() {
        return this.message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}

