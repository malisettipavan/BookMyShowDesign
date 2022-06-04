package com.lowleveldesign.bookmyshow.api;

import com.lowleveldesign.bookmyshow.model.Screen;
import com.lowleveldesign.bookmyshow.model.Theatre;
import com.lowleveldesign.bookmyshow.service.TheatreService;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class TheatreController {
    private final TheatreService theatreService;

    public String createTheatre(@NonNull final String theatreName){
        return theatreService.createTheatre(theatreName).getId();
    }

    public String createScreenInTheatre(@NonNull final String screenName, @NonNull final String theatreId) {
        final Theatre theatre = theatreService.getTheatre(theatreId);
        return theatreService.createScreenInTheatre(screenName, theatre).getId();
    }

    public String createSeatInScreen(@NonNull final Integer rowNo, @NonNull final Integer seatNo, @NonNull final String screenId) {
        final Screen screen = theatreService.getScreen(screenId);
        return theatreService.createSeatInScreen(rowNo, seatNo, screen).getId();
    }
}
