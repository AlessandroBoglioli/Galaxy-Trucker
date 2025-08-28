package it.polimi.ingsoftware.ll13.network.requests.menu_requests;

import it.polimi.ingsoftware.ll13.model.general_enumerations.GameLevel;
import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.MenuController;
import it.polimi.ingsoftware.ll13.network.requests.Request;

import java.io.Serial;

/**
 * This class is the first request that a new player joining the game session sends to the server, that of
 * asking for a login service
 */
public class LoginRequest extends Request {
    private final String username;

    public LoginRequest(int senderId, String username) {
        super(senderId);
        this.username = username;
    }

    @Override
    public void execute(Controller controller) {
        if(controller instanceof MenuController){
            MenuController.getInstance().loginRequestHandler(this);
        }
    }

    public String getUsername() {
        return username;
    }
}
