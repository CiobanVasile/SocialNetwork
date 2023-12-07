package com.example.laborator7.Domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Message extends Entity<UUID> {

    private UUID id;
    private User from;
    private User to;
    private String message;
    private LocalDateTime data;

    public Message(User from, User to, String message, LocalDateTime data) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.data = data;
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", from=" + from +
                ", to=" + to +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    @Override

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;

        Message message1 = (Message) o;

        if (!getId().equals(message1.getId())) return false;
        if (!getFrom().equals(message1.getFrom())) return false;
        if (!getTo().equals(message1.getTo())) return false;
        if (!getMessage().equals(message1.getMessage())) return false;
        return getData().equals(message1.getData());
    }

}
