package g1t.hbv401g.model;

import java.time.LocalDateTime;
import java.util.List;

public class Flight {
    private String flightID;
    private String name;
    private Airport departureA;
    private Airport arrivalA;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private double price;
    private double changeFee;
    private String duration;

    public Flight(String flightID, String name, Airport departureA, Airport arrivalA,
                  LocalDateTime departureTime, LocalDateTime arrivalTime,
                  double price, double changeFee, String duration) {
        this.flightID = flightID;
        this.name = name;
        this.departureA = departureA;
        this.arrivalA = arrivalA;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.changeFee = changeFee;
        this.duration = duration;
    }

    public String getFlightID() { return flightID; }
    public String getName() { return name; }
    public Airport getDepartureA() { return departureA; }
    public Airport getArrivalA() { return arrivalA; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public double getPrice() { return price; }
    public double getChangeFee() { return changeFee; }
    public String getDuration() { return duration; }
}
