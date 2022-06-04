package com.lowleveldesign.bookmyshow.service;

import com.lowleveldesign.bookmyshow.exceptions.NotFoundException;
import com.lowleveldesign.bookmyshow.model.Screen;
import com.lowleveldesign.bookmyshow.model.Seat;
import com.lowleveldesign.bookmyshow.model.Theatre;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TheatreService {

    private final Map<String, Theatre> theatres;
    private final Map<String, Screen> screens;
    private final Map<String, Seat> seats;

    public TheatreService(){
        this.theatres = new HashMap<>();
        this.screens = new HashMap<>();
        this.seats = new HashMap<>();
    }

    public Theatre createTheatre(final String theatreName){
        final String theatreId = UUID.randomUUID().toString();
        final Theatre theatre = new Theatre(theatreId, theatreName);
        this.theatres.put(theatreId, theatre);
        return theatre;
    }

    private Screen createScreen(final String screenName, final Theatre theatre){
        final String screenId = UUID.randomUUID().toString();
        final Screen screen = new Screen(screenId, screenName, theatre);
        screens.put(screenId, screen);
        return screen;
    }

    public Screen createScreenInTheatre(final String screenName, final Theatre theatre){
        final Screen screen = createScreen(screenName, theatre);
        theatre.addScreen(screen);
        return screen;
    }

    private Seat createSeat(final int rowNo, final int seatNo){
        final String seatId = UUID.randomUUID().toString();
        final Seat seat = new Seat(seatId, rowNo, seatNo);
        seats.put(seatId, seat);
        return seat;
    }

    public Seat createSeatInScreen(final int rowNo, final int seatNo, final Screen screen){
        final Seat seat = createSeat(rowNo, seatNo);
        screen.addSeat(seat);
        return seat;
    }

    public Theatre getTheatre(final String theatreId){
        if(!theatres.containsKey(theatreId)){
            throw new NotFoundException();
        }
        return theatres.get(theatreId);
    }

    public Screen getScreen(final String screenId){
        if(!screens.containsKey(screenId)){
            throw new NotFoundException();
        }
        return screens.get(screenId);
    }

    public Seat getSeat(final String seatId){
        if(!seats.containsKey(seatId)){
            throw new NotFoundException();
        }
        return seats.get(seatId);
    }
}
