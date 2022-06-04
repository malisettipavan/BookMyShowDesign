package com.lowleveldesign.bookmyshow.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.Date;

@AllArgsConstructor
@Getter
public class SeatLock {
    private final Seat seat;
    private final Show show;
    private final Date lockTime;
    private final Integer timeoutInSeconds;
    private final String lockedBy;

    public boolean isLockExpired(){
        final Instant lockInstant = lockTime.toInstant().plusSeconds(timeoutInSeconds);
        final Instant currentInstant = new Date().toInstant();
        return lockInstant.isBefore(currentInstant);
    }

}
