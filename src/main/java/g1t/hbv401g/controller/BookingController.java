package g1t.hbv401g.controller;

import g1t.hbv401g.db.Database;
import g1t.hbv401g.model.Booking;
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
import controller.*;
import model.*;
import storage.*;


public class BookingController {

    // private FlightBookingController flightBookingController; - setja inn þegar lið F skilar
    private final DayTripBookingController dayTripBookingController;
    private final

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

            user.addBooking(booking);
            created.add(booking);
        }

        user.getCart().clear();
        return created;
    }

    public boolean cancelBooking(User user, Booking booking) {
        if (user == null || booking == null) return false;
        return user.removeBooking(booking);
    }
}
