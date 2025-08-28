package it.polimi.ingsoftware.ll13.network.requests.game_requests.construction;

import it.polimi.ingsoftware.ll13.network.requests.Request;
import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.GameController;

public class FlipHourGlassRequest extends Request {
    public FlipHourGlassRequest(int senderId) {
        super(senderId);
    }

    @Override
    public void execute(Controller controller) {
        if(controller instanceof GameController){
            GameController.getInstance().handleFlipHourglassRequest(this);
        }
    }

}
