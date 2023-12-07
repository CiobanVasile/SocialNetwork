package com.example.laborator7.Domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Clasa User reprezintă un utilizator într-un sistem sau aplicație.
 */
public class User extends Entity<UUID> {
    private String firstName;
    private Integer age;
    private String email;
    private String password;
    private String lastName;
    private Map<UUID, User> friends;
    /**
     * Constructor pentru inițializarea unui obiect User.
     *
     * @param firstName Prenumele utilizatorului.
     * @param lastName Numele de familie al utilizatorului.
     * @param age Vârsta utilizatorului.
     * @param email Adresa de email a utilizatorului.
     * @param password Parola utilizatorului.
     */
    public User(String firstName, String lastName, Integer age, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.password = password;
        this.friends = new HashMap<>();
        this.setId(UUID.randomUUID()); // Generare automată a unui ID UUID pentru utilizator.
    }

    /**
     * Returnează prenumele utilizatorului.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Actualizează prenumele utilizatorului.
     *
     * @param firstName Noul prenume al utilizatorului.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returnează adresa de email a utilizatorului.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Actualizează adresa de email a utilizatorului.
     *
     * @param email Noua adresă de email a utilizatorului.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returnează parola utilizatorului.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Actualizează parola utilizatorului.
     *
     * @param password Noua parolă a utilizatorului.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returnează numele de familie al utilizatorului.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Actualizează numele de familie al utilizatorului.
     *
     * @param lastName Noul nume de familie al utilizatorului.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Actualizează vârsta utilizatorului.
     *
     * @param age Noua vârstă a utilizatorului.
     */
    public void setAge(Integer age){
        this.age = age;
    }

    /**
     * Returnează vârsta utilizatorului.
     */
    public Integer getAge(){
        return this.age;
    }

    /**
     * Returnează lista de prieteni a utilizatorului sub formă de Iterable.
     */
    public Iterable<User> getFriends() {
        return friends.values();
    }

    @Override
    public String toString() {
        // Returnează o reprezentare sub formă de șir a obiectului User.
        return email;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getFirstName(), user.getFirstName()) && Objects.equals(getAge(), user.getAge()) && Objects.equals(getId(), user.getId()) && Objects.equals(getLastName(), user.getLastName()) && Objects.equals(getFriends(), user.getFriends());
    }

    /**
     * Adaugă un utilizator la lista de prieteni a acestui utilizator.
     *
     * @param u Utilizatorul pe care dorim să-l adăugăm ca prieten.
     */
    public void addFriend(User u){
        friends.put(u.getId(),u);
    }

    /**
     * Elimină un utilizator din lista de prieteni a acestui utilizator.
     *
     * @param u Utilizatorul pe care dorim să-l eliminăm din lista de prieteni.
     * @return true dacă utilizatorul a fost eliminat cu succes, false în caz contrar.
     */
    public boolean removeFriend(User u){
        return friends.remove(u.getId()) != null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getFirstName(), getAge(), getId(), getLastName(), getFriends());
    }
}
