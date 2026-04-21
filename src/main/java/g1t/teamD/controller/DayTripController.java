package g1t.teamD.controller;

import g1t.teamD.db.DayTripDB;
import java.sql.SQLException;
import java.util.List;
import g1t.teamD.model.DayTrip;

public class DayTripController {

    private final DayTripDB tripDB;

    /**
     * @param tripDB the database instance to use for trip operations
     */
    public DayTripController(DayTripDB tripDB) {
        this.tripDB = tripDB;
    }

    /**
     * Adds a new trip to the database.
     *
     * @param t the trip to add
     * @throws SQLException if the database operation fails
     */
    public void addTrip(DayTrip t) throws SQLException {
        tripDB.insert(t);
    }

    /**
     * Removes a trip from the database.
     *
     * @param t the trip to remove
     * @throws SQLException if the database operation fails
     */
    public void removeTrip(DayTrip t) throws SQLException {
        tripDB.delete(t);
    }

    /**
     * Searches for available trips matching the given category, place and group size.
     *
     * @param category     the trip category to filter by (e.g. "Nature", "Culture"),
     *                     or {@code null} to match any category
     * @param place        the place to filter by, or {@code null} to match any place
     * @param spacesNeeded the minimum number of available spaces required
     * @return a list of matching trips, empty if none found
     * @throws SQLException if the database operation fails
     */
    public List<DayTrip> search(String category, String place, int spacesNeeded) throws SQLException {
        return tripDB.select(category, place, spacesNeeded);
    }

    /**
     * Searches for available trips matching the given category and group size, across all places.
     *
     * @param category     the trip category to filter by, or {@code null} to match any category
     * @param spacesNeeded the minimum number of available spaces required
     * @return a list of matching trips, empty if none found
     * @throws SQLException if the database operation fails
     */
    public List<DayTrip> search(String category, int spacesNeeded) throws SQLException {
        return tripDB.select(category, null, spacesNeeded);
    }

    /**
     * Searches for available trips matching the given group size, across all categories and places.
     *
     * @param spacesNeeded the minimum number of available spaces required
     * @return a list of matching trips, empty if none found
     * @throws SQLException if the database operation fails
     */
    public List<DayTrip> search(int spacesNeeded) throws SQLException {
        return tripDB.select(null, null, spacesNeeded);
    }
}
