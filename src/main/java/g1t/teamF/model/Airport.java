package g1t.teamF.model;

/**
 * @author Eva Guðrún Jónsdóttir (@hi.is)
 * @author Hera Huld Gunnlaugsdóttir (@hi.is)
 * @author Kristín Kolka Björnsdóttir (@hi.is)
 * @author Sigríður H. Halldórsdóttir (shh60@hi.is)
 */
public class Airport {

    private final String code;
    private final String place;
    private final String country;

    public Airport(String code, String place, String country) {
        this.code = code;
        this.place = place;
        this.country = country;
    }

    public String getCode() { return code; }

    public String getPlace() { return place; }

    public String getCountry() { return country; }

    @Override
    public String toString() { return place + " (" + code + ")"; }
}
