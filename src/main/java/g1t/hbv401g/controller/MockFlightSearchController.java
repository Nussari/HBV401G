package g1t.hbv401g.controller;

import g1t.hbv401g.model.Airport;
import g1t.hbv401g.model.Flight;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MockFlightSearchController {

    private final List<Airport> airports = new ArrayList<>();
    private final List<Flight> flights = new ArrayList<>();
    private final String[] airlines = {"PáskaFlug", "IcelandAir", "FlightsForAll", "KökuFlug", "SuperFlug"};
    private final String[] airlineCodes = {"PFL", "IAR", "FFA", "KFL", "SFL"};

    public MockFlightSearchController() {
        Random r = new Random();

        airports.add(new Airport("Keflavík International Airport", "KEF", "Keflavík"));
        airports.add(new Airport("Billund Airport", "BLL", "Billund"));
        airports.add(new Airport("Copenhagen Airport, Kastrup", "CPH", "Copenhagen"));
        airports.add(new Airport("Heathrow Airport", "LHR", "London"));
        airports.add(new Airport("Glasgow Airport", "GLA", "Glasgow"));
        airports.add(new Airport("Nuuk Airport", "GOH", "Nuuk"));
        airports.add(new Airport("Berlin Brandenburg Airport", "BER", "Berlin"));

        for (int i = 0; i <= 21; i++) { // búum til random flug
            int airlineIdx = r.nextInt(5);
            String flightID = airlineCodes[airlineIdx] + (r.nextInt(9000) + 1000);
            String flightName = airlines[airlineIdx];

            Airport departure;
            Airport arrival;
            do {
                departure = airports.get(r.nextInt(airports.size()));
                arrival = airports.get(r.nextInt(airports.size()));
            } while (departure.getCode().equals(arrival.getCode()));

            LocalDate date = LocalDate.now().plusDays(i / 3);
            int hour = 6 + r.nextInt(14);  // flights between 06:00–19:59
            LocalDateTime departureTime = LocalDateTime.of(date, LocalTime.of(hour, 0));
            LocalDateTime arrivalTime = departureTime.plusHours(2 + r.nextInt(6));
            double price = 100 + r.nextInt(900);

            flights.add(new Flight(flightID, flightName, departure, arrival,
                    departureTime, arrivalTime, price, price * 0.1,
                    arrivalTime.toLocalTime().toString()));
        }
    }

    public List<Airport> findAllAirports() {
        return airports;
    }

    // public List<LocalDate> findDates(Airport departureA, Airport arrivalA) {} ekki þörf á þessu fyrir okkur

    public List<Flight> searchFlights(Airport departureA, 
                                    Airport arrivalA,
                                    LocalDate date, 
                                    int passengerCount) {
        List<Flight> results = new ArrayList<>();
        for (Flight flight : flights) {
            if (flight.getDepartureA().getCode().equals(departureA.getCode())
                    && flight.getArrivalA().getCode().equals(arrivalA.getCode())
                    && flight.getDepartureTime().toLocalDate().equals(date)) {
                results.add(flight);
            }
        }
        return results;
    }
}
