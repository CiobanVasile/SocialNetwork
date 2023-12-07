package com.example.laborator7.Service;

import java.util.Optional;
import java.util.UUID;

import com.example.laborator7.Domain.Friendship;
import com.example.laborator7.Domain.User;
import com.example.laborator7.Domain.Entity;

/**
 * Interfața Service_Interface reprezintă un set de servicii oferite de aplicație.
 *
 * @param <ID> Tipul de identificator utilizat în entități.
 */
public interface Service_Interface<ID> {

    /**
     * Adaugă un utilizator nou în sistem.
     *
     * @param user Utilizatorul de adăugat.
     * @return true dacă adăugarea s-a realizat cu succes, false în caz contrar.
     */
    boolean addUser(User user);

    /**
     * Șterge un utilizator din sistem în funcție de adresa de email.
     *
     * @param email Adresa de email a utilizatorului de șters.
     * @return Entitatea utilizatorului șters sau null dacă utilizatorul nu a fost găsit.
     */
    Optional<Entity<UUID>> deleteUser(String email);

    /**
     * Crează o relație de prietenie între doi utilizatori în funcție de adresele de email.
     *
     * @param email1 Adresa de email a primului utilizator.
     * @param email2 Adresa de email a celui de-al doilea utilizator.
     * @return true dacă relația de prietenie a fost creată cu succes, false în caz contrar.
     */
    boolean createFriendship(String email1, String email2);
    /**
     * Șterge o relație de prietenie între doi utilizatori în funcție de adresele de email.
     *
     * @param email1 Adresa de email a primului utilizator.
     * @param email2 Adresa de email a celui de-al doilea utilizator.
     * @return Entitatea relației de prietenie ștearsă sau null dacă relația nu a fost găsită.
     */
    Optional<Entity<ID>> deleteFriendship(String email1, String email2);
    /**
     * Returnează toți utilizatorii din sistem.
     *
     * @return Toți utilizatorii din sistem sub formă de Iterable.
     */
    Iterable<User> getAllUsers();
    /**
     * Returnează toate relațiile de prietenie din sistem.
     *
     * @return Toate relațiile de prietenie din sistem sub formă de Iterable.
     */
    Iterable<Friendship> getAllFriendships();

    Optional<User> getUserByEmail(String email);

}
