package com.example.laborator7.Service;

import com.example.laborator7.Domain.Entity;
import com.example.laborator7.Domain.FriendRequest;
import com.example.laborator7.Domain.Friendship;
import com.example.laborator7.Domain.User;
import com.example.laborator7.Repository.Repository;
import com.example.laborator7.Validators.ValidationException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.example.laborator7.Domain.FriendRequest.ACCEPTED;

/**
 * Clasa Service reprezintă un serviciu care oferă funcționalitate pentru gestionarea utilizatorilor și relațiilor de prietenie.
 *
 */
public class Service implements Service_Interface<UUID> {
    private Repository<UUID, User> userRepo;
    private Repository<UUID, Friendship> friendshipRepo;

    /**
     * Constructor pentru inițializarea serviciului.
     *
     * @param userRepo       Repositoriul de utilizatori.
     * @param friendshipRepo Repositoriul de relații de prietenie.
     */
    public Service(Repository<UUID, User> userRepo, Repository<UUID, Friendship> friendshipRepo) {
        this.userRepo = userRepo;
        this.friendshipRepo = friendshipRepo;
    }

    /**
     * Adaugă un utilizator nou în sistem.
     *
     * @param user Utilizatorul de adăugat.
     * @return true dacă adăugarea s-a realizat cu succes, false în caz contrar.
     */
    public boolean updUser(User user) {
        Optional<User> u;
        if (user.getEmail() == null)
            throw new IllegalArgumentException("Email-ul nu trebuie sa fie gol!");
        else if (getUserByEmail(user.getEmail()).isEmpty())
            throw new IllegalArgumentException("This email address doesn't exists!");
        u = userRepo.update(user);
        return u.isPresent();
    }

    @Override
    public boolean addUser(User user) {
        Optional<User> u;
        if (getUserByEmail(user.getEmail()).isPresent())
            throw new IllegalArgumentException("This email address already exists!");
        u = userRepo.save(user);
        return u.isPresent();
    }

    /**
     * Obține un utilizator din sistem în funcție de adresa de email.
     *
     * @param email Adresa de email a utilizatorului căutat.
     * @return Utilizatorul găsit sau null dacă nu există.
     */
    public Optional<User> getUserByEmail(String email) {
        Optional<User> foundUser = Optional.empty();
        Iterable<User> userRepoAll = userRepo.findAll();
        Stream<User> userStream = StreamSupport.stream(userRepoAll.spliterator(), false);
        foundUser = userStream
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
        return foundUser;
    }

    public void getFriendsbyMonth(String email, int month) {
        Optional<User> user = getUserByEmail(email);
        if (user.isEmpty()) {
            throw new ValidationException("Nu exista acest email!");
        }
        Map<User, LocalDateTime> friends = friendshipRepo.getFriendsbyMonth(user.get(), month);
        friends.forEach((friend, dateTime) -> System.out.println(friend.getFirstName()
                + " " + friend.getLastName() + " " + dateTime.toString()));
    }

    @Override
    /**
     * Șterge un utilizator din sistem pe baza adresei de email.
     *
     * @param email Adresa de email a utilizatorului de șters.
     * @return Utilizatorul șters sau null dacă nu a fost găsit.
     */
    public Optional<Entity<UUID>> deleteUser(String email) {
        Optional<User> userOptional = getUserByEmail(email);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("This user doesn't exists!");
        }
        User user = userOptional.get();
        user.getFriends().forEach(friend -> {
            friendshipRepo.findAll().forEach(f -> {
                if ((f.getUser1().equals(user) && f.getUser2().equals(friend)) ||
                        (f.getUser1().equals(friend) && f.getUser2().equals(user))) {
                    friendshipRepo.delete(f.getId());
                }
            });
            friend.removeFriend(user);
            user.removeFriend(friend);
        });
        Optional<Entity<UUID>> deletedUser = userRepo.delete(user.getId())
                .map(entity -> (Entity<UUID>) entity);
        return deletedUser;
    }

    @Override
    /**
     * Creează o relație de prietenie între doi utilizatori pe baza adreselor de email.
     *
     * @param email1 Adresa de email a primului utilizator.
     * @param email2 Adresa de email a celui de-al doilea utilizator.
     * @return true dacă relația de prietenie a fost creată cu succes, false în caz contrar.
     */
    public boolean createFriendship(String email1, String email2) {
        Optional<Friendship> friendshipOptional;
        Friendship friendship;
        Optional<User> userOptional1, userOptional2;
        User user1, user2;
        try {
            if (email1 == null || email2 == null)
                throw new IllegalArgumentException("Emailul nu trebuie sa fie gol!");
            userOptional1 = getUserByEmail(email1);
            userOptional2 = getUserByEmail(email2);
            if (userOptional1.isEmpty() || userOptional2.isEmpty() ||
                    userOptional1.equals(userOptional2))
                throw new ValidationException("Nu exista aceste doua email-uri!");
            user1 = userOptional1.get();
            user2 = userOptional2.get();

            friendship = new Friendship(user1, user2, LocalDateTime.now(), FriendRequest.ACCEPTED);
            friendshipOptional = friendshipRepo.save(friendship);
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
        return friendshipOptional.isPresent();
    }

    @Override
    /**
     * Șterge o relație de prietenie între doi utilizatori pe baza adresei de email.
     *
     * @param email1 Adresa de email a primului utilizator.
     * @param email2 Adresa de email a celui de-al doilea utilizator.
     * @return Relația de prietenie ștearsă sau null dacă nu a fost găsită.
     */
    public Optional<Entity<UUID>> deleteFriendship(String email1, String email2) {
        Optional<User> user1, user2;
        try {
            user1 = getUserByEmail(email1);
            user2 = getUserByEmail(email2);
            System.out.println(user1);
            System.out.println(user2);

            if (user1.isEmpty() || user2.isEmpty() || user1.equals(user2)) {
                throw new ValidationException("Nu există aceste două email-uri!");
            }
            Optional<Entity<UUID>> deletedFriendship = StreamSupport.stream(friendshipRepo.findAll().spliterator(), false)
                    .filter(el -> (el.getUser1().getId().equals(user1.get().getId()) && el.getUser2().getId().equals(user2.get().getId())) ||
                            (el.getUser1().getId().equals(user2.get().getId()) && el.getUser2().getId().equals(user1.get().getId())))
                    .findFirst()
                    .map(entity -> entity);

            return deletedFriendship
                    .map(deleted -> {
                        friendshipRepo.delete(deleted.getId());
                        return deletedFriendship;
                    })
                    .orElse(deletedFriendship);
        } catch (Exception e) {
            System.err.println(e);
        }
        return Optional.empty();
    }

    @Override
    /**
     * Obține toți utilizatorii din sistem.
     *
     * @return O secvență (iterabilă) de utilizatori.
     */
    public Iterable<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    /**
     * Obține toate relațiile de prietenie din sistem.
     *
     * @return O secvență (iterabilă) de relații de prietenie.
     */
    public Iterable<Friendship> getAllFriendships() {
        return friendshipRepo.findAll();
    }

    /*
     * Adaugă utilizatori și relații de prietenie predefinite în sistem.
     */
    public void acceptFriendship(String email1, String email2) {
        deleteFriendship(email1, email2);
        createFriendship(email1, email2);
    }

    public void declineFriendRequest(String email1, String email2) {
        deleteFriendship(email1, email2);
        Optional<Friendship> f = Optional.empty();
        User u1, u2;
        try {
            if (email1 == null || email2 == null)
                throw new IllegalArgumentException("Emails must not be null!");

            u1 = getUserByEmail(email1).get();
            u2 = getUserByEmail(email2).get();
            if (u1 == null || u2 == null || u1.equals(u2))
                throw new ValidationException("There are no two users with these two emails!");

            f = friendshipRepo.save(new Friendship(u1, u2, LocalDateTime.now(), FriendRequest.REJECTED));
        } catch (Exception e) {
            System.err.println(e);
            return;
        }

    }

    public void createFriendRequest(String email1, String email2) {
        Optional<Friendship> f = Optional.empty();
        User u1, u2;
        try {
            if (email1 == null || email2 == null)
                throw new IllegalArgumentException("Emails must not be null!");

            u1 = getUserByEmail(email1).get();
            u2 = getUserByEmail(email2).get();
            if (u1 == null || u2 == null || u1.equals(u2))
                throw new ValidationException("There are no two users with these two emails!");

            f = friendshipRepo.save(new Friendship(u1, u2, LocalDateTime.now(), FriendRequest.PENDING));
        } catch (Exception e) {
            System.err.println(e);
            return;
        }
    }
}

