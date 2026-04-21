package g1t.teamD.model;

import java.time.LocalDate;

public class DayTrip {

    private final int tripID;
    private final String name;
    private final int duration;
    private final int difficulty;
    private int bookedSpaces;
    private final int totalSpace;
    private int price;
    private final String category;
    private final String place;
    private LocalDate date;

    // For creating new trips — ID and bookedSpaces assigned by DB
    public DayTrip(String name, int duration, int difficulty,
            int totalSpace, int price, String category, String place, LocalDate date) {
        this.tripID = 0;
        this.bookedSpaces = 0;
        this.name = name;
        this.duration = duration;
        this.difficulty = difficulty;
        this.totalSpace = totalSpace;
        this.price = price;
        this.category = category;
        this.place = place;
        this.date = date;
    }

    // For reconstructing from DB
    public DayTrip(int tripID, String name, int duration, int difficulty,
            int bookedSpaces, int totalSpace, int price,
            String category, String place, LocalDate date) {
        this.tripID = tripID;
        this.name = name;
        this.duration = duration;
        this.difficulty = difficulty;
        this.bookedSpaces = bookedSpaces;
        this.totalSpace = totalSpace;
        this.price = price;
        this.category = category;
        this.place = place;
        this.date = date;
    }

    // Getters
    public int getTripID() {
        return tripID;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getBookedSpaces() {
        return bookedSpaces;
    }

    public int getTotalSpace() {
        return totalSpace;
    }

    public int getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getPlace() {
        return place;
    }

    public LocalDate getDate() {
        return date;
    }

    // Setters
    public void setBookedSpaces(int bookedSpaces) {
        this.bookedSpaces = bookedSpaces;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean hasAvailability() {
        return bookedSpaces < totalSpace;
    }

    @Override
    public String toString() {
        return "DayTrip{" +
                "tripID=" + tripID +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", place='" + place + '\'' +
                ", date='" + date + '\'' +
                ", price=" + price +
                ", spaces=" + bookedSpaces + "/" + totalSpace +
                '}';
    }
}
