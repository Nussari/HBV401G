package g1t.teamD.db;

import g1t.teamD.model.DayTripBooking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DayTripBookingDB {

    private final Connection conn;

    public DayTripBookingDB(Connection conn) throws SQLException {
        this.conn = conn;
        conn.createStatement().execute(
            "CREATE TABLE IF NOT EXISTS bookings (" +
            "bookingID INTEGER PRIMARY KEY, tripID INTEGER NOT NULL, name TEXT NOT NULL, " +
            "phoneNr TEXT NOT NULL, email TEXT NOT NULL, " +
            "FOREIGN KEY (tripID) REFERENCES trips(tripID))");
    }

    public DayTripBooking selectByID(int bookingID) throws SQLException {
        String sql = "SELECT * FROM bookings WHERE bookingID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return mapRow(rs);
        }
        return null;
    }

    public List<DayTripBooking> selectByTrip(int tripID) throws SQLException {
        String sql = "SELECT * FROM bookings WHERE tripID = ?";
        List<DayTripBooking> results = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tripID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(mapRow(rs));
            }
        }
        return results;
    }

    public int insert(DayTripBooking b) throws SQLException {
        String sql = "INSERT INTO bookings (tripID, name, phoneNr, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, b.getTripID());
            stmt.setString(2, b.getName());
            stmt.setString(3, b.getPhoneNr());
            stmt.setString(4, b.getEmail());
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next())
                return keys.getInt(1);
            throw new SQLException("Insert failed, no ID returned");
        }
    }

    public void delete(int bookingID) throws SQLException {
        String sql = "DELETE FROM bookings WHERE bookingID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingID);
            stmt.executeUpdate();
        }
    }

    private DayTripBooking mapRow(ResultSet rs) throws SQLException {
        return new DayTripBooking(
                rs.getInt("bookingID"),
                rs.getInt("tripID"),
                rs.getString("name"),
                rs.getString("phoneNr"),
                rs.getString("email"));
    }
}