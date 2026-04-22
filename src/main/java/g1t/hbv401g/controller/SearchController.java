package g1t.hbv401g.controller;

import g1t.hbv401g.db.Database;
import g1t.teamD.controller.DayTripController;
import g1t.teamD.db.DayTripDB;
import g1t.teamD.model.DayTrip;

import g1t.teamF.controller.FlightSearchController;
import g1t.teamF.model.Airport;
import g1t.teamF.model.Flight;
import g1t.teamF.db.FlightDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SearchController {

    // private FlightSearchController flightSearchController; - setja inn þegar lið F skilar
    // private HotelSearchController hotelSearchController; - setja inn þegar lið H skilar
    private final DayTripController dayTripController;
    private final FlightSearchController flightSearchController;

    public SearchController(DayTripController dayTripController, FlightSearchController flightSearchController) {
        this.dayTripController = dayTripController;
        this.flightSearchController = flightSearchController;
    }

    public SearchController() {
        DayTripController ctrl = null;
        FlightSearchController fctrl = null;
        try {
            ctrl = new DayTripController(new DayTripDB(Database.teamD()));
        } catch (SQLException e) {
            System.err.println("[search] team D DayTripDB init failed: " + e.getMessage());
        }
        try {
            fctrl = new FlightSearchController(new FlightDAO());
        } catch (Exception e) {
            System.err.println("[search] team D DayTripDB init failed: " + e.getMessage());
        }
        this.dayTripController = ctrl;
        this.flightSearchController = fctrl;
    }

    public record SearchResult(List<Flight> flights, List<DayTrip> dayTrips) {}

    // ná í alla staði fyrir view dropdowns
    public List<String> getPlaces() {
        //return new ArrayList<>();
        return List.of("Reykjavik", "Copenhagen", "Stockholm", "Tokyo", "New York");
    }

    // heildar search
    public SearchResult search(String originPlace,
                                String destinationPlace,
                                LocalDate startDate,
                                LocalDate endDate,
                                double priceMin,
                                double priceMax,
                                int travellerAmount,
                                boolean searchFlights,
                                boolean searchDayTrips,
                                boolean searchHotels) {

        List<Flight> flightResults = new ArrayList<>();
        List<DayTrip> dayTripResults = new ArrayList<>();

        if (searchFlights) {
            flightResults.addAll(searchFlightsByPlace(originPlace, destinationPlace,
                    startDate, endDate, priceMin, priceMax, travellerAmount));
        }

        if (searchDayTrips) {
            dayTripResults.addAll(searchDayTrips(destinationPlace, travellerAmount,
                    startDate, endDate, priceMin, priceMax));
        }

        if (searchHotels) {
            // seinna
        }

        return new SearchResult(flightResults, dayTripResults);
    }

    // Flight search - sérhannað til að geta leiðað eftir borgum, ekki flugvöllum
    public List<Flight> searchFlightsByPlace(String originPlace,
                                            String destinationPlace,
                                            LocalDate startDate,
                                            LocalDate endDate,
                                            double priceMin,
                                            double priceMax,
                                            int travellerAmount) {
        /*
        List<Airport> originAirports = findAirportsByPlace(originPlace);
        List<Airport> destinationAirports = findAirportsByPlace(destinationPlace);

        List<Flight> results = new ArrayList<>();

        // flug út
        if (startDate != null) {
            for (Airport dep : originAirports) {
                for (Airport arr : destinationAirports) {
                    results.addAll(flightSearchController.searchFlights(
                            dep, arr, startDate, travellerAmount));
                }
            }
        }

        // flug heim
        if (endDate != null) {
            for (Airport dep : destinationAirports) {
                for (Airport arr : originAirports) {
                    results.addAll(flightSearchController.searchFlights(
                            dep, arr, endDate, travellerAmount));
                }
            }
        }

        results.removeIf(f -> f.getPrice() < priceMin || f.getPrice() > priceMax); // price filter

        return results;
        */
        Airport orgAirport = flightSearchController.findAirportByPlace(originPlace);
        Airport destAirport = flightSearchController.findAirportByPlace(destinationPlace);
        List<Flight> flightsToFrom = flightSearchController.searchFlights(orgAirport, destAirport, endDate, travellerAmount);
        ArrayList<Flight> returnFlights = new ArrayList<>();
        for (Flight flight : flightsToFrom) {
            if (flight.getPrice() <= priceMax || flight.getPrice() >= priceMin){
                returnFlights.add(flight);
            }
        }
        return returnFlights;
    }

    // helper fall sem mappar place og airport svo hægt sé að leita eftir borgum
    /*
    public List<Airport> findAirportsByPlace(String place) {
        List<Airport> matched = new ArrayList<>();
        for (Airport a : flightSearchController.findAllAirports()) {
            if (a.getPlace().equalsIgnoreCase(place)) {
                matched.add(a);
            }
        }
        return matched;
    }
    */

    public List<DayTrip> searchDayTrips(String place, int spacesNeeded,
                                        LocalDate startDate, LocalDate endDate,
                                        double priceMin, double priceMax) {
        if (dayTripController == null) return new ArrayList<>();
        try {
            List<DayTrip> trips = dayTripController.search(null, place, spacesNeeded);
            List<DayTrip> filtered = new ArrayList<>();
            for (DayTrip t : trips) {
                LocalDate d = t.getDate();
                if (startDate != null && d.isBefore(startDate)) continue;
                if (endDate != null && d.isAfter(endDate)) continue;
                if (t.getPrice() < priceMin || t.getPrice() > priceMax) continue;
                filtered.add(t);
            }
            return filtered;
        } catch (SQLException e) {
            System.err.println("[search] day-trip search failed: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Hotel search
    public List<String> searchHotels(String location, LocalDate checkIn, LocalDate checkOut) {
        return new ArrayList<>();
    }
}
