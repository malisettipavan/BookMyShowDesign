package com.lowleveldesign.bookmyshow.service;

import com.lowleveldesign.bookmyshow.exceptions.BadRequestException;
import com.lowleveldesign.bookmyshow.exceptions.NotFoundException;
import com.lowleveldesign.bookmyshow.model.Booking;
import com.lowleveldesign.bookmyshow.model.Seat;
import com.lowleveldesign.bookmyshow.model.Show;
import com.lowleveldesign.bookmyshow.providers.SeatLockProvider;

import java.util.*;
import java.util.stream.Collectors;

public class BookingService {
    private final SeatLockProvider seatLockProvider;
    private final Map<String, Booking> showBookings;

    public BookingService(SeatLockProvider seatLockProvider){
        this.seatLockProvider = seatLockProvider;
        this.showBookings = new HashMap<>();
    }

    public Booking getBooking(final String bookingId) {
        if (!showBookings.containsKey(bookingId)) {
            throw new NotFoundException();
        }
        return showBookings.get(bookingId);
    }


    public List<Booking> getAllBookings(final Show show){
        final List<Booking> response = new ArrayList<>();
        for(Booking booking : showBookings.values()){
            if(booking.getShow().equals(show)){
                response.add(booking);
            }
        }

        return response;
    }

    public List<Seat> getBookedSeats(final Show show){
        return getAllBookings(show).stream()
                .filter(Booking::isConfirmed)
                .map(Booking::getSeatsBooked)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private boolean isAnySeatAlreadyBooked(final Show show, final List<Seat> seats){
        final List<Seat> bookedSeats = getBookedSeats(show);
        for(Seat seat : seats){
            if(bookedSeats.contains(seat)){
                return true;
            }
        }
        return false;
    }

    public Booking createBooking(final String user, final Show show, final List<Seat> seats){
        // check if any seat is already booked

        seatLockProvider.lockSeats(show, seats, user);
        final String bookingId = UUID.randomUUID().toString();
        final Booking booking = new Booking(bookingId, show, user, seats);
        showBookings.put(bookingId, booking);
        return booking;

        // TODO: Create timer for booking expiry
    }

    public void confirmBooking(final Booking booking, final String user) {
        if (!booking.getUser().equals(user)) {
            throw new BadRequestException();
        }

        for (Seat seat : booking.getSeatsBooked()) {
            if (!seatLockProvider.validateLock(booking.getShow(), seat, user)) {
                throw new BadRequestException();
            }
        }

        booking.confirmBooking();
    }

}
