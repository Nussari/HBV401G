package g1t.hbv401g.controller;

import g1t.hbv401g.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    // register

    @Test
    void registerReturnsUserWithMatchingCredentials() {
        User user = userController.register("alice@example.com", "pw123");
        assertEquals("alice@example.com", user.getEmail());
        assertEquals("pw123", user.getPassword());
    }

    @Test
    void registerCreatesEmptyCartAndBookings() {
        User user = userController.register("alice@example.com", "pw123");
        assertNotNull(user.getCart());
        assertNotNull(user.getBookings());
        assertTrue(user.getBookings().isEmpty());
    }

    @Test
    void registerRejectsDuplicateEmail() {
        userController.register("alice@example.com", "pw123");
        assertThrows(IllegalStateException.class, () ->
            userController.register("alice@example.com", "different"));
    }

    @Test
    void registerRejectsDuplicateEmailCaseInsensitive() {
        userController.register("alice@example.com", "pw123");
        assertThrows(IllegalStateException.class, () ->
            userController.register("ALICE@example.com", "pw123"));
    }

    @Test
    void registerRejectsNullEmail() {
        assertThrows(IllegalArgumentException.class, () ->
            userController.register(null, "pw123"));
    }

    @Test
    void registerRejectsBlankEmail() {
        assertThrows(IllegalArgumentException.class, () ->
            userController.register("   ", "pw123"));
    }

    @Test
    void registerRejectsEmailWithoutAt() {
        assertThrows(IllegalArgumentException.class, () ->
            userController.register("notAnEmail", "pw123"));
    }

    @Test
    void registerRejectsNullPassword() {
        assertThrows(IllegalArgumentException.class, () ->
            userController.register("alice@example.com", null));
    }

    @Test
    void registerRejectsBlankPassword() {
        assertThrows(IllegalArgumentException.class, () ->
            userController.register("alice@example.com", "   "));
    }

    // login

    @Test
    void loginReturnsRegisteredUser() {
        User registered = userController.register("alice@example.com", "pw123");
        User loggedIn = userController.login("alice@example.com", "pw123");
        assertSame(registered, loggedIn);
    }

    @Test
    void loginIsCaseInsensitiveOnEmail() {
        userController.register("alice@example.com", "pw123");
        assertDoesNotThrow(() ->
            userController.login("ALICE@EXAMPLE.COM", "pw123"));
    }

    @Test
    void loginRejectsWrongPassword() {
        userController.register("alice@example.com", "pw123");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            userController.login("alice@example.com", "wrong"));
        assertEquals("Invalid email or password", ex.getMessage());
    }

    @Test
    void loginRejectsUnknownEmailWithSameMessage() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            userController.login("ghost@example.com", "pw123"));
        assertEquals("Invalid email or password", ex.getMessage());
    }

    // demo user - yrði eytt ef haldið væri áfram með verkefni

    @Test
    void seededDemoUserCanLogIn() {
        assertDoesNotThrow(() -> userController.login("demo@demo.is", "demo"));
    }
}
