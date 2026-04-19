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
        validate(email, password);
        String key = email.toLowerCase();
        if (users.containsKey(key)) {
            throw new IllegalStateException("Email already registered");
        }
        User user = new User(email, password);
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
