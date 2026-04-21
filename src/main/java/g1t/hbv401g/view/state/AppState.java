package g1t.hbv401g.view.state;

import g1t.hbv401g.controller.BookingController;
import g1t.hbv401g.controller.CartController;
import g1t.hbv401g.controller.UserController;
import g1t.hbv401g.model.Booking;
import g1t.hbv401g.model.Cart;
import g1t.hbv401g.model.Trip;
import g1t.hbv401g.model.User;
import g1t.teamD.model.DayTrip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// appstate notað í stað gagnagrunns þar sem gagnagrunnur er ekki partur af þessu verkefni
public final class AppState {

    private static final AppState INSTANCE = new AppState();
    public static AppState get() { return INSTANCE; }

    private final UserController userController = new UserController();
    private final CartController cartController = new CartController();
    private final BookingController bookingController = new BookingController();
    private final Cart emptyCart = new Cart();

    private User currentUser;
    private final List<Runnable> listeners = new ArrayList<>();

    private AppState() {
    }

    public UserController getUserController() { return userController; }

    public boolean isLoggedIn() { return currentUser != null; }
    public User getCurrentUser() { return currentUser; }

    public void login(String email, String password) {
        this.currentUser = userController.login(email, password);
        notifyListeners();
    }

    public void register(String email, String password, String username) {
        this.currentUser = userController.register(email, password, username);
        notifyListeners();
    }

    public void logout() {
        this.currentUser = null;
        notifyListeners();
    }

    public Cart getCart() {
        return isLoggedIn() ? currentUser.getCart() : emptyCart;
    }

    public boolean addTripToCart(Trip trip) {
        if (!isLoggedIn()) return false;
        boolean ok = cartController.addTrip(currentUser, trip);
        if (ok) notifyListeners();
        return ok;
    }

    public boolean addSelectionToCart(List<DayTrip> dayTrips) {
        if (!isLoggedIn()) return false;
        boolean ok = cartController.addSelection(currentUser, dayTrips);
        if (ok) notifyListeners();
        return ok;
    }

    public void removeFromCart(Trip trip) {
        if (!isLoggedIn()) return;
        if (cartController.removeTrip(currentUser, trip)) notifyListeners();
    }

    public void removeComponentFromTrip(Trip trip, Object component) {
        if (!isLoggedIn()) return;
        if (cartController.removeComponent(currentUser, trip, component)) notifyListeners();
    }

    public void clearCart() {
        if (!isLoggedIn()) return;
        cartController.clear(currentUser);
        notifyListeners();
    }

    public double cartSubtotal() { return getCart().getSubtotal(); }

    public List<Booking> getBookings() {
        return isLoggedIn() ? currentUser.getBookings() : Collections.emptyList();
    }

    public List<Booking> checkout() {
        if (!isLoggedIn()) throw new IllegalStateException("Must be logged in to check out");
        List<Booking> created = bookingController.checkout(currentUser);
        notifyListeners();
        return created;
    }

    public void subscribe(Runnable r) { listeners.add(r); }
    public void notifyListeners() { for (Runnable r : new ArrayList<>(listeners)) r.run(); }
}
