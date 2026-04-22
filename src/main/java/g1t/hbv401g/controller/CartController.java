package g1t.hbv401g.controller;

import g1t.hbv401g.model.Cart;
import g1t.hbv401g.model.HotelSelection;
import g1t.hbv401g.model.Trip;
import g1t.hbv401g.model.User;
import g1t.teamD.model.DayTrip;
import g1t.teamF.model.Flight;

import java.util.Collections;
import java.util.List;

public class CartController {

    public boolean addTrip(User user, Trip trip) {
        if (user == null || trip == null || !trip.hasComponent()) return false;
        return user.getCart().addTrip(trip);
    }

    public boolean addSelection(User user, List<Flight> flights, List<DayTrip> dayTrips, HotelSelection hotel) {
        if (user == null) return false;
        boolean hasFlights = flights != null && !flights.isEmpty();
        boolean hasDayTrips = dayTrips != null && !dayTrips.isEmpty();
        if (!hasFlights && !hasDayTrips && hotel == null) return false;
        Trip trip = new Trip(buildDefaultName(flights, dayTrips, hotel));
        if (hasFlights) for (Flight f : flights) trip.addFlight(f);
        if (hasDayTrips) for (DayTrip d : dayTrips) trip.addDayTrip(d);
        if (hotel != null && !trip.setHotel(hotel)) return false;
        return addTrip(user, trip);
    }

    private String buildDefaultName(List<Flight> flights, List<DayTrip> dayTrips, HotelSelection hotel) {
        String dest = null;
        if (dayTrips != null && !dayTrips.isEmpty()) dest = dayTrips.get(0).getPlace();
        if ((dest == null || dest.isBlank()) && hotel != null) dest = hotel.getPlace();
        if ((dest == null || dest.isBlank()) && flights != null && !flights.isEmpty()) {
            dest = flights.get(0).getArrivalAirport().getPlace();
        }
        return (dest == null || dest.isBlank()) ? "Trip" : "Trip to " + dest;
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

    public void renameTrip(Trip trip, String name) {
        if (trip == null) return;
        if (name == null || name.isBlank() || name.equals(trip.getDefaultName())) {
            trip.setCustomName(null);
        } else {
            trip.setCustomName(name);
        }
    }
}
