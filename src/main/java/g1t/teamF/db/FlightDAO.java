package g1t.teamF.db;

import g1t.teamF.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Eva Guðrún Jónsdóttir (@hi.is)
 * @author Hera Huld Gunnlaugsdóttir (@hi.is)
 * @author Kristín Kolka Björnsdóttir (@hi.is)
 * @author Sigríður H. Halldórsdóttir (shh60@hi.is)
 */
public class FlightDAO implements IFFlightDAO {

    private final List<Flight> flights = new ArrayList<>();
    private final List<Airport> airports = new ArrayList<>();

    public FlightDAO() {
        makeAirports();
        makeFlights();
    }

    private void makeAirports() {
        airports.add(new Airport("VEY", "Vestmannaeyjar", "Iceland"));
        airports.add(new Airport("RKV", "Reykjavík", "Iceland"));
        airports.add(new Airport("IFJ", "Ísafjörður", "Iceland"));
        airports.add(new Airport("HFN", "Höfn", "Iceland"));
        airports.add(new Airport("EGS", "Egilsstaðir", "Iceland"));
        airports.add(new Airport("AEY", "Akureyri", "Iceland"));
    }

    public List<Airport> getAirports() {
        return airports;
    }

    private void makeFlights() {
        Airport VEY = airports.get(0);
        Airport RKV = airports.get(1);
        Airport IFJ = airports.get(2);
        Airport HFN = airports.get(3);
        Airport EGS = airports.get(4);
        Airport AEY = airports.get(5);

        int flightID = 0;

        flights.add(new Flight(flightID++, "OG101", RKV, AEY, LocalDateTime.of(2025, 6, 1, 7, 0),
                LocalDateTime.of(2025, 6, 1, 7, 45), 12990, 3000));
        flights.add(new Flight(flightID++, "OG102", AEY, RKV, LocalDateTime.of(2025, 6, 1, 9, 0),
                LocalDateTime.of(2025, 6, 1, 9, 45), 12990, 3000));
        flights.add(new Flight(flightID++, "OG103", RKV, AEY, LocalDateTime.of(2025, 6, 2, 7, 0),
                LocalDateTime.of(2025, 6, 2, 7, 45), 12990, 3000));
        flights.add(new Flight(flightID++, "OG104", AEY, RKV, LocalDateTime.of(2025, 6, 2, 9, 0),
                LocalDateTime.of(2025, 6, 2, 9, 45), 12990, 3000));
        flights.add(new Flight(flightID++, "OG105", RKV, AEY, LocalDateTime.of(2025, 6, 3, 7, 0),
                LocalDateTime.of(2025, 6, 3, 7, 45), 12990, 3000));
        flights.add(new Flight(flightID++, "OG106", AEY, RKV, LocalDateTime.of(2025, 6, 3, 9, 0),
                LocalDateTime.of(2025, 6, 3, 9, 45), 12990, 3000));
        flights.add(new Flight(flightID++, "OG201", RKV, IFJ, LocalDateTime.of(2025, 6, 1, 8, 0),
                LocalDateTime.of(2025, 6, 1, 8, 45), 14990, 3000));
        flights.add(new Flight(flightID++, "OG202", IFJ, RKV, LocalDateTime.of(2025, 6, 1, 10, 0),
                LocalDateTime.of(2025, 6, 1, 10, 45), 14990, 3000));
        flights.add(new Flight(flightID++, "OG203", RKV, IFJ, LocalDateTime.of(2025, 6, 2, 8, 0),
                LocalDateTime.of(2025, 6, 2, 8, 45), 14990, 3000));
        flights.add(new Flight(flightID++, "OG204", IFJ, RKV, LocalDateTime.of(2025, 6, 2, 10, 0),
                LocalDateTime.of(2025, 6, 2, 10, 45), 14990, 3000));
        flights.add(new Flight(flightID++, "OG301", RKV, VEY, LocalDateTime.of(2025, 6, 1, 10, 0),
                LocalDateTime.of(2025, 6, 1, 10, 30), 9990, 2500));
        flights.add(new Flight(flightID++, "OG302", VEY, RKV, LocalDateTime.of(2025, 6, 1, 11, 30),
                LocalDateTime.of(2025, 6, 1, 12, 0), 9990, 2500));
        flights.add(new Flight(flightID++, "OG303", RKV, VEY, LocalDateTime.of(2025, 6, 3, 10, 0),
                LocalDateTime.of(2025, 6, 3, 10, 30), 9990, 2500));
        flights.add(new Flight(flightID++, "OG304", VEY, RKV, LocalDateTime.of(2025, 6, 3, 11, 30),
                LocalDateTime.of(2025, 6, 3, 12, 0), 9990, 2500));
        flights.add(new Flight(flightID++, "OG401", RKV, EGS, LocalDateTime.of(2025, 6, 1, 11, 0),
                LocalDateTime.of(2025, 6, 1, 12, 0), 15990, 3000));
        flights.add(new Flight(flightID++, "OG402", EGS, RKV, LocalDateTime.of(2025, 6, 1, 13, 0),
                LocalDateTime.of(2025, 6, 1, 14, 0), 15990, 3000));
        flights.add(new Flight(flightID++, "OG403", RKV, EGS, LocalDateTime.of(2025, 6, 4, 11, 0),
                LocalDateTime.of(2025, 6, 4, 12, 0), 15990, 3000));
        flights.add(new Flight(flightID++, "OG404", EGS, RKV, LocalDateTime.of(2025, 6, 4, 13, 0),
                LocalDateTime.of(2025, 6, 4, 14, 0), 15990, 3000));
        flights.add(new Flight(flightID++, "OG501", RKV, HFN, LocalDateTime.of(2025, 6, 2, 12, 0),
                LocalDateTime.of(2025, 6, 2, 13, 0), 15990, 3000));
        flights.add(new Flight(flightID++, "OG502", HFN, RKV, LocalDateTime.of(2025, 6, 2, 14, 0),
                LocalDateTime.of(2025, 6, 2, 15, 0), 15990, 3000));
        flights.add(new Flight(flightID++, "OG503", RKV, HFN, LocalDateTime.of(2025, 6, 5, 12, 0),
                LocalDateTime.of(2025, 6, 5, 13, 0), 15990, 3000));
        flights.add(new Flight(flightID++, "OG504", HFN, RKV, LocalDateTime.of(2025, 6, 5, 14, 0),
                LocalDateTime.of(2025, 6, 5, 15, 0), 15990, 3000));
        flights.add(new Flight(flightID++, "OG601", AEY, EGS, LocalDateTime.of(2025, 6, 2, 14, 0),
                LocalDateTime.of(2025, 6, 2, 14, 30), 9990, 2500));
        flights.add(new Flight(flightID++, "OG602", EGS, AEY, LocalDateTime.of(2025, 6, 2, 15, 30),
                LocalDateTime.of(2025, 6, 2, 16, 0), 9990, 2500));
        flights.add(new Flight(flightID++, "OG603", AEY, EGS, LocalDateTime.of(2025, 6, 4, 14, 0),
                LocalDateTime.of(2025, 6, 4, 14, 30), 9990, 2500));
        flights.add(new Flight(flightID++, "OG604", EGS, AEY, LocalDateTime.of(2025, 6, 4, 15, 30),
                LocalDateTime.of(2025, 6, 4, 16, 0), 9990, 2500));
        flights.add(new Flight(flightID++, "OG701", VEY, IFJ, LocalDateTime.of(2025, 6, 2, 13, 0),
                LocalDateTime.of(2025, 6, 2, 13, 45), 11990, 2500));
        flights.add(new Flight(flightID++, "OG702", IFJ, VEY, LocalDateTime.of(2025, 6, 2, 14, 30),
                LocalDateTime.of(2025, 6, 2, 15, 15), 11990, 2500));
        flights.add(new Flight(flightID++, "OG703", VEY, HFN, LocalDateTime.of(2025, 6, 3, 13, 0),
                LocalDateTime.of(2025, 6, 3, 14, 0), 12990, 2500));
        flights.add(new Flight(flightID++, "OG704", HFN, VEY, LocalDateTime.of(2025, 6, 3, 15, 0),
                LocalDateTime.of(2025, 6, 3, 16, 0), 12990, 2500));
        flights.add(new Flight(flightID++, "OG705", VEY, EGS, LocalDateTime.of(2025, 6, 4, 10, 0),
                LocalDateTime.of(2025, 6, 4, 11, 0), 11990, 2500));
        flights.add(new Flight(flightID++, "OG706", EGS, VEY, LocalDateTime.of(2025, 6, 4, 12, 0),
                LocalDateTime.of(2025, 6, 4, 13, 0), 11990, 2500));
        flights.add(new Flight(flightID++, "OG707", VEY, AEY, LocalDateTime.of(2025, 6, 5, 9, 0),
                LocalDateTime.of(2025, 6, 5, 10, 0), 11990, 2500));
        flights.add(new Flight(flightID++, "OG708", AEY, VEY, LocalDateTime.of(2025, 6, 5, 11, 0),
                LocalDateTime.of(2025, 6, 5, 12, 0), 11990, 2500));
        flights.add(new Flight(flightID++, "OG709", IFJ, HFN, LocalDateTime.of(2025, 6, 3, 8, 0),
                LocalDateTime.of(2025, 6, 3, 9, 0), 13990, 3000));
        flights.add(new Flight(flightID++, "OG710", HFN, IFJ, LocalDateTime.of(2025, 6, 3, 10, 0),
                LocalDateTime.of(2025, 6, 3, 11, 0), 13990, 3000));
        flights.add(new Flight(flightID++, "OG711", IFJ, EGS, LocalDateTime.of(2025, 6, 4, 9, 0),
                LocalDateTime.of(2025, 6, 4, 10, 0), 13990, 3000));
        flights.add(new Flight(flightID++, "OG712", EGS, IFJ, LocalDateTime.of(2025, 6, 4, 11, 0),
                LocalDateTime.of(2025, 6, 4, 12, 0), 13990, 3000));
        flights.add(new Flight(flightID++, "OG713", IFJ, AEY, LocalDateTime.of(2025, 6, 5, 8, 0),
                LocalDateTime.of(2025, 6, 5, 8, 45), 11990, 2500));
        flights.add(new Flight(flightID++, "OG714", AEY, IFJ, LocalDateTime.of(2025, 6, 5, 9, 30),
                LocalDateTime.of(2025, 6, 5, 10, 15), 11990, 2500));
        flights.add(new Flight(flightID++, "OG715", HFN, EGS, LocalDateTime.of(2025, 6, 1, 15, 0),
                LocalDateTime.of(2025, 6, 1, 16, 0), 13990, 3000));
        flights.add(new Flight(flightID++, "OG716", EGS, HFN, LocalDateTime.of(2025, 6, 1, 17, 0),
                LocalDateTime.of(2025, 6, 1, 18, 0), 13990, 3000));
        flights.add(new Flight(flightID++, "OG717", HFN, AEY, LocalDateTime.of(2025, 6, 2, 16, 0),
                LocalDateTime.of(2025, 6, 2, 17, 0), 12990, 3000));
        flights.add(new Flight(flightID++, "OG718", AEY, HFN, LocalDateTime.of(2025, 6, 2, 18, 0),
                LocalDateTime.of(2025, 6, 2, 19, 0), 12990, 3000));

        for (Flight flight : flights) {
            makeSeats(flight);
        }

        Flight fullFlight = flights.get(0);
        List<Seat> seats = fullFlight.getSeats();
        for (int i = 0; i < 30; i++) {
            seats.get(i).book();
        }
    }

    public List<Flight> getFlights() {
        return flights;
    }

    private void makeSeats(Flight flight) {
        String[] letters = {"A", "B", "C", "D"};
        for (int row = 1; row <= 8; row++) {
            for (String letter : letters) {
                flight.addSeat(new Seat(row + letter));
            }
        }
    }

    public List<LocalDate> findDatesForFlights(Airport departureAirport, Airport arrivalAirport) {
        List<LocalDate> dates = new ArrayList<>();
        for (Flight flight : flights) {
            if (flight.getDepartureAirport().equals(departureAirport) &&
                    flight.getArrivalAirport().equals(arrivalAirport)) {
                dates.add(flight.getDepartureDate());
            }
        }

        return dates;
    }

    public List<Flight> findFlights(Airport departureA, Airport arrivalA, LocalDate date, int passengerCount) {
        List<Flight> flights = new ArrayList<>();
        for (Flight flight : this.flights) {
            if (flight.getDepartureAirport().equals(departureA) &&
                    flight.getArrivalAirport().equals(arrivalA) &&
                    flight.getDepartureDate().equals(date) &&
                    flight.getAvailableSeatsCount() >= passengerCount) {
                flights.add(flight);
            }
        }

        return flights;
    }
}
