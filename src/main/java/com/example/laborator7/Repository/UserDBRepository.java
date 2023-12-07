package com.example.laborator7.Repository;


import com.example.laborator7.Domain.User;
import com.example.laborator7.Validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class UserDBRepository implements Repository<UUID, User> {

    String url;
    String username;
    String password;
    Validator<User> userValidator;


    public UserDBRepository(String url, String username, String password, Validator<User> userValidator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.userValidator = userValidator;
    }


    @Override
    public Optional<User> findOne(UUID id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users where id = '" + id.toString() + "';");) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    UUID uuid = UUID.fromString(resultSet.getString("id"));
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    Integer age = resultSet.getInt("age");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("passwd");
                    User user = new User(firstName, lastName, age, email, password);
                    user.setId(uuid);
                    return Optional.of(user);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> useriSet = new HashSet<>();
        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement statemenet = con.prepareStatement("select *from users");
             ResultSet resultSet = statemenet.executeQuery();
        ) {
            while (resultSet.next()) {
                UUID id = UUID.fromString(resultSet.getString("id"));
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Integer age = resultSet.getInt("age");
                String email = resultSet.getString("email");
                String password = resultSet.getString("passwd");
                User user = new User(firstName, lastName, age, email, password);
                user.setId(id);
                useriSet.add(user);
            }
            return useriSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<User, LocalDateTime> getFriendsbyMonth(User user, int month) {
        return null;
    }

    @Override
    public Optional<User> save(User entity) {
        String insert_sql = "insert into users (id, first_name, last_name, age, email, passwd) values (?, ?, ?, ?, ?, ?)";
        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = con.prepareStatement(insert_sql);
        ) {
            statement.setObject(1, entity.getId());
            statement.setString(2, entity.getFirstName());
            statement.setString(3, entity.getLastName());
            statement.setInt(4, entity.getAge());
            statement.setString(5, entity.getEmail());
            statement.setString(6, entity.getPassword());
            statement.executeUpdate();
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Optional<User> delete(UUID uuid) {
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            String sql = "DELETE from users where id = '" + uuid + "';";
            statement.executeUpdate(sql);
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> update(User entity) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            String sql = "UPDATE users set first_name = '" + entity.getFirstName() + "' " + " where id = '" + entity.getId() + "';";
            statement.executeUpdate(sql);
            sql = "UPDATE users set last_name = '" + entity.getLastName() + "' " + " where id = '" + entity.getId() + "';";
            statement.executeUpdate(sql);
            sql = "UPDATE users set email = '" + entity.getEmail() + "' " + " where id = '" + entity.getId() + "';";
            statement.executeUpdate(sql);
            sql = "UPDATE users set age = '" + entity.getAge() + "' " + " where id = '" + entity.getId() + "';";
            statement.executeUpdate(sql);
            }
            catch (SQLException e) {
                System.out.println(e);
            }
        return Optional.of(entity);
    }

    @Override
    public List<User> getMessagesBetweenUsers(User user1, User user2) {
        return null;
    }

    /*@Override
    public Iterable<Object> getFriendsUser(User user) {
        return null;
    }
     */
}