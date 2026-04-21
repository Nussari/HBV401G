package g1t.teamF.controller;

import g1t.teamF.model.Airport;
import g1t.teamF.model.Flight;
import g1t.teamF.db.IFFlightDAO;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Eva Guðrún Jónsdóttir (@hi.is)
 * @author Hera Huld Gunnlaugsdóttir (@hi.is)
 * @author Kristín Kolka Björnsdóttir (@hi.is)
 * @author Sigríður H. Halldórsdóttir (shh60@hi.is)
 */
public class FlightSearchController implements IFFlightSearchController {

    private final IFFlightDAO flightDAO;

    public FlightSearchController(IFFlightDAO flightDAO) {
        this.flightDAO = flightDAO;
    }

    public List<Airport> findAllAirports() {
        return flightDAO.getAirports();
    }

    public Airport findAirportByPlace(String place) {
        for (Airport airport : findAllAirports()) {
            if (airport.getPlace().equalsIgnoreCase(place)) {
                return airport;
            }
        }
        return null;
    }

    public List<LocalDate> findDates(Airport departureA, Airport arrivalA) {
        return flightDAO.findDatesForFlights(departureA, arrivalA);
    }

    public List<Flight> searchFlights(Airport departureA, Airport arrivalA, LocalDate date, int passengerCount) {
        return flightDAO.findFlights(departureA, arrivalA, date, passengerCount);
    }
}
