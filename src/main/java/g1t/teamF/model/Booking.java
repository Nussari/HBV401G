package g1t.teamF.model;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Eva Guðrún Jónsdóttir (@hi.is)
 * @author Hera Huld Gunnlaugsdóttir (@hi.is)
 * @author Kristín Kolka Björnsdóttir (@hi.is)
 * @author Sigríður H. Halldórsdóttir (shh60@hi.is)
 */
public class Booking {

    private final String bookingReference;
    private final Flight outboundFlight;
    private final Flight returnFlight;
    private final int passengerCount;
    private final Map<Flight, List<Seat>> seatsForFlight;
    private BookingStatus status;
    private LocalDateTime confirmedAt;
    private final LocalDateTime expiresAt;
    private final int price;

    public Booking(Flight outboundFlight, Flight returnFlight, int passengerCount) {
        this.outboundFlight = outboundFlight;
        this.returnFlight = returnFlight;
        this.passengerCount = passengerCount;

        bookingReference = UUID.randomUUID().toString();
        status = BookingStatus.PENDING;
        expiresAt = LocalDateTime.now().plusMinutes(30);
        price = calculatePrice();

        seatsForFlight = new HashMap<>();
        seatsForFlight.put(outboundFlight, new ArrayList<>());
        if (returnFlight != null) {
            seatsForFlight.put(returnFlight, new ArrayList<>());
        }
    }

    public boolean validate() {
        if (LocalDateTime.now().isAfter(expiresAt)) return false;
        for (List<Seat> seats : seatsForFlight.values()) {
            if (seats.size() != passengerCount) return false;
        }

        return true;
    }

    public void confirm() {
        this.status = BookingStatus.CONFIRMED;
        this.confirmedAt = LocalDateTime.now();
    }

    public void cancel() {
        status = BookingStatus.CANCELLED;
    }

    private int calculatePrice() {
        int pricePerPassenger;

        if (returnFlight == null) {
            pricePerPassenger = outboundFlight.getPrice();
        } else {
            pricePerPassenger = outboundFlight.getPrice() + returnFlight.getPrice();
        }

        return pricePerPassenger * passengerCount;
    }

    public void addSeatToFlight(Flight flight, Seat seat) {
        seatsForFlight.get(flight).add(seat);
    }

    public void removeSeatFromFlight(Flight flight, Seat seat) {
        seatsForFlight.get(flight).remove(seat);
    }

    public List<Seat> getSeats() {
        List<Seat> allSeats = new ArrayList<>();
        for (List<Seat> seats : seatsForFlight.values()) {
            allSeats.addAll(seats);
        }

        return allSeats;
    }

    public List<Seat> getSeatsForFlight(Flight flight) { return seatsForFlight.get(flight); }

    public String getBookingReference() { return bookingReference; }

    public Flight getOutboundFlight() { return outboundFlight; }

    public Flight getReturnFlight() { return returnFlight; }

    public BookingStatus getStatus() { return status; }

    public LocalDateTime getConfirmedAt() { return confirmedAt; }

    public LocalDateTime getExpiresAt() { return expiresAt; }

    public int getPrice() { return price; }

    public int getPassengerCount() { return passengerCount; }
}
