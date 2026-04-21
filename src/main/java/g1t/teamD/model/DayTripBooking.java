package g1t.teamD.model;

public class DayTripBooking {

    private final int bookingID;
    private final int tripID;
    private final String name;
    private final String phoneNr;
    private final String email;

    // For creating new bookings — ID assigned by DB
    public DayTripBooking(int tripID, String name, String phoneNr, String email) {
        this.bookingID = 0;
        this.tripID = tripID;
        this.name = name;
        this.phoneNr = phoneNr;
        this.email = email;
    }

    // For reconstructing from DB
    public DayTripBooking(int bookingID, int tripID, String name,
            String phoneNr, String email) {
        this.bookingID = bookingID;
        this.tripID = tripID;
        this.name = name;
        this.phoneNr = phoneNr;
        this.email = email;
    }

    // Getters
    public int getBookingID() {
        return bookingID;
    }

    public int getTripID() {
        return tripID;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNr() {
        return phoneNr;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "DayTripBooking{" +
                "bookingID=" + bookingID +
                ", tripID=" + tripID +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}