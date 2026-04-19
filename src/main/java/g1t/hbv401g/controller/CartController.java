package g1t.hbv401g.controller;

import g1t.hbv401g.model.Cart;
import g1t.hbv401g.model.Trip;
import g1t.hbv401g.model.User;

import java.util.Collections;
import java.util.List;

public class CartController {

    public boolean addTrip(User user, Trip trip) {
        if (user == null || trip == null || !trip.hasComponent()) return false;
        return user.getCart().addTrip(trip);
    }

    public boolean removeTrip(User user, Trip trip) {
        if (user == null || trip == null) return false;
        return user.getCart().removeTrip(trip);
    }

    public boolean removeComponent(User user, Trip trip, Object component) {
        if (user == null || trip == null || component == null) return false;
        Cart cart = user.getCart();
        if (!cart.getTrips().contains(trip)) return false;
        boolean removed = trip.removeComponent(component);
        if (removed && !trip.hasComponent()) cart.removeTrip(trip);
        return removed;
    }

    public List<Trip> getTrips(User user) {
        if (user == null) return Collections.emptyList();
        return user.getCart().getTrips();
    }

    public void clear(User user) {
        if (user != null) user.getCart().clear();
    }
}
