package g1t.hbv401g.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String email;
    private String password;
    private String username;
    private final Cart cart;
    private final List<Booking> bookings;

    public User(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.cart = new Cart();
        this.bookings = new ArrayList<>();
    }

    public User(String email, String password) {
        this(email, password, defaultUsername(email));
    }

    private static String defaultUsername(String email) {
        if (email == null) return "";
        int at = email.indexOf('@');
        return at > 0 ? email.substring(0, at) : email;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Cart getCart() { return cart; }

    public List<Booking> getBookings() { return bookings; }
    public void addBooking(Booking booking) { bookings.add(booking); }
    public boolean removeBooking(Booking booking) { return bookings.remove(booking); }
}
