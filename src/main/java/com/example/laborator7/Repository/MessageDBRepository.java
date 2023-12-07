package com.example.laborator7.Repository;

import com.example.laborator7.Domain.Message;
import com.example.laborator7.Domain.User;
import com.example.laborator7.Validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class MessageDBRepository implements Repository<UUID,Message>{
    String url;
    String username;
    String password;
    Repository<UUID, User> userRepo;
    public MessageDBRepository(String url, String username, String password, Repository<UUID, User> userRepo) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.userRepo = userRepo;
    }

    @Override
    public Optional<Message> findOne(UUID id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from messages where id = '" + id.toString() + "';");) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    UUID uuid_message = UUID.fromString(resultSet.getString("id_message"));
                    UUID uuid_user = UUID.fromString(resultSet.getString("id_user_from"));
                    UUID uuid_user_2 = UUID.fromString(resultSet.getString("id_user_sent"));
                    String message = resultSet.getString("m");
                    String date = resultSet.getString("date_sent");
                    Message message1 = new Message(userRepo.findOne(uuid_user).get(),userRepo.findOne(uuid_user_2).get(), message, LocalDateTime.now());
                    message1.setId(uuid_message);
                    return Optional.of(message1);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Message> findAll() {
        Set<Message> messageSet = new HashSet<>();
        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement statemenet = con.prepareStatement("select *from messages");
             ResultSet resultSet = statemenet.executeQuery();
        ) {
            while (resultSet.next()) {
                UUID uuid_message = UUID.fromString(resultSet.getString("id_message"));
                UUID uuid_user = UUID.fromString(resultSet.getString("id_user_from"));
                UUID uuid_user_2 = UUID.fromString(resultSet.getString("id_user_sent"));
                String message = resultSet.getString("m");
                String date = resultSet.getString("date_sent");

                Message message1 = new Message(userRepo.findOne(uuid_user).get(),userRepo.findOne(uuid_user_2).get(), message, LocalDateTime.now());
                message1.setId(uuid_message);
            }
            return messageSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private User getUserByEmail(String email) {
            Optional<User> foundUser = Optional.empty();
            Iterable<User> userRepoAll = userRepo.findAll();
            Stream<User> userStream = StreamSupport.stream(userRepoAll.spliterator(), false);
            foundUser = userStream
                    .filter(u -> u.getEmail().equals(email))
                    .findFirst();
            return foundUser.get();
    }

    @Override
    public Map<User, LocalDateTime> getFriendsbyMonth(User user, int month) {
        return null;
    }

    @Override
    public Optional<Message> save(Message entity) {
        String insert_sql = "insert into messages(id_message,id_user_from,id_user_sent,m,date_sent) values (?, ?, ?, ?, ?)";
        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = con.prepareStatement(insert_sql);
        ) {
            statement.setObject(1,entity.getId());
            statement.setObject(2,entity.getFrom().getId());
            statement.setObject(3,entity.getTo().getId());
            statement.setString(4,entity.getMessage());
            statement.setString(5,entity.getData().toString());
            statement.executeUpdate();
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Message> delete(UUID uuid) {
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            String sql = "DELETE from messages where id = '" + uuid + "';";
            statement.executeUpdate(sql);
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return Optional.empty();
    }

    public List<Message> getMessagesBetweenUsers(User user1, User user2) {
        List<Message> messagesBetweenUsers = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM messages " +
                             "WHERE (id_user_from = ? AND id_user_sent = ?) OR (id_user_from = ? AND id_user_sent = ?)"
             )) {
            statement.setObject(1, user1.getId());
            statement.setObject(2, user2.getId());
            statement.setObject(3, user2.getId());
            statement.setObject(4, user1.getId());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    UUID messageId = UUID.fromString(resultSet.getString("id_message"));
                    String messageText = resultSet.getString("m");
                    LocalDateTime messageTimestamp = LocalDateTime.parse(resultSet.getString("date_sent"));

                    Message message;
                    if (resultSet.getObject("id_user_from").equals(user1.getId())) {
                        message = new Message(user1, user2, messageText, messageTimestamp);
                    } else {
                        message = new Message(user2, user1, messageText, messageTimestamp);
                    }

                    message.setId(messageId);
                    messagesBetweenUsers.add(message);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return messagesBetweenUsers;
    }

    @Override
    public Optional<Message> update(Message entity) {
        return Optional.empty();
    }
}
