package g1t.hbv401g.controller;

import g1t.hbv401g.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserController {

    private final Map<String, User> users = new HashMap<>();

    public UserController() {
        // dummy user svo hægt sé að prufa appið, ekkert DB í þessu verkefni
        seed("demo@demo.is", "demo");
    }

    public User register(String email, String password) {
        return register(email, password, null);
    }

    public User register(String email, String password, String username) {
        validate(email, password);
        String key = email.toLowerCase();
        if (users.containsKey(key)) {
            throw new IllegalStateException("Email already registered");
        }
        User user = username == null || username.isBlank()
                ? new User(email, password)
                : new User(email, password, username);
        users.put(key, user);
        return user;
    }

    public User login(String email, String password) {
        validate(email, password);
        User user = users.get(email.toLowerCase());
        if (user == null || !user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return user;
    }

    public void updateEmail(User user, String email) {
        if (user == null) return;
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email can't be empty");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Email must contain '@'");
        }
        user.setEmail(email);
    }

    public void updateUsername(User user, String username) {
        if (user == null) return;
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username can't be empty");
        }
        user.setUsername(username);
    }

    public void changePassword(User user, String password) {
        if (user == null) return;
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password can't be empty");
        }
        user.setPassword(password);
    }

    private void validate(String email, String password) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email can't be empty");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Email must contain '@'");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password can't be empty");
        }
    }

    private void seed(String email, String password) {
        users.put(email.toLowerCase(), new User(email, password));
    }
}
