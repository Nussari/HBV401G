package g1t.hbv401g.model;

import g1t.teamD.model.DayTrip;
import g1t.teamF.model.Flight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Trip {

    private String defaultName;
    private String customName;
    private final List<DayTrip> dayTrips = new ArrayList<>();
    private HotelSelection hotel;

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

    public List<DayTrip> getDayTrips() { return Collections.unmodifiableList(dayTrips); }
    public HotelSelection getHotel() { return hotel; }


    public List<Flight> getFlights() { return Collections.emptyList(); }

    public boolean addFlight(Flight flight) { return false; }
    public boolean removeFlight(Flight flight) { return false; }

    public boolean addDayTrip(DayTrip dayTrip) {
        if (dayTrip == null || dayTrips.contains(dayTrip)) return false;
        return dayTrips.add(dayTrip);
    }

    public boolean setHotel(HotelSelection selection) {
        this.hotel = selection;
        return true;
    }

    public boolean removeDayTrip(DayTrip dayTrip) { return dayTrips.remove(dayTrip); }
    public void removeHotel() { this.hotel = null; }

    public boolean removeComponent(Object component) {
        if (component instanceof Flight f) return removeFlight(f);   // STUB FOR REMOVAL
        if (component instanceof DayTrip d) return removeDayTrip(d);
        if (component instanceof HotelSelection h && h == hotel) { removeHotel(); return true; }
        return false;
    }

    public double getTotalCost() {
        double total = 0;
        for (DayTrip d : dayTrips) total += d.getPrice();
        if (hotel != null) total += hotel.getTotalCost();
        return total;
    }

    public boolean hasComponent() {
        return !dayTrips.isEmpty() || hotel != null;
    }

    public int getComponentCount() {
        return dayTrips.size() + (hotel != null ? 1 : 0);
    }
}
