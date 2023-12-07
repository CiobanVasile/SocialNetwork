package com.example.laborator7.Domain;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Clasa Friendship reprezintă o relație de prietenie între doi utilizatori.
 */
public class Friendship extends Entity<UUID> {
    private LocalDateTime date;
    private User user1;
    private User user2;
    private FriendRequest friendRequest;

    /**
     * Constructor pentru inițializarea unei relații de prietenie.
     *
     * @param user1 Primul utilizator implicat în relație.
     * @param user2 Al doilea utilizator implicat în relație.
     */
    public Friendship(User user1, User user2,LocalDateTime l) {
        this.user1 = user1;
        this.user2 = user2;
        this.setId(UUID.randomUUID()); // Generare automată a unui ID UUID pentru relația de prietenie.
        this.date = l; // Setarea datei la momentul actual.
        this.friendRequest = FriendRequest.PENDING;
    }
    public Friendship(User user1, User user2,LocalDateTime l,FriendRequest friendRequest) {
        this.user1 = user1;
        this.user2 = user2;
        this.setId(UUID.randomUUID()); // Generare automată a unui ID UUID pentru relația de prietenie.
        this.date = l; // Setarea datei la momentul actual.
        this.friendRequest = friendRequest;
    }
    /**
     * Actualizează data relației de prietenie.
     *
     * @param date Noua dată a relației de prietenie.
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * Returnează primul utilizator implicat în relația de prietenie.
     */
    public User getUser1() {
        return user1;
    }

    /**
     * Actualizează primul utilizator implicat în relația de prietenie.
     *
     * @param user1 Noul utilizator implicat în relația de prietenie.
     */
    public void setUser1(User user1) {
        this.user1 = user1;
    }

    /**
     * Returnează al doilea utilizator implicat în relația de prietenie.
     */
    public User getUser2() {
        return user2;
    }

    /**
     * Actualizează al doilea utilizator implicat în relația de prietenie.
     *
     * @param user2 Noul utilizator implicat în relația de prietenie.
     */
    public void setUser2(User user2) {
        this.user2 = user2;
    }

    @Override
    public String toString() {
        // Returnează o reprezentare sub formă de șir a obiectului Friendship.
        return "Friendship{" +
                "date=" + date +
                ", user1=" + user1 +
                ", user2=" + user2 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Friendship that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getDate(), that.getDate()) && Objects.equals(getUser1(), that.getUser1()) && Objects.equals(getUser2(), that.getUser2());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDate(), getUser1(), getUser2());
    }

    /**
     * Returnează data relației de prietenie.
     */
    public LocalDateTime getDate() {
        return this.date;
    }

    public String getAcceptance() {
        return this.friendRequest.toString();
    }
}
