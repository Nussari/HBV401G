package g1t.teamF.db;

import g1t.teamF.model.Booking;

/**
 * @author Eva Guðrún Jónsdóttir (@hi.is)
 * @author Hera Huld Gunnlaugsdóttir (@hi.is)
 * @author Kristín Kolka Björnsdóttir (@hi.is)
 * @author Sigríður H. Halldórsdóttir (shh60@hi.is)
 */
public interface IFBookingDAO {

    Booking findByReference(String bookingReference);

    boolean save(Booking booking);
}
