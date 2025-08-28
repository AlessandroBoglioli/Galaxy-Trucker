package it.polimi.ingsoftware.ll13.network.response.menu_response;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.network.response.Response;

import java.io.Serial;

/**
 * this class is a response sent by the server indicating the client about the fact that his username has
 * already been taken
 */
public class UsernameTakenResponse extends Response {
    @Serial
    private static final long serialVersionUID = 1L;
    @Override
    public void execute(Handler handler) {
        handler.handleUsernameTakenResponse(this);

    }
}
