package it.polimi.ingsoftware.ll13.network.requests;

import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.MenuController;

public class PingRequest extends Request{
    public PingRequest(int senderId) {
        super(senderId);
    }

    @Override
    public void execute(Controller controller) {
        if(controller instanceof MenuController){
            MenuController.getInstance().pingRequestHandler(this);
        }
    }
}
