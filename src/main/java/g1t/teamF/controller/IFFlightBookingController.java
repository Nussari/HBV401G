package g1t.teamF.controller;

import g1t.teamF.model.*;

/**
 * @author Eva Guðrún Jónsdóttir (@hi.is)
 * @author Hera Huld Gunnlaugsdóttir (@hi.is)
 * @author Kristín Kolka Björnsdóttir (@hi.is)
 * @author Sigríður H. Halldórsdóttir (shh60@hi.is)
 */
public interface IFFlightBookingController {

    /**
     * Creates a new {@link Booking} for the given flights and passenger count.
     *
     * @param outboundF the outbound flight
     * @param returnF the return flight, {@code null} if one-way trip
     * @param passengerCount number of passengers
     * @return a new {@link Booking}, or {@code null} if outboundF is {@code null} or {@code passengerCount <= 0}
     */
    Booking createBooking(Flight outboundF, Flight returnF, int passengerCount);

    /**
     * Reserves a seat on a specific flight for the given booking.
     * Seat status is changed to {@link SeatStatus#RESERVED}.
     *
     * @param booking the booking to add the seat to
     * @param flight the flight the seat belongs to
     * @param seat the seat to reserve
     * @return {@code true} if seat status is {@link SeatStatus#AVAILABLE} and fewer than {@code passengerCount}
     * seats have been selected for the given flight, {@code false} otherwise
     */
    boolean chooseSeat(Booking booking, Flight flight, Seat seat);

    /**
     * Releases a reserved seat from a specific flight in the given booking.
     * Seat status is changed to {@link SeatStatus#AVAILABLE}.
     *
     * @param booking the booking to remove the seat from
     * @param flight the flight the seat belongs to
     * @param seat the seat to release
     * @return {@code true} if the seat is found in the booking for that flight, {@code false} otherwise
     */
    boolean removeSeat(Booking booking, Flight flight, Seat seat);

    /**
     * Confirms a given booking.
     * Seat status of all seats is changed to {@link SeatStatus#BOOKED} and booking status is changed to
     * {@link BookingStatus#CONFIRMED}.
     *
     * @param booking the booking to confirm
     * @return {@code true} if booking passes validation and has not already been saved, {@code false} otherwise
     */
    boolean confirmBooking(Booking booking);

    /**
     * Cancels a booking with given bookingReference.
     * Seat status of all seats in booking is changed to {@link SeatStatus#AVAILABLE} and booking status is changed to
     * {@link BookingStatus#CANCELLED}.
     *
     * @param bookingReference the bookingReference of the booking to cancel
     * @return {@code true} if booking is found with the given booking reference and if its status is
     * {@link BookingStatus#CONFIRMED}, {@code false} otherwise
     */
    boolean cancelBooking(String bookingReference);
}
