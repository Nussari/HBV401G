package g1t.teamF.db;

import g1t.teamF.model.Booking;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eva Guðrún Jónsdóttir (@hi.is)
 * @author Hera Huld Gunnlaugsdóttir (@hi.is)
 * @author Kristín Kolka Björnsdóttir (@hi.is)
 * @author Sigríður H. Halldórsdóttir (shh60@hi.is)
 */
public class BookingDAO implements IFBookingDAO{

    private final List<Booking> bookings;

    public BookingDAO() {
        bookings = new ArrayList<>();
    }

    public Booking findByReference(String bookingReference) {
        for (Booking booking : bookings) {
            if (booking.getBookingReference().equals(bookingReference)) {
                return booking;
            }
        }

        return null;
    }

    public boolean save(Booking booking) {
        if (findByReference(booking.getBookingReference()) != null) return false;
        bookings.add(booking);

        return true;
    }
}
