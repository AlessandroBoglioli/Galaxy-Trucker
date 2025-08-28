package it.polimi.ingsoftware.ll13.network.requests.menu_requests;

import it.polimi.ingsoftware.ll13.model.general_enumerations.GameLevel;
import it.polimi.ingsoftware.ll13.network.requests.Request;
import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.MenuController;

public class NewMatchRequest extends Request {
    private final int numberOfPlayers;
    private final GameLevel gameLevel;
    private final String username;

    public NewMatchRequest(int senderId, int numberOfPlayers, GameLevel gameLevel, String username) {
        super(senderId);
        this.numberOfPlayers = numberOfPlayers;
        this.gameLevel = gameLevel;
        this.username = username;
    }

    @Override
    public void execute(Controller controller) {
        if(controller instanceof MenuController){
            MenuController.getInstance().newMatchRequestHandler(this);
        }
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public GameLevel getGameLevel() {
        return gameLevel;
    }

    public String getUsername() {
        return username;
    }
}
