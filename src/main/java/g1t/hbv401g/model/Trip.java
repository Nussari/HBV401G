package g1t.hbv401g.model;

import g1t.teamD.model.DayTrip;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Trip {

    private String defaultName;
    private String customName;
    private final List<Flight> flights = new ArrayList<>();
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

    public List<Flight> getFlights() { return Collections.unmodifiableList(flights); }
    public List<DayTrip> getDayTrips() { return Collections.unmodifiableList(dayTrips); }
    public HotelSelection getHotel() { return hotel; }

    public boolean addFlight(Flight flight) {
        if (flight == null || flights.contains(flight)) return false;
        if (!fitsInHotelSpan(flight)) return false;
        return flights.add(flight);
    }

    public boolean addDayTrip(DayTrip dayTrip) {
        if (dayTrip == null || dayTrips.contains(dayTrip)) return false;
        return dayTrips.add(dayTrip);
    }

    public boolean setHotel(HotelSelection selection) {
        if (selection == null) { this.hotel = null; return true; }
        if (!hotelCoversFlights(selection)) return false;
        this.hotel = selection;
        return true;
    }

    public boolean removeFlight(Flight flight) { return flights.remove(flight); }
    public boolean removeDayTrip(DayTrip dayTrip) { return dayTrips.remove(dayTrip); }
    public void removeHotel() { this.hotel = null; }

    public boolean removeComponent(Object component) {
        if (component instanceof Flight f) return removeFlight(f);
        if (component instanceof DayTrip d) return removeDayTrip(d);
        if (component instanceof HotelSelection h && h == hotel) { removeHotel(); return true; }
        return false;
    }

    public double getTotalCost() {
        double total = 0;
        for (Flight f : flights) total += f.getPrice();
        for (DayTrip d : dayTrips) total += d.getPrice();
        if (hotel != null) total += hotel.getTotalCost();
        return total;
    }

    public boolean hasComponent() {
        return !flights.isEmpty() || !dayTrips.isEmpty() || hotel != null;
    }

    public int getComponentCount() {
        return flights.size() + dayTrips.size() + (hotel != null ? 1 : 0);
    }

    // hotel must be available for the full span of every flight
    private boolean hotelCoversFlights(HotelSelection sel) {
        for (Flight f : flights) {
            LocalDate fd = f.getDepartureTime().toLocalDate();
            if (fd.isBefore(sel.getCheckIn()) || fd.isAfter(sel.getCheckOut())) return false;
        }
        return true;
    }

    private boolean fitsInHotelSpan(Flight f) {
        if (hotel == null) return true;
        LocalDate fd = f.getDepartureTime().toLocalDate();
        return !fd.isBefore(hotel.getCheckIn()) && !fd.isAfter(hotel.getCheckOut());
    }
}
