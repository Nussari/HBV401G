package g1t.hbv401g.model;

import g1t.teamD.model.DayTrip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Trip {

    private String defaultName;
    private String customName;
    private final List<Flight> flights = new ArrayList<>();
    private final List<DayTrip> dayTrips = new ArrayList<>();

    public Trip(String defaultName) {
        this.defaultName = defaultName;
    }

    public String getName() {
        return (customName != null && !customName.isBlank()) ? customName : defaultName;
    }

    public String getDefaultName() { return defaultName; }
    public void setDefaultName(String name) { this.defaultName = name; }

    public String getCustomName() { return customName; }
    public boolean hasCustomName() { return customName != null && !customName.isBlank(); }

    public void setCustomName(String name) {
        this.customName = (name == null || name.isBlank()) ? null : name.trim();
    }

    public List<Flight> getFlights() { return Collections.unmodifiableList(flights); }
    public List<DayTrip> getDayTrips() { return Collections.unmodifiableList(dayTrips); }

    public boolean addFlight(Flight flight) {
        if (flight == null || flights.contains(flight)) return false;
        return flights.add(flight);
    }

    public boolean addDayTrip(DayTrip dayTrip) {
        if (dayTrip == null || dayTrips.contains(dayTrip)) return false;
        return dayTrips.add(dayTrip);
    }

    public boolean removeFlight(Flight flight) { return flights.remove(flight); }
    public boolean removeDayTrip(DayTrip dayTrip) { return dayTrips.remove(dayTrip); }

    public boolean removeComponent(Object component) {
        if (component instanceof Flight f) return removeFlight(f);
        if (component instanceof DayTrip d) return removeDayTrip(d);
        return false;
    }

    public double getTotalCost() {
        double total = 0;
        for (Flight f : flights) total += f.getPrice();
        for (DayTrip d : dayTrips) total += d.getPrice();
        return total;
    }

    public boolean hasComponent() {
        return !flights.isEmpty() || !dayTrips.isEmpty();
    }

    public int getComponentCount() {
        return flights.size() + dayTrips.size();
    }
}
