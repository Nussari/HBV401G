package g1t.teamD.db;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import g1t.teamD.model.DayTrip;

public class DayTripDB {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    private final Connection conn;

    public DayTripDB(Connection conn) throws SQLException {
        this.conn = conn;
        conn.createStatement().execute(
            "CREATE TABLE IF NOT EXISTS trips (" +
            "tripID INTEGER PRIMARY KEY, name TEXT NOT NULL, duration INTEGER NOT NULL, " +
            "difficulty INTEGER NOT NULL, totalSpace INTEGER NOT NULL, bookedSpaces INTEGER NOT NULL DEFAULT 0, " +
            "price INTEGER NOT NULL, category TEXT NOT NULL, place TEXT NOT NULL, date TEXT NOT NULL)");
    }

    public List<DayTrip> select(String category, String place, int spacesNeeded) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM trips WHERE (totalSpace - bookedSpaces) >= ?");
        if (category != null) {
            sql.append(" AND category = ?");
        }
        if (place != null) {
            sql.append(" AND place = ?");
        }
        List<DayTrip> results = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            stmt.setInt(idx++, spacesNeeded);
            if (category != null) {
                stmt.setString(idx++, category);
            }
            if (place != null) {
                stmt.setString(idx++, place);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(mapRow(rs));
            }
        }
        return results;
    }

    public DayTrip selectByID(int tripID) throws SQLException {
        String sql = "SELECT * FROM trips WHERE tripID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tripID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return mapRow(rs);
        }
        return null;
    }

    public void insert(DayTrip t) throws SQLException {
        String sql = "INSERT INTO trips (name, duration, difficulty, totalSpace, bookedSpaces, price, category, place, date) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, t.getName());
            stmt.setInt(2, t.getDuration());
            stmt.setInt(3, t.getDifficulty());
            stmt.setInt(4, t.getTotalSpace());
            stmt.setInt(5, t.getBookedSpaces());
            stmt.setInt(6, t.getPrice());
            stmt.setString(7, t.getCategory());
            stmt.setString(8, t.getPlace());
            stmt.setString(9, t.getDate().format(DATE_FORMAT));
            stmt.executeUpdate();
        }
    }

    public void update(DayTrip t) throws SQLException {
        String sql = "UPDATE trips SET bookedSpaces=?, price=?, date=? WHERE tripID=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, t.getBookedSpaces());
            stmt.setInt(2, t.getPrice());
            stmt.setString(3, t.getDate().format(DATE_FORMAT));
            stmt.setInt(4, t.getTripID());
            stmt.executeUpdate();
        }
    }

    public void delete(DayTrip t) throws SQLException {
        String sql = "DELETE FROM trips WHERE tripID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, t.getTripID());
            stmt.executeUpdate();
        }
    }

    private DayTrip mapRow(ResultSet rs) throws SQLException {
        return new DayTrip(
                rs.getInt("tripID"),
                rs.getString("name"),
                rs.getInt("duration"),
                rs.getInt("difficulty"),
                rs.getInt("bookedSpaces"),
                rs.getInt("totalSpace"),
                rs.getInt("price"),
                rs.getString("category"),
                rs.getString("place"),
                LocalDate.parse(rs.getString("date"), DATE_FORMAT));
    }
}