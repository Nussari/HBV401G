package g1t.hbv401g.controller;

import g1t.hbv401g.db.Database;
import g1t.hbv401g.model.HotelSelection;
import g1t.teamF.model.Flight;
import g1t.teamD.controller.DayTripController;
import g1t.teamD.db.DayTripDB;
import g1t.teamD.model.DayTrip;
import is.hi.H1.controllers.BookingController;
import is.hi.H1.controllers.HotelController;
import is.hi.H1.model.Hotel;
import is.hi.H1.model.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                                                      String place, int capacity,
                                                      double priceMin, double priceMax) {
        List<HotelSelection> result = new ArrayList<>();
        if (checkIn == null || checkOut == null || place == null || place.isBlank()) return result;
        if (!checkOut.isAfter(checkIn)) return result;

        Hotel[] hotels;
        try {
            hotels = HotelController.getHotels(checkIn, checkOut, place, capacity);
        } catch (Exception e) {
            System.err.println("[search] hotel search failed: " + e.getMessage());
            return result;
        }
        if (hotels == null) return result;

        // hardcoded villa í db.getHotels hjá H, þetta er patch fyrir það
        patchRoomPrices(hotels);

        for (Hotel h : hotels) {
            try {
                Room[] bestRooms = BookingController.getBestBookingOption(h, capacity);
                if (bestRooms == null || bestRooms.length == 0) continue;
                HotelSelection sel = new HotelSelection(h, bestRooms, checkIn, checkOut, place);
                double cost = sel.getTotalCost();
                if (cost < priceMin || cost > priceMax) continue;
                result.add(sel);
            } catch (Exception e) {
                System.err.println("[search] getBestBookingOption failed: " + e.getMessage());
            }
        }
        return result;
    }

    private static void patchRoomPrices(Hotel[] hotels) {
        Connection conn = Database.teamH();
        if (conn == null) return;
        Map<Integer, Integer> prices = new HashMap<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT room_id, price_per_night FROM rooms");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) prices.put(rs.getInt(1), rs.getInt(2));
        } catch (SQLException e) {
            System.err.println("[search] price patch query failed: " + e.getMessage());
            return;
        }
        for (Hotel h : hotels) {
            Room[] rooms = h.getRooms();
            if (rooms == null) continue;
            for (Room r : rooms) {
                Integer p = prices.get(r.getId());
                if (p != null) r.setPricePerNight(p);
            }
        }
    }
}
