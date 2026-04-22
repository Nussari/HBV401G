package g1t.hbv401g.controller;

import g1t.teamF.model.Flight;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SearchControllerTest {

    private SearchController searchController;

    @BeforeEach
    void setUp() {
        searchController = new SearchController();
    }

    // Validation tests - öll nöfn ættu að vera auðskiljanleg

    @Test
    void searchRejectsNegativeTravellerAmount() {
        assertThrows(IllegalArgumentException.class, () ->
            searchController.search("Keflavík", "London",
                LocalDate.now(), LocalDate.now().plusDays(5),
                0, 1000, -1,
                true, false, false));
    }

    @Test
    void searchRejectsZeroTravellerAmount() {
        assertThrows(IllegalArgumentException.class, () ->
            searchController.search("Keflavík", "London",
                LocalDate.now(), LocalDate.now().plusDays(5),
                0, 1000, 0,
                true, false, false));
    }

    @Test
    void searchRejectsNegativePriceMin() {
        assertThrows(IllegalArgumentException.class, () ->
            searchController.search("Keflavík", "London",
                LocalDate.now(), LocalDate.now().plusDays(5),
                -100, 1000, 1,
                true, false, false));
    }

    @Test
    void searchRejectsPriceMinExceedingPriceMax() {
        assertThrows(IllegalArgumentException.class, () ->
            searchController.search("Keflavík", "London",
                LocalDate.now(), LocalDate.now().plusDays(5),
                500, 100, 1,
                true, false, false));
    }

    @Test
    void searchRejectsStartDateAfterEndDate() {
        assertThrows(IllegalArgumentException.class, () ->
            searchController.search("Keflavík", "London",
                LocalDate.now().plusDays(5), LocalDate.now(),
                0, 1000, 1,
                true, false, false));
    }

    @Test
    void searchRejectsNoCategorySelected() {
        assertThrows(IllegalArgumentException.class, () ->
            searchController.search("Keflavík", "London",
                LocalDate.now(), LocalDate.now().plusDays(5),
                0, 1000, 1,
                false, false, false));
    }

    @Test
    void searchWithValidParametersDoesNotThrow() {
        assertDoesNotThrow(() ->
            searchController.search("Keflavík", "London",
                LocalDate.now(), LocalDate.now().plusDays(5),
                0, 1000, 2,
                true, false, false));
    }

    // flug tests

    @Test
    void flightSearchRejectsNegativeTravellers() {
        assertThrows(IllegalArgumentException.class, () ->
            searchController.searchFlightsByPlace(
                "Keflavík", "London",
                LocalDate.now(), LocalDate.now().plusDays(3),
                0, 1000, -1));
    }

    @Test
    void flightSearchRejectsInvertedPriceRange() {
        assertThrows(IllegalArgumentException.class, () ->
            searchController.searchFlightsByPlace(
                "Keflavík", "London",
                LocalDate.now(), LocalDate.now().plusDays(3),
                500, 100, 1));
    }

    @Test
    void flightSearchRejectsStartDateAfterEndDate() {
        assertThrows(IllegalArgumentException.class, () ->
            searchController.searchFlightsByPlace(
                "Keflavík", "London",
                LocalDate.now().plusDays(5), LocalDate.now(),
                0, 1000, 1));
    }

    // Prepopulate tests

    @Test
    void getPlacesReturnsNonEmptyList() {
        List<String> places = searchController.getPlaces();
        assertFalse(places.isEmpty());
    }

    @Test
    void getPlacesContainsKnownCities() {
        List<String> places = searchController.getPlaces();
        assertTrue(places.contains("Keflavík"));
        assertTrue(places.contains("London"));
        assertTrue(places.contains("Berlin"));
    }

    @Test
    void getPlacesHasNoDuplicates() {
        List<String> places = searchController.getPlaces();
        long distinct = places.stream().distinct().count();
        assertEquals(places.size(), distinct);
    }

    // Prufur fyrir útkomu flight search

    @Test
    void flightSearchReturnsOnlyFlightsWithinPriceRange() {
        List<Flight> results = searchController.searchFlightsByPlace(
                "Keflavík", "London",
                LocalDate.now(), LocalDate.now().plusDays(7),
                0, 200, 1);
        for (Flight f : results) {
            assertTrue(f.getPrice() >= 0 && f.getPrice() <= 200);
        }
    }

    @Test
    void flightSearchForUnknownCityReturnsEmpty() {
        List<Flight> results = searchController.searchFlightsByPlace(
                "Atlantis", "London",
                LocalDate.now(), LocalDate.now().plusDays(3),
                0, 10000, 1);
        assertTrue(results.isEmpty());
    }
}
