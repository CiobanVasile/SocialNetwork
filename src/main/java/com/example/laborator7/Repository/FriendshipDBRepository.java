package com.example.laborator7.Repository;

import com.example.laborator7.Domain.FriendRequest;
import com.example.laborator7.Domain.Friendship;
import com.example.laborator7.Domain.User;
import com.example.laborator7.Validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FriendshipDBRepository implements Repository<UUID, Friendship>{
    String url;
    String username;
    String password;
    Validator<Friendship> friendshipValidator;
    Repository<UUID, User> userRepo;

    public FriendshipDBRepository(String url, String username, String password, Validator<Friendship> friendshipValidator,Repository<UUID,User> userRepo){
        this.url = url;
        this.username = username;
        this.password = password;
        this.friendshipValidator = friendshipValidator;
        this.userRepo = userRepo;
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendShips = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendship;");
             ResultSet resultSet = statement.executeQuery())
        {
            while (resultSet.next()) {
                UUID id_ = UUID.fromString(resultSet.getString("id_friendship"));
                UUID id1 = UUID.fromString(resultSet.getString("id_user1"));
                UUID id2 = UUID.fromString(resultSet.getString("id_user2"));
                LocalDateTime from = LocalDateTime.parse(resultSet.getString("nume_coloana"));
                String status = resultSet.getString("status");

                User u1 = userRepo.findOne(id1).get();
                User u2 = userRepo.findOne(id2).get();

                u1.addFriend(u2);
                u2.addFriend(u1);

                Friendship friendShip = new Friendship(u1, u2, from, FriendRequest.valueOf(status));
                friendShip.setId(id_);

                friendShips.add(friendShip);
            }
            return friendShips;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Friendship> save(Friendship entity) {
        String insert_sql = "INSERT INTO friendship(id_friendship,id_user1,id_user2,nume_coloana,status) VALUES(?,?,?,?,?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(insert_sql))
            {
                statement.setObject(1, entity.getId());
                statement.setObject(2, entity.getUser1().getId());
                statement.setObject(3, entity.getUser2().getId());
                statement.setString(4, entity.getDate().toString());
                statement.setString(5,entity.getAcceptance());
                statement.executeUpdate();
                return Optional.empty();
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }
    @Override
    public Optional<Friendship> delete(UUID id) {
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            String sql = "DELETE from friendship where id_friendship = '" + id + "';";
            statement.executeUpdate(sql);
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Friendship> update(Friendship entity) {
        return null;
    }

    @Override
    public List<Friendship> getMessagesBetweenUsers(User user1, User user2) {
        return null;
    }

    @Override
    public Optional<Friendship> findOne(UUID id) {
        Friendship friendShip = null;
        String query = "SELECT * FROM friendship WHERE id_friendship = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id); // Setează parametrul pentru id

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    UUID id_ = UUID.fromString(resultSet.getString("id_friendship"));
                    UUID id1 = UUID.fromString(resultSet.getString("id_user1"));
                    UUID id2 = UUID.fromString(resultSet.getString("id_user2"));
                    LocalDateTime from = LocalDateTime.parse(resultSet.getString("nume_coloana"));
                    String status = resultSet.getString("status");


                    User u1 = userRepo.findOne(id1).get();
                    User u2 = userRepo.findOne(id2).get();
                    friendShip = new Friendship(u1, u2, from,FriendRequest.valueOf(status));
                    friendShip.setId(id_);

                    return Optional.of(friendShip);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
    public Map<User, LocalDateTime> getFriendsbyMonth(User user, int month) {
        Map<User, LocalDateTime> friendsMap = new HashMap<>();

        String query = "SELECT u.*, f.nume_coloana FROM friendship f " +
                "INNER JOIN users u ON (f.id_user1 = u.id OR f.id_user2 = u.id) " +
                "WHERE (f.id_user1 = ? OR f.id_user2 = ?) AND EXTRACT(MONTH FROM TO_TIMESTAMP(f.nume_coloana, 'YYYY-MM-DD\"T\"HH24:MI:SS')) = ?";

        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = con.prepareStatement(query);
        ) {
            statement.setObject(1, user.getId());
            statement.setObject(2, user.getId());
            statement.setInt(3, month);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    UUID friendId = resultSet.getObject("id", UUID.class);

                    // Verificăm dacă id-ul prietenului nu este același cu id-ul utilizatorului curent
                    if (!user.getId().equals(friendId)) {
                        String firstName = resultSet.getString("first_name");
                        String lastName = resultSet.getString("last_name");
                        int age = resultSet.getInt("age");
                        String email = resultSet.getString("email");
                        String passwd = resultSet.getString("passwd");

                        User friend = new User(firstName, lastName, age, email, passwd);
                        friend.setId(friendId);

                        String dateAsString = resultSet.getString("nume_coloana");
                        LocalDateTime friendshipDate = LocalDateTime.parse(dateAsString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS"));

                        friendsMap.put(friend, friendshipDate);
                    }
                }
            }

            return friendsMap;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}




