package it.polimi.ingsoftware.ll13.network.response.match_responses;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.hourglass.HourglassLevel2;
import it.polimi.ingsoftware.ll13.network.response.Response;

public class NotifyHourglass extends Response {
    private final boolean isFlipped;

    public NotifyHourglass(boolean isFlipped) {
        this.isFlipped = isFlipped;
    }
    @Override
    public void execute(Handler handler) {
        handler.handleNotifyHourglass(this);
    }

    public boolean isFlipped() {
        return isFlipped;
    }
}
