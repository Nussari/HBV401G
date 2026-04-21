package g1t.teamF.db;

import g1t.teamF.model.Airport;
import g1t.teamF.model.Flight;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Eva Guðrún Jónsdóttir (@hi.is)
 * @author Hera Huld Gunnlaugsdóttir (@hi.is)
 * @author Kristín Kolka Björnsdóttir (@hi.is)
 * @author Sigríður H. Halldórsdóttir (shh60@hi.is)
 */
public interface IFFlightDAO {

    List<Airport> getAirports();

    List<Flight> getFlights();

    List<LocalDate> findDatesForFlights(Airport departureAirport, Airport arrivalAirport);

    List<Flight> findFlights(Airport departureA, Airport arrivalA, LocalDate date, int passengerCount);
}
