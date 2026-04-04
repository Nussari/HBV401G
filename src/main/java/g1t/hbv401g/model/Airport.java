package g1t.hbv401g.model;

public class Airport {
    private String name;
    private String code;
    private String place;

    public Airport(String name, String code, String place) {
        this.name = name;
        this.code = code;
        this.place = place;
    }

    public String getName() { return name; }
    public String getCode() { return code; }
    public String getPlace() { return place; }
}
