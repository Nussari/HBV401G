package g1t.teamD.controller;

import g1t.teamD.db.DayTripBookingDB;
import g1t.teamD.db.DayTripDB;

import java.sql.SQLException;
import g1t.teamD.model.DayTripBooking;
import g1t.teamD.model.DayTripBookingResult;
import g1t.teamD.model.DayTrip;

public class DayTripBookingController {

    private final DayTripBookingDB bookingDB;
    private final DayTripDB tripDB;

    /**
     * @param bookingDB the database instance to use for booking operations
     * @param tripDB    the database instance to use for trip lookups and updates
     */
    public DayTripBookingController(DayTripBookingDB bookingDB, DayTripDB tripDB) {
        this.bookingDB = bookingDB;
        this.tripDB = tripDB;
    }

    /**
     * Books a spot on a trip for the given person.
     * <p>
     * Validates all inputs before proceeding. Check {@link DayTripBookingResult#success}
     * to determine whether the booking was created, and {@link DayTripBookingResult#message}
     * for a human-readable description of the outcome.
     *
     * @param name    the full name of the person booking (must not be null or empty)
     * @param tripID  the ID of the trip to book
     * @param phoneNr the contact phone number (7-15 digits, optional leading +)
     * @param email   the contact email address
     * @return a {@link DayTripBookingResult} indicating success or failure, and the new booking ID on success
     */
    public DayTripBookingResult book(String name, int tripID, String phoneNr, String email) {
            try {
                if (tripID < 0)
                    return new DayTripBookingResult(false, "Invalid trip ID", -1);

                if (name == null || name.isEmpty())
                    return new DayTripBookingResult(false, "Name is required", -1);

                if (phoneNr == null || !phoneNr.matches("\\+?[0-9]{7,15}"))
                    return new DayTripBookingResult(false, "Invalid phone number", -1);

                if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
                    return new DayTripBookingResult(false, "Invalid email", -1);

                DayTrip trip = tripDB.selectByID(tripID);

                if (trip == null)
                    return new DayTripBookingResult(false, "Trip not found", -1);

                if (!trip.hasAvailability())
                    return new DayTripBookingResult(false, "Trip is fully booked", -1);

                DayTripBooking booking = new DayTripBooking(tripID, name, phoneNr, email);
                int bookingID = bookingDB.insert(booking);

                trip.setBookedSpaces(trip.getBookedSpaces() + 1);
                tripDB.update(trip);

                return new DayTripBookingResult(true, "Booking confirmed", bookingID);

            } catch (SQLException e) {
                return new DayTripBookingResult(false, "Booking failed", -1);
            }
    }
}