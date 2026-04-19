package g1t.hbv401g.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cart {

    private final List<Trip> trips = new ArrayList<>();

    public List<Trip> getTrips() { return Collections.unmodifiableList(trips); }

    public boolean addTrip(Trip trip) {
        if (trip == null || !trip.hasComponent()) return false;
        return trips.add(trip);
    }

    public boolean removeTrip(Trip trip) {
        return trips.remove(trip);
    }

    public void clear() { trips.clear(); }

    public int size() { return trips.size(); }
    public boolean isEmpty() { return trips.isEmpty(); }

    public double getSubtotal() {
        return trips.stream().mapToDouble(Trip::getTotalCost).sum();
    }
}
