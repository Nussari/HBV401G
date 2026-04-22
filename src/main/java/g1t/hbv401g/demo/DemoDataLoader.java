package g1t.hbv401g.demo;

import g1t.hbv401g.db.Database;
import g1t.teamD.controller.DayTripController;
import g1t.teamD.db.DayTripDB;
import g1t.teamD.model.DayTrip;
import is.hi.H1.model.Hotel;
import is.hi.H1.model.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public final class DemoDataLoader {

    private DemoDataLoader() {}

    public static void load() {
        loadTeamD(Database.teamD());
        loadTeamF(Database.teamF());
        loadTeamH(Database.teamH());
    }

    private static void loadTeamD(Connection conn) {
        if (conn == null) {
            System.err.println("[demo] team D: no connection — skipping");
            return;
        }
        try {
            conn.createStatement().execute("DROP TABLE IF EXISTS trips");

            DayTripDB tripDB = new DayTripDB(conn);
            DayTripController tripController = new DayTripController(tripDB);

            String[] categories = {"Nature", "Culture", "Food", "Adventure"};
            String[] places = {"Reykjavik", "Copenhagen", "Stockholm", "Tokyo", "New York"};

            Random rng = new Random();
            LocalDate today = LocalDate.now();

            for (int dayOffset = 0; dayOffset <= 14; dayOffset++) {
                LocalDate date = today.plusDays(dayOffset);
                for (String place : places) {
                    String category = categories[rng.nextInt(categories.length)];
                    int duration = 1 + rng.nextInt(8);
                    int difficulty = 1 + rng.nextInt(5);
                    int totalSpace = 5 + rng.nextInt(46);
                    double bookedFraction = 0.10 + rng.nextDouble() * 0.70;
                    int bookedSpaces = (int) Math.round(totalSpace * bookedFraction);
                    int price = 20 + rng.nextInt(281);
                    String name = category + " trip in " + place;

                    DayTrip trip = new DayTrip(
                            name, duration, difficulty, totalSpace, price, category, place, date);
                    trip.setBookedSpaces(bookedSpaces);
                    tripController.addTrip(trip);
                }
            }
        } catch (SQLException e) {
            System.err.println("[demo] team D load failed: " + e.getMessage());
        }
    }

    // flug
    private static void loadTeamF(Connection conn) {
    }

    // hotel
    private static void loadTeamH(Connection conn) {
        if (conn == null) {
            System.err.println("[demo] team H: no connection — skipping");
            return;
        }
        try (Statement s = conn.createStatement()) {
            s.execute("DROP TABLE IF EXISTS bookings");
            s.execute("DROP TABLE IF EXISTS rooms");
            s.execute("DROP TABLE IF EXISTS hotels");
            s.execute("CREATE TABLE hotels ("
                    + "hotel_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "name TEXT NOT NULL, street TEXT NOT NULL, "
                    + "postcode INTEGER NOT NULL, town TEXT NOT NULL)");
            s.execute("CREATE TABLE rooms ("
                    + "room_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "hotel_id INTEGER NOT NULL, room_capacity INTEGER NOT NULL, "
                    + "room_name TEXT NOT NULL, price_per_night INTEGER NOT NULL)");
            s.execute("CREATE TABLE bookings ("
                    + "booking_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "hotel_id INTEGER NOT NULL, room_id INTEGER NOT NULL, "
                    + "check_in INTEGER NOT NULL, check_out INTEGER NOT NULL, "
                    + "email TEXT)");
        } catch (SQLException e) {
            System.err.println("[demo] team H schema failed: " + e.getMessage());
            return;
        }

        String[] cities = {"Reykjavik", "Copenhagen", "Stockholm", "Tokyo", "New York"};
        String[] prefixes = {"Grand", "Royal", "Plaza", "Central", "Majestic", "Comfort"};
        String[] roomTypes = {"Single", "Double", "Twin", "Family"};
        int[] capacities = {1, 2, 2, 4};

        Random rng = new Random();

        List<Hotel> hotels = new ArrayList<>();
        for (String city : cities) {
            int hotelsInCity = 2 + rng.nextInt(2); // 2 or 3
            for (int h = 0; h < hotelsInCity; h++) {
                String name = prefixes[rng.nextInt(prefixes.length)] + " " + city;
                String street = "Street " + (1 + rng.nextInt(200));
                int postcode = 100 + rng.nextInt(900);

                int roomCount = 4 + rng.nextInt(6);
                Room[] rooms = new Room[roomCount];
                for (int r = 0; r < roomCount; r++) {
                    int ti = rng.nextInt(roomTypes.length);
                    int price = 60 + capacities[ti] * 40 + rng.nextInt(120);
                    // id/hotelId populated by the DB on insert; 0 placeholders here
                    rooms[r] = new Room(0, 0, capacities[ti], roomTypes[ti] + " Room " + (101 + r), price);
                }

                hotels.add(new Hotel(name, street, postcode, city, rooms));
            }
        }

        String insertHotel = "INSERT INTO hotels (name, street, postcode, town) VALUES (?, ?, ?, ?)";
        String insertRoom = "INSERT INTO rooms (hotel_id, room_capacity, room_name, price_per_night) VALUES (?, ?, ?, ?)";

        try {
            for (Hotel hotel : hotels) {
                int hotelId;
                try (PreparedStatement ps = conn.prepareStatement(insertHotel)) {
                    ps.setString(1, hotel.getName());
                    ps.setString(2, hotel.getAddress().getStreet());
                    ps.setInt(3, hotel.getAddress().getPostCode());
                    ps.setString(4, hotel.getAddress().getTown());
                    ps.executeUpdate();
                }
                // getGeneratedKeys virkar ekki
                try (Statement s = conn.createStatement();
                     ResultSet rs = s.executeQuery("SELECT last_insert_rowid()")) {
                    if (!rs.next()) continue;
                    hotelId = rs.getInt(1);
                }

                try (PreparedStatement ps = conn.prepareStatement(insertRoom)) {
                    for (Room room : hotel.getRooms()) {
                        ps.setInt(1, hotelId);
                        ps.setInt(2, room.getCapacity());
                        ps.setString(3, room.getName());
                        ps.setInt(4, room.getPricePerNight());
                        ps.executeUpdate();
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("[demo] team H load failed: " + e.getMessage());
        }
    }
}
