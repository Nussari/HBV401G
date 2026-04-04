package g1t.hbv401g.controller;

import g1t.hbv401g.model.Airport;
import g1t.hbv401g.model.Flight;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SearchController {

    private final MockFlightSearchController flightSearchController;
    // private EventController eventController; - setja inn þegar lið D skilar
    // private HotelSearchController hotelSearchController; - setja inn þegar lið H skilar

    public SearchController(MockFlightSearchController flightSearchController) {
        this.flightSearchController = flightSearchController;
    }

    public SearchController() {
        this(new MockFlightSearchController());
    }

    // ná í alla staði fyrir view dropdowns
    public List<String> getPlaces() {
        List<String> places = new ArrayList<>();
        for (Airport a : flightSearchController.findAllAirports()) {
            if (!places.contains(a.getPlace())) {
                places.add(a.getPlace());
            }
        }
        return places;
    }

    // heildar search
    public List<Flight> search(String originPlace, 
                                String destinationPlace,
                                LocalDate startDate, 
                                LocalDate endDate,
                                double priceMin, 
                                double priceMax,
                                int travellerAmount,
                                boolean searchFlights, 
                                boolean searchEvents,
                                boolean searchHotels) {

        if (travellerAmount <= 0) {
            throw new IllegalArgumentException("Traveller amount must be at least 1");
        }
        if (priceMin < 0 || priceMax < 0) {
            throw new IllegalArgumentException("Price bounds can't be negative");
        }
        if (priceMin > priceMax) {
            throw new IllegalArgumentException("priceMin can't exceed priceMax");
        }
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("startDate can't be after endDate");
        }
        if (!searchFlights && !searchEvents && !searchHotels) {
            throw new IllegalArgumentException("At least one search category must be selected");
        }

        List<Flight> results = new ArrayList<>();

        if (searchFlights) {
            results.addAll(searchFlightsByPlace(originPlace, destinationPlace,
                    startDate, endDate, priceMin, priceMax, travellerAmount));
        }

        if (searchEvents) {
            // seinna
        }

        if (searchHotels) {
            // seinna
        }

        return results;
    }

    // Flight search - sérhannað til að geta leiðað eftir borgum, ekki flugvöllum
    public List<Flight> searchFlightsByPlace(String originPlace, 
                                            String destinationPlace,
                                            LocalDate startDate, 
                                            LocalDate endDate,
                                            double priceMin, 
                                            double priceMax,
                                            int travellerAmount) {
        if (travellerAmount <= 0) {
            throw new IllegalArgumentException("Traveller amount must be at least 1");
        }
        if (priceMin < 0 || priceMax < 0) {
            throw new IllegalArgumentException("Price bounds can't be negative");
        }
        if (priceMin > priceMax) {
            throw new IllegalArgumentException("priceMin can't exceed priceMax");
        }
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("startDate can't be after endDate");
        }

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
    }

    // helper fall sem mappar place og airport svo hægt sé að leita eftir borgum
    public List<Airport> findAirportsByPlace(String place) {
        List<Airport> matched = new ArrayList<>();
        for (Airport a : flightSearchController.findAllAirports()) {
            if (a.getPlace().equalsIgnoreCase(place)) {
                matched.add(a);
            }
        }
        return matched;
    }

    // Event search
    public List<String> searchEvents(String name) {
        return new ArrayList<>();
    }

    // Hotel search
    public List<String> searchHotels(String location, LocalDate checkIn, LocalDate checkOut) {
        return new ArrayList<>();
    }
}
