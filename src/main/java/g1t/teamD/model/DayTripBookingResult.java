package g1t.teamD.model;

public class DayTripBookingResult {
    public final boolean success;
    public final String message;
    public final int bookingID;

    public DayTripBookingResult(boolean success, String message, int bookingID) {
        this.success = success;
        this.message = message;
        this.bookingID = bookingID;
    }
}