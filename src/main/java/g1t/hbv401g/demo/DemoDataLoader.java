package g1t.hbv401g.demo;

import g1t.hbv401g.db.Database;
import g1t.teamD.controller.DayTripController;
import g1t.teamD.db.DayTripDB;
import g1t.teamD.model.DayTrip;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
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
        // TODO: populate once team F classes are imported.
    }

    // hotel
    private static void loadTeamH(Connection conn) {
        // TODO: populate once team H classes are imported.
    }
}
