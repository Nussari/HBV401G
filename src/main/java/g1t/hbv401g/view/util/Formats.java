package g1t.hbv401g.view.util;

import g1t.teamF.model.Flight;
import g1t.hbv401g.model.HotelSelection;
import g1t.teamD.model.DayTrip;
import g1t.teamF.model.Flight;
import is.hi.H1.model.Room;

// formatting helperar
public final class Formats {

    private Formats() {}

    public static String money(double amount) {
        return "$" + String.format("%,d", (long) Math.round(amount));
    }

    public static String moneyInt(double amount) {
        return "$" + String.format("%,d", (int) amount);
    }

    public static String flightShort(Flight f) {
        return f.getFlightNumber() + "  \u00B7  "
                + f.getDepartureAirport().getCode() + "\u2192" + f.getArrivalAirport().getCode()
                + "  \u00B7  " + f.getDepartureTime();
    }

    public static String flightLong(Flight f) {
        return f.getFlightNumber() + " " + f.getFlightID() + "  \u00B7  "
                + f.getDepartureAirport().getCode() + "\u2192" + f.getArrivalAirport().getCode()
                + "  \u00B7  " + f.getDepartureTime();
    }

    public static String dayTripShort(DayTrip t) {
        return t.getName() + "  \u00B7  " + t.getDate();
    }

    public static String dayTripLong(DayTrip t) {
        return t.getName() + "  \u00B7  " + t.getCategory() + "  \u00B7  " + t.getDate();
    }

    public static String hotelShort(HotelSelection sel) {
        long nights = sel.getNights();
        return sel.getHotel().getName()
                + "  \u00B7  " + nights + (nights == 1 ? " night" : " nights");
    }

    public static String hotelLong(HotelSelection sel) {
        long nights = sel.getNights();
        StringBuilder rooms = new StringBuilder();
        for (Room r : sel.getRooms()) {
            if (rooms.length() > 0) rooms.append(", ");
            rooms.append(r.getName());
        }
        return sel.getHotel().getName()
                + "  \u00B7  " + sel.getCheckIn() + " \u2192 " + sel.getCheckOut()
                + "  \u00B7  " + nights + (nights == 1 ? " night" : " nights")
                + "  \u00B7  " + rooms;
    }
}
