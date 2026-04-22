package g1t.hbv401g.controller;

import g1t.hbv401g.db.Database;
import g1t.hbv401g.model.Booking;
import g1t.hbv401g.model.HotelSelection;
import g1t.hbv401g.model.Trip;
import g1t.hbv401g.model.User;
import g1t.teamD.controller.DayTripBookingController;
import g1t.teamD.db.DayTripBookingDB;
import g1t.teamD.db.DayTripDB;
import g1t.teamD.model.DayTrip;
import g1t.teamD.model.DayTripBooking;
import g1t.teamD.model.DayTripBookingResult;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class BookingController {

    // private FlightBookingController flightBookingController; - setja inn þegar lið F skilar
    private final DayTripBookingController dayTripBookingController;

    public BookingController(DayTripBookingController dayTripBookingController) {
        this.dayTripBookingController = dayTripBookingController;
    }

    public BookingController() {
        DayTripBookingController ctrl = null;
        try {
            DayTripDB tripDB = new DayTripDB(Database.teamD());
            DayTripBookingDB bookingDB = new DayTripBookingDB(Database.teamD());
            ctrl = new DayTripBookingController(bookingDB, tripDB);
        } catch (SQLException e) {
            System.err.println("[booking] team D init failed: " + e.getMessage());
        }
        this.dayTripBookingController = ctrl;
    }

    public List<Booking> checkout(User user) {
        if (user == null) throw new IllegalStateException("Must be logged in to checkout");

        List<Booking> created = new ArrayList<>();
        List<Trip> trips = new ArrayList<>(user.getCart().getTrips());

        for (Trip trip : trips) {
            Booking booking = new Booking(trip);

            // Flight booking: bíður eftir F innleiðingu
            // for (Flight flight : trip.getFlights()) {
            //     FlightBooking fb = flightBookingController.createBooking(flight, null, 1);
            //     if (fb != null) {
            //         flightBookingController.confirmBooking(fb);
            //         booking.addFlightBooking(fb);
            //     }
            // }

            if (dayTripBookingController != null) {
                for (DayTrip dayTrip : trip.getDayTrips()) {
                    DayTripBookingResult result = dayTripBookingController.book(
                            user.getUsername(),
                            dayTrip.getTripID(),
                            "+0000000",
                            user.getEmail());
                    if (result.success) {
                        booking.addDayTripBooking(new DayTripBooking(
                                result.bookingID, dayTrip.getTripID(),
                                user.getUsername(), "+0000000", user.getEmail()));
                    }
                }
            }

            HotelSelection hotel = trip.getHotel();
            if (hotel != null) {
                is.hi.H1.model.Booking hBooking = new is.hi.H1.model.Booking(
                        hotel.getCheckIn(),
                        hotel.getCheckOut(),
                        hotel.getRooms(),
                        user.getEmail(),
                        false);
                try {
                    boolean ok = is.hi.H1.controllers.BookingController.createBooking(hBooking);
                    if (ok) {
                        booking.setHotelBooking(hBooking);
                    } else {
                        System.err.println("[booking] hotel booking rejected by H1");
                    }
                } catch (Exception e) {
                    System.err.println("[booking] hotel booking failed: " + e.getMessage());
                }
            }

            user.addBooking(booking);
            created.add(booking);
        }

        user.getCart().clear();
        return created;
    }

    public enum CancellationResult { CANCELLED, HOTEL_REQUIRES_PHONE, FAILED }

    public CancellationResult cancelBooking(User user, Booking booking) {
        if (user == null || booking == null) return CancellationResult.FAILED;
        if (booking.hasHotelBooking()) return CancellationResult.HOTEL_REQUIRES_PHONE;
        return user.removeBooking(booking) ? CancellationResult.CANCELLED : CancellationResult.FAILED;
    }

    public CancellationResult cancelBookingComponent(User user, Booking booking, Object component) {
        if (user == null || booking == null || component == null) return CancellationResult.FAILED;
        if (component instanceof HotelSelection) return CancellationResult.HOTEL_REQUIRES_PHONE;

        Trip trip = booking.getTrip();
        if (trip == null || !trip.removeComponent(component)) return CancellationResult.FAILED;

        if (component instanceof DayTrip dt) {
            booking.removeDayTripBookingByTripID(dt.getTripID());
        }

        if (!trip.hasComponent() && !booking.hasHotelBooking()) {
            user.removeBooking(booking);
        }
        return CancellationResult.CANCELLED;
    }
}
