package g1t.teamF.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Eva Guðrún Jónsdóttir (@hi.is)
 * @author Hera Huld Gunnlaugsdóttir (@hi.is)
 * @author Kristín Kolka Björnsdóttir (@hi.is)
 * @author Sigríður H. Halldórsdóttir (shh60@hi.is)
 */
public class Flight {

    private final int flightID;
    private final String flightNumber;
    private final Airport departureAirport;
    private final Airport arrivalAirport;
    private final LocalDateTime departureTime;
    private final LocalDateTime arrivalTime;
    private final List<Seat> seats;
    private final int price;
    private final int changeFee;
    private final Duration duration;

    public Flight(int flightID, String flightNumber, Airport departureAirport, Airport arrivalAirport,
                  LocalDateTime departureTime, LocalDateTime arrivalTime, int price, int changeFee) {
        this.flightID = flightID;
        this.flightNumber = flightNumber;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.seats = new ArrayList<>();
        this.price = price;
        this.changeFee = changeFee;
        this.duration = Duration.between(departureTime, arrivalTime);
    }

    public void addSeat(Seat seat) {
        seats.add(seat);
    }

    public List<Seat> getAvailableSeats() {
        List<Seat> availableSeats = new ArrayList<>();
        for (Seat seat : seats) {
            if (seat.getStatus() == SeatStatus.AVAILABLE) {
                availableSeats.add(seat);
            }
        }

        return availableSeats;
    }

    public int getAvailableSeatsCount() { return getAvailableSeats().size(); }

    public int getFlightID() { return flightID; }

    public String getFlightNumber() { return flightNumber; }

    public Airport getDepartureAirport() { return departureAirport; }

    public Airport getArrivalAirport() { return arrivalAirport; }

    public LocalDate getDepartureDate() { return departureTime.toLocalDate(); }

    public LocalTime getDepartureTime() { return departureTime.toLocalTime(); }

    public LocalDate getArrivalDate() { return arrivalTime.toLocalDate(); }

    public LocalTime getArrivalTime() { return arrivalTime.toLocalTime(); }

    public List<Seat> getSeats() { return seats; }

    public int getPrice() { return price; }

    public int getChangeFee() { return changeFee; }

    public Duration getDuration() { return duration; }
}
