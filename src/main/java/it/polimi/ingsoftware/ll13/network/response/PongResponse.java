package it.polimi.ingsoftware.ll13.network.response;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;

public class PongResponse extends Response{

    @Override
    public void execute(Handler handler) {
        handler.handlePongResponse(this);
    }
}
