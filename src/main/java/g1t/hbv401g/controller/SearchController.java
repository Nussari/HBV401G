package g1t.hbv401g.controller;

import g1t.hbv401g.db.Database;
import g1t.hbv401g.model.Flight;
import g1t.hbv401g.model.HotelSelection;
import g1t.teamD.controller.DayTripController;
import g1t.teamD.db.DayTripDB;
import g1t.teamD.model.DayTrip;
import is.hi.H1.controllers.BookingController;
import is.hi.H1.controllers.HotelController;
import is.hi.H1.model.Hotel;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SearchController {

    // private FlightSearchController flightSearchController; - setja inn þegar lið F skilar
    private final DayTripController dayTripController;

    public SearchController(DayTripController dayTripController) {
        this.dayTripController = dayTripController;
    }

    public SearchController() {
        DayTripController ctrl = null;
        try {
            ctrl = new DayTripController(new DayTripDB(Database.teamD()));
        } catch (SQLException e) {
            System.err.println("[search] team D DayTripDB init failed: " + e.getMessage());
        }
        this.dayTripController = ctrl;
    }

    // ná í alla staði fyrir view dropdowns
    public List<String> getPlaces() {
        return List.of("Reykjavik", "Copenhagen", "Stockholm", "Tokyo", "New York");
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
        return new ArrayList<>();
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

    // Hotel search - pakkar fyrstu booking-tillögu hvers hótels í HotelSelection
    public List<HotelSelection> searchHotelSelections(LocalDate checkIn, LocalDate checkOut,
                                                      String place, int capacity) {
        List<HotelSelection> result = new ArrayList<>();
        if (checkIn == null || checkOut == null || place == null || place.isBlank()) return result;
        if (!checkOut.isAfter(checkIn)) return result;

        Hotel[] hotels;
        try {
            hotels = HotelController.search(checkIn, checkOut, place, capacity);
        } catch (Exception e) {
            System.err.println("[search] hotel search failed: " + e.getMessage());
            return result;
        }
        if (hotels == null) return result;

        for (Hotel h : hotels) {
            try {
                is.hi.H1.model.Booking[] options = BookingController.getPossibleBookings(h, capacity);
                if (options == null || options.length == 0) continue;
                is.hi.H1.model.Booking best = options[0];
                result.add(new HotelSelection(
                        h, best.getRooms(), best.getCheckIn(), best.getCheckOut(), place));
            } catch (Exception e) {
                System.err.println("[search] getPossibleBookings failed: " + e.getMessage());
            }
        }
        return result;
    }
}
