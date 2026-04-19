package g1t.hbv401g.controller;

import g1t.hbv401g.model.Booking;
import g1t.hbv401g.model.DayTrip;
import g1t.hbv401g.model.DayTripBookingResult;
import g1t.hbv401g.model.Flight;
import g1t.hbv401g.model.FlightBooking;
import g1t.hbv401g.model.Trip;
import g1t.hbv401g.model.User;

import java.util.ArrayList;
import java.util.List;


public class BookingController {

    private final MockFlightBookingController flightBookingController;
    private final MockDayTripBookingController dayTripBookingController;

    public BookingController(MockFlightBookingController flightBookingController,
                             MockDayTripBookingController dayTripBookingController) {
        this.flightBookingController = flightBookingController;
        this.dayTripBookingController = dayTripBookingController;
    }

    public BookingController() {
        this(new MockFlightBookingController(), new MockDayTripBookingController());
    }

    public List<Booking> checkout(User user) {
        if (user == null) throw new IllegalStateException("Must be logged in to checkout");

        List<Booking> created = new ArrayList<>();
        List<Trip> trips = new ArrayList<>(user.getCart().getTrips());

        for (Trip trip : trips) {
            Booking booking = new Booking(trip);

            for (Flight flight : trip.getFlights()) {
                FlightBooking fb = flightBookingController.createBooking(flight, null, 1);
                if (fb != null) {
                    flightBookingController.confirmBooking(fb);
                    booking.addFlightBooking(fb);
                }
            }

            for (DayTrip dayTrip : trip.getDayTrips()) {
                DayTripBookingResult result = dayTripBookingController.book(
                        user.getUsername(),
                        dayTrip.getTripID(),
                        "+000",
                        user.getEmail());
                if (result.success) {
                    booking.addDayTripBooking(dayTripBookingController.findBooking(result.bookingID));
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
