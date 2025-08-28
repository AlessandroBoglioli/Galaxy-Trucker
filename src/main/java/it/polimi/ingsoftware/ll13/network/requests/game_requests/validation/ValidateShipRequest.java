package it.polimi.ingsoftware.ll13.network.requests.game_requests.validation;

import it.polimi.ingsoftware.ll13.network.requests.Request;
import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.GameController;

public class ValidateShipRequest extends Request {
    public ValidateShipRequest(int senderId) {
        super(senderId);
    }
    @Override
    public void execute(Controller controller) {
        if(controller instanceof GameController){
            GameController.getInstance().handleValidateShipRequest(this);
        }
    }
}
