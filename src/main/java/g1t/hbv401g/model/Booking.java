package g1t.hbv401g.model;

import g1t.teamD.model.DayTripBooking;
import g1t.teamF.model.Flight;
import g1t.teamF.model.FlightBooking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Booking {

    private final Trip trip;
    private final List<g1t.teamF.model.Booking> flightBookings = new ArrayList<>();
    private final List<DayTripBooking> dayTripBookings = new ArrayList<>();
    private is.hi.H1.model.Booking hotelBooking;

    public Booking(Trip trip) {
        this.trip = trip;
    }

    public Trip getTrip() { return trip; }
    public String getName() { return trip == null ? "" : trip.getName(); }
    public double getTotal() { return trip == null ? 0 : trip.getTotalCost(); }

    public List<g1t.teamF.model.Booking> getFlightBookings() { return Collections.unmodifiableList(flightBookings); }
    public void addFlightBooking(g1t.teamF.model.Booking booking) { if (booking != null) flightBookings.add(booking); }

    public g1t.teamF.model.Booking removeFlightBookingByOutbound(Flight flight) {
        for (int i = 0; i < flightBookings.size(); i++) {
            if (flightBookings.get(i).getOutboundFlight() == flight) {
                return flightBookings.remove(i);
            }
        }
        return null;
    }

    public List<DayTripBooking> getDayTripBookings() { return Collections.unmodifiableList(dayTripBookings); }
    public void addDayTripBooking(DayTripBooking booking) { if (booking != null) dayTripBookings.add(booking); }
    public void removeDayTripBookingByTripID(int tripID) {
        dayTripBookings.removeIf(b -> b.getTripID() == tripID);
    }

    public is.hi.H1.model.Booking getHotelBooking() { return hotelBooking; }
    public void setHotelBooking(is.hi.H1.model.Booking booking) { this.hotelBooking = booking; }
    public boolean hasHotelBooking() { return hotelBooking != null; }
}
