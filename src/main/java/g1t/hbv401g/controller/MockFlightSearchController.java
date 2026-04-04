package g1t.hbv401g.controller;
import java.util.List;
import java.util.Random;
import java.time.LocalDate;
import java.util.random.*;

public class MockFlightSearchController {
    // mock object, Airport String arrays follow the format:
    // name[0], code[1], place[2]
    List<String[]> Airports;
    // FlightNumber, departure airport code, arrival airport code, localdate(String)
    List<String[]> Flights;
    String[] airlines = {"PáskaFlug", "IcelandAir", "FlightsForAll", "KökuFlug", "SuperFlug"};
    String[] airlineCodes = {"PFL", "IAR", "FFA", "KFL", "SFL"};

    public MockFlightSearchController(){
        Random r = new Random();
        // hægt að breyta place keflavík í greater reykjavík area en það er algjör óþarfi
        Airports.add(new String[]{"Keflavík International Airport", "Kef", "Keflavík"});
        Airports.add(new String[]{"Billund Airport", "BLL", "Billund"});
        Airports.add(new String[]{"Copenhagen Airport, Kastrup", "CPH", "Copenhagen"});
        Airports.add(new String[]{"Heathrow Airport", "LHR", "London"});
        Airports.add(new String[]{"Glasgow Airport", "GLA", "Glasgow"});
        Airports.add(new String[]{"Nuuk Airport", "GOH", "Nuuk"});
        Airports.add(new String[]{"Berlin Brandenburg Airport", "BER", "Berlin"});

        String flightString;
        String arrival;
        String departure;
        String tempDate;
        for(int i = 0; i<=21; i++){
            flightString = "";
            flightString += airlineCodes[r.nextInt(5)];
            flightString += Integer.toString(r.nextInt(9000)+1000);
            do {
                arrival = Airports.get(r.nextInt(6))[1];
                departure = Airports.get(r.nextInt(6))[1];
            } while (departure == arrival);
            tempDate = LocalDate.now().plusDays(i/3).toString();
            Flights.add(new String[]{flightString, departure, arrival, tempDate});
        }

    }
    public List<String[]> findAllAirports(){
        return Airports;
    }

    public List<LocalDate> findDates(String departureA, String arrivalA){

    }

    public List<String> searchFlights(String departureA, String arrivalA, LocalDate date, int passengerCount){

    }
}