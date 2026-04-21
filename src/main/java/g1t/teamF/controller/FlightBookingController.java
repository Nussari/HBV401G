package g1t.teamF.controller;

import g1t.teamF.model.*;
import g1t.teamF.db.IFBookingDAO;

/**
 * @author Eva Guðrún Jónsdóttir (@hi.is)
 * @author Hera Huld Gunnlaugsdóttir (@hi.is)
 * @author Kristín Kolka Björnsdóttir (@hi.is)
 * @author Sigríður H. Halldórsdóttir (shh60@hi.is)
 */
public class FlightBookingController implements IFFlightBookingController {

    private final IFBookingDAO bookingDAO;

    public FlightBookingController(IFBookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
    }

    public Booking createBooking(Flight outboundF, Flight returnF, int passengerCount) {
        if (passengerCount <= 0 || outboundF == null) return null;

        return new Booking(outboundF, returnF, passengerCount);
    }

    public boolean chooseSeat(Booking booking, Flight flight, Seat seat) {
        if (seat.getStatus() != SeatStatus.AVAILABLE) return false;
        if (booking.getSeatsForFlight(flight).size() >= booking.getPassengerCount()) return false;

        booking.addSeatToFlight(flight, seat);
        seat.reserve();

        return true;
    }

    public boolean removeSeat(Booking booking, Flight flight, Seat seat) {
        if (!booking.getSeatsForFlight(flight).contains(seat)) return false;

        booking.removeSeatFromFlight(flight, seat);
        seat.release();

        return true;
    }

    public boolean confirmBooking(Booking booking) {
        if (!booking.validate()) return false;
        if (!bookingDAO.save(booking)) return false;

        for (Seat seat : booking.getSeats()) {
            seat.book();
        }
        booking.confirm();

        return true;
    }

    public boolean cancelBooking(String bookingReference) {
        Booking booking = bookingDAO.findByReference(bookingReference);

        if (booking == null) return false;
        if (booking.getStatus() != BookingStatus.CONFIRMED) return false;

        for (Seat seat : booking.getSeats()) {
            seat.release();
        }
        booking.cancel();

        return true;
    }
}
