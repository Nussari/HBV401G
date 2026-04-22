package g1t.hbv401g.model;

import is.hi.H1.model.Hotel;
import is.hi.H1.model.Room;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// brú á milli okkar aðgerða og H liðs
public final class HotelSelection {

    private final Hotel hotel;
    private final Room[] rooms;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private final String place;

    public HotelSelection(Hotel hotel, Room[] rooms, LocalDate checkIn, LocalDate checkOut, String place) {
        this.hotel = hotel;
        this.rooms = rooms;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.place = place;
    }

    public Hotel getHotel() { return hotel; }
    public Room[] getRooms() { return rooms; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public String getPlace() { return place; }

    public long getNights() {
        return Math.max(1, ChronoUnit.DAYS.between(checkIn, checkOut));
    }

    public int getGuestCapacity() {
        int total = 0;
        if (rooms != null) for (Room r : rooms) total += r.getCapacity();
        return total;
    }

    public double getTotalCost() {
        if (rooms == null) return 0;
        double perNight = 0;
        for (Room r : rooms) perNight += r.getPricePerNight();
        return perNight * getNights();
    }
}
