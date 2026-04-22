package g1t.hbv401g.view.search;

import g1t.hbv401g.controller.SearchController;
import g1t.teamF.model.Flight;
import g1t.hbv401g.model.HotelSelection;
import g1t.hbv401g.view.util.Animations;
import g1t.teamD.model.DayTrip;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public final class SearchResults {

    private final SearchController controller;
    private final Runnable onSelectionChange;
    private final VBox root = new VBox(40);

    private Flight selectedOut;
    private Flight selectedReturn;
    private final Set<DayTrip> selectedDayTrips = new LinkedHashSet<>();
    private HotelSelection selectedHotel;

    public SearchResults(SearchController controller, Runnable onSelectionChange) {
        this.controller = controller;
        this.onSelectionChange = onSelectionChange;
    }

    public Node getRoot() { return root; }

    // ---------- selection access ----------

    public Flight selectedOut() { return selectedOut; }
    public Flight selectedReturn() { return selectedReturn; }
    public List<DayTrip> selectedDayTrips() { return new ArrayList<>(selectedDayTrips); }
    public HotelSelection selectedHotel() { return selectedHotel; }

    public boolean hasAnySelection() {
        return selectedOut != null || selectedReturn != null
                || !selectedDayTrips.isEmpty() || selectedHotel != null;
    }

    public void clearSelection() {
        selectedOut = null;
        selectedReturn = null;
        selectedDayTrips.clear();
        selectedHotel = null;
        for (Node n : new ArrayList<>(root.lookupAll(".selected"))) {
            n.getStyleClass().remove("selected");
        }
        onSelectionChange.run();
    }

    // ---------- render ----------

    public void render(SearchForm form, SearchFilters filters) {
        root.getChildren().clear();

        if (filters.nothingIncluded()) {
            root.getChildren().add(emptyRow(
                    "No components selected. Turn on a category above to see options."));
            onSelectionChange.run();
            return;
        }

        List<Flight> outbound = Collections.emptyList();
        List<Flight> inbound = Collections.emptyList();
        if (filters.includeFlights() && form.from() != null && form.to() != null) {
            Splits s = fetchFlights(form, filters);
            outbound = s.outbound;
            inbound = s.inbound;
        }

        if (filters.includeFlights()) root.getChildren().add(flightSection("Flight out", outbound, true));
        if (filters.includeHotels())  root.getChildren().add(hotelSection(form));
        if (filters.includeEvents())  root.getChildren().add(eventsSection(filters, form));
        if (filters.includeFlights()) root.getChildren().add(flightSection("Flight home", inbound, false));

        Animations.staggerIn(root);
        onSelectionChange.run();
    }

    // ---------- flight lookup ----------

    private record Splits(List<Flight> outbound, List<Flight> inbound) {}

    private Splits fetchFlights(SearchForm form, SearchFilters filters) {
        List<Flight> out = new ArrayList<>();
        List<Flight> in = new ArrayList<>();
        try {
            List<Flight> all = controller.searchFlightsByPlace(
                    form.from(), form.to(), form.depart(), form.returnOn(),
                    filters.priceMin(), filters.priceMax(), form.travellers());
            for (Flight f : all) {
                if (f.getDepartureA().getPlace().equalsIgnoreCase(form.from())) out.add(f);
                else in.add(f);
            }
        } catch (IllegalArgumentException ignored) {}
        return new Splits(out, in);
    }

    // ---------- sections ----------

    private VBox flightSection(String title, List<Flight> flights, boolean outbound) {
        VBox section = new VBox(12);
        section.getChildren().add(sectionHead(title, flights.size() + " OPTIONS"));
        if (flights.isEmpty()) {
            section.getChildren().add(emptyRow("No flights match. Widen your filters or change dates."));
        } else {
            for (Flight f : flights) {
                section.getChildren().add(FlightRow.create(f, outbound,
                        picked -> outbound ? toggleOutbound(picked) : toggleReturn(picked)));
            }
        }
        return section;
    }

    private VBox hotelSection(SearchForm form) {
        VBox section = new VBox(16);
        List<HotelSelection> options = fetchHotelSelections(form);

        // keep prior selection only if it's still a match (same hotel name & dates)
        if (selectedHotel != null) {
            boolean stillValid = false;
            for (HotelSelection o : options) {
                if (o.getHotel().getName().equals(selectedHotel.getHotel().getName())
                        && o.getCheckIn().equals(selectedHotel.getCheckIn())
                        && o.getCheckOut().equals(selectedHotel.getCheckOut())) {
                    selectedHotel = o;
                    stillValid = true;
                    break;
                }
            }
            if (!stillValid) selectedHotel = null;
        }

        section.getChildren().add(sectionHead("Hotel",
                options.size() + " OPTIONS  \u00B7  PICK ONE"));

        if (options.isEmpty()) {
            section.getChildren().add(emptyRow(
                    "No hotels match. Change destination, dates, or traveller count."));
        } else {
            TilePane grid = new TilePane(16, 16);
            grid.setPrefColumns(3);
            List<Node> hotelCards = new ArrayList<>();
            for (HotelSelection sel : options) {
                Node card = HotelCard.create(sel, sel == selectedHotel, picked -> {
                    boolean nowSelected = toggleHotel(picked);
                    if (nowSelected) {
                        for (Node other : hotelCards) other.getStyleClass().remove("selected");
                    }
                    return nowSelected;
                });
                hotelCards.add(card);
                grid.getChildren().add(card);
            }
            section.getChildren().add(grid);
        }
        return section;
    }

    private List<HotelSelection> fetchHotelSelections(SearchForm form) {
        return controller.searchHotelSelections(
                form.depart(), form.returnOn(), form.to(), form.travellers());
    }

    private VBox eventsSection(SearchFilters filters, SearchForm form) {
        VBox section = new VBox(16);
        List<DayTrip> trips = controller.searchDayTrips(
                form.to(), form.travellers(),
                form.depart(), form.returnOn(),
                filters.priceMin(), filters.priceMax());
        selectedDayTrips.retainAll(trips);

        section.getChildren().add(sectionHead("Day-trips", trips.size() + " OPTIONS  \u00B7  PICK ANY"));

        if (trips.isEmpty()) {
            section.getChildren().add(emptyRow("No day-trips match. Widen your filters."));
        } else {
            TilePane grid = new TilePane(16, 16);
            grid.setPrefColumns(3);
            for (DayTrip t : trips) {
                grid.getChildren().add(DayTripCard.create(t, selectedDayTrips.contains(t), this::toggleDayTrip));
            }
            section.getChildren().add(grid);
        }
        return section;
    }

    // ---------- toggles ----------

    private boolean toggleOutbound(Flight f) {
        selectedOut = (selectedOut == f) ? null : f;
        onSelectionChange.run();
        return selectedOut == f;
    }

    private boolean toggleReturn(Flight f) {
        selectedReturn = (selectedReturn == f) ? null : f;
        onSelectionChange.run();
        return selectedReturn == f;
    }

    private boolean toggleDayTrip(DayTrip t) {
        boolean nowSelected;
        if (selectedDayTrips.contains(t)) {
            selectedDayTrips.remove(t);
            nowSelected = false;
        } else {
            selectedDayTrips.add(t);
            nowSelected = true;
        }
        onSelectionChange.run();
        return nowSelected;
    }

    private boolean toggleHotel(HotelSelection sel) {
        boolean nowSelected = (selectedHotel != sel);
        selectedHotel = nowSelected ? sel : null;
        onSelectionChange.run();
        return nowSelected;
    }

    // ---------- helperar ----------

    private HBox sectionHead(String title, String hint) {
        Label name = new Label(title);
        name.getStyleClass().addAll("display", "display-md", "fs-26");

        Label hintLbl = new Label(hint);
        hintLbl.getStyleClass().add("eyebrow");

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        HBox head = new HBox(16, name, sp, hintLbl);
        head.setAlignment(Pos.BASELINE_LEFT);
        return head;
    }

    private HBox emptyRow(String message) {
        HBox row = new HBox();
        row.getStyleClass().add("result-item");
        row.setAlignment(Pos.CENTER_LEFT);
        Label l = new Label(message);
        l.getStyleClass().add("muted");
        row.getChildren().setAll(l);
        return row;
    }
}
