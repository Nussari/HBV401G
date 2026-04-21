package g1t.teamF.model;

/**
 * @author Eva Guðrún Jónsdóttir (@hi.is)
 * @author Hera Huld Gunnlaugsdóttir (@hi.is)
 * @author Kristín Kolka Björnsdóttir (@hi.is)
 * @author Sigríður H. Halldórsdóttir (shh60@hi.is)
 */
public class Seat {

    private final String seatNumber;
    private SeatStatus status;

    public Seat(String seatNumber) {
        this.seatNumber = seatNumber;
        this.status = SeatStatus.AVAILABLE;
    }

    public void reserve() {
        status = SeatStatus.RESERVED;
    }

    public void release() {
        status = SeatStatus.AVAILABLE;
    }

    public void book() {
        status = SeatStatus.BOOKED;
    }

    public SeatStatus getStatus() { return status; }

    public String getSeatNumber() { return seatNumber; }

    @Override
    public String toString() { return seatNumber; }
}
