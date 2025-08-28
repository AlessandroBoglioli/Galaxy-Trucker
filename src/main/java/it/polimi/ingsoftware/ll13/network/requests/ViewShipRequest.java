package it.polimi.ingsoftware.ll13.network.requests;

import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.GameController;

public class ViewShipRequest extends Request{
    private final PlayerColors color;
    public ViewShipRequest(int senderId, PlayerColors color) {
        super(senderId);
        this.color = color;
    }
    @Override
    public void execute(Controller controller) {
        if(controller instanceof GameController){
            GameController.getInstance().handleViewShipRequest(this);
        }
    }
    public PlayerColors getColor() {
        return color;
    }
}
