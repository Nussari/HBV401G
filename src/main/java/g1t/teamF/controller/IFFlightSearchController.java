package g1t.teamF.controller;

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
public interface IFFlightSearchController {

    /**
     * Returns all airports available in the system.
     *
     * @return list of all {@link Airport}s
     */
    List<Airport> findAllAirports();

    /**
     * Returns the airport in the given place.
     *
     * @param place the city name
     * @return the matching {@link Airport}, or {@code null} if not found
     */
    Airport findAirportByPlace(String place);


    /**
     * Returns all dates on which a flight operates from {@code departureA} to {@code arrivalA}.
     *
     * @param departureA the departure airport
     * @param arrivalA the arrival airport
     * @return list of available departure dates
     */
    List<LocalDate> findDates(Airport departureA, Airport arrivalA);

    /**
     * Returns all flights from {@code departureA} to {@code arrivalA} on the given date with at least
     * {@code passengerCount} available seats.
     *
     * @param departureA the departure airport
     * @param arrivalA the arrival airport
     * @param date the departure date
     * @param passengerCount minimum number of available seats required
     * @return list of matching {@link Flight}s
     */
    List<Flight> searchFlights(Airport departureA, Airport arrivalA, LocalDate date, int passengerCount);
}
