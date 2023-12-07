package com.example.laborator7.Service;

import com.example.laborator7.Domain.Message;
import com.example.laborator7.Domain.User;
import com.example.laborator7.Repository.MessageDBRepository;
import com.example.laborator7.Repository.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class MessageService {
    private Repository<UUID, Message> messageRepo;

    public MessageService(Repository<UUID, Message> messageRepo) {
        this.messageRepo = messageRepo;
    }

    public boolean addMessage(Message message) {
        Message m;
        try {
            m = (Message) messageRepo.save(message).get();

        }
        catch (Exception e) {
            System.err.println(e);
            return false;
        }

        if(m != null) {
            System.err.println("Exista deja un user cu acest ID!");
            return false;
        }

        return true;
    }

    public Message deleteMessage(UUID id) {
        Message m = null;

        try {
            m = (Message) messageRepo.delete(id).get();
        }
        catch (Exception e) {
            System.err.println(e);
        }

        return m;
    }
    public List<Message> getMessagesBetweenUsers(User user1, User user2){
        return this.messageRepo.getMessagesBetweenUsers(user1, user2);
    }
    public Iterable<Message> getAllMessages() {
        ArrayList<Message> l =  (ArrayList) messageRepo.findAll();
        l.sort(new Comparator<Message>() {
            @Override
            public int compare(Message m1, Message m2) {
                return m1.getData().compareTo(m2.getData());
            }
        });

        return l;
    }
}
