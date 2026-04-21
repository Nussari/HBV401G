package g1t.hbv401g.view.util;

import g1t.teamD.model.DayTrip;
import g1t.hbv401g.model.Flight;

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
        return f.getFlightID() + "  \u00B7  "
                + f.getDepartureA().getCode() + "\u2192" + f.getArrivalA().getCode()
                + "  \u00B7  " + f.getDepartureTime().toLocalDate();
    }

    public static String flightLong(Flight f) {
        return f.getName() + " " + f.getFlightID() + "  \u00B7  "
                + f.getDepartureA().getCode() + "\u2192" + f.getArrivalA().getCode()
                + "  \u00B7  " + f.getDepartureTime().toLocalDate();
    }

    public static String dayTripShort(DayTrip t) {
        return t.getName() + "  \u00B7  " + t.getDate();
    }

    public static String dayTripLong(DayTrip t) {
        return t.getName() + "  \u00B7  " + t.getCategory() + "  \u00B7  " + t.getDate();
    }
}
