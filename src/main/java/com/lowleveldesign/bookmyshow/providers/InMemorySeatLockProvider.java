package com.lowleveldesign.bookmyshow.providers;

import com.lowleveldesign.bookmyshow.exceptions.SeatTemporaryUnavailableException;
import com.lowleveldesign.bookmyshow.model.Seat;
import com.lowleveldesign.bookmyshow.model.SeatLock;
import com.lowleveldesign.bookmyshow.model.Show;

import java.util.*;

public class InMemorySeatLockProvider implements SeatLockProvider{

    private final Integer lockTimeout;
    private final Map<Show, Map<Seat, SeatLock>> locks;

    public InMemorySeatLockProvider(final Integer lockTimeout){
        this.lockTimeout = lockTimeout;
        this.locks = new HashMap<>();
    }

    private void lockSeat(final Show show, final Seat seat, final String user, final Integer timeoutInSeconds){
        final SeatLock seatLock = new SeatLock(seat, show, new Date(), timeoutInSeconds, user);
        if(!locks.containsKey(show)){
            locks.put(show, new HashMap<>());
        }
        locks.get(show).put(seat, seatLock);
    }

    private boolean isSeatLocked(final Show show, final Seat seat){
        return locks.containsKey(show) && locks.get(show).containsKey(seat) &&
        !locks.get(show).get(seat).isLockExpired();
    }

    @Override
    synchronized public void lockSeats(final Show show, final List<Seat> seats, final String user) {

        for(Seat seat : seats){
            if(isSeatLocked(show, seat)){
                throw new SeatTemporaryUnavailableException();
            }
        }

        for(Seat seat : seats){
            lockSeat(show, seat, user, lockTimeout);
        }
    }

    private void unlockSeat(Show show, Seat seat){
        if(locks.containsKey(show)){
            locks.get(show).remove(seat);
        }
    }

    @Override
    public void unlockSeats(Show show, List<Seat> seats, String user) {
        for(Seat seat : seats){
            if(validateLock(show, seat, user)){
                unlockSeat(show, seat);
            }
        }
    }

    @Override
    public boolean validateLock(Show show, Seat seat, String user) {
        return isSeatLocked(show, seat) && locks.get(show).get(seat).getLockedBy().equals(user);
    }

    @Override
    public List<Seat> getLockedSeats(Show show) {
        if(!locks.containsKey(show)) return new ArrayList<>();

        final List<Seat> lockedSeats = new ArrayList<>();
        for(Seat seat : locks.get(show).keySet()){
            if(isSeatLocked(show, seat)){
                lockedSeats.add(seat);
            }
        }

        return lockedSeats;
    }
}
