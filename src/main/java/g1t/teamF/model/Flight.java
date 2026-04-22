package g1t.teamF.model;

import java.time.LocalDateTime;


// temp
public class Flight {

    public double getPrice() { return 0; }
    public String getName() { return ""; }
    public String getFlightID() { return ""; }
    public Airport getDepartureA() { return new Airport(); }
    public Airport getArrivalA() { return new Airport(); }
    public LocalDateTime getDepartureTime() { return LocalDateTime.now(); }
    public LocalDateTime getArrivalTime() { return LocalDateTime.now(); }
    public String getDuration() { return ""; }
}
