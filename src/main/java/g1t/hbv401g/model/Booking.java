package g1t.hbv401g.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Booking {

    private final Trip trip;
    private final List<FlightBooking> flightBookings = new ArrayList<>();
    private final List<DayTripBooking> dayTripBookings = new ArrayList<>();

    public Booking(Trip trip) {
        this.trip = trip;
    }

    public Trip getTrip() { return trip; }
    public String getName() { return trip == null ? "" : trip.getName(); }
    public double getTotal() { return trip == null ? 0 : trip.getTotalCost(); }

    public List<FlightBooking> getFlightBookings() { return Collections.unmodifiableList(flightBookings); }
    public void addFlightBooking(FlightBooking booking) { if (booking != null) flightBookings.add(booking); }

    public List<DayTripBooking> getDayTripBookings() { return Collections.unmodifiableList(dayTripBookings); }
    public void addDayTripBooking(DayTripBooking booking) { if (booking != null) dayTripBookings.add(booking); }
}
