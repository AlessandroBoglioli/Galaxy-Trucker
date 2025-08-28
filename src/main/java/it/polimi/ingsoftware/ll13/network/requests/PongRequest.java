package it.polimi.ingsoftware.ll13.network.requests;

import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.MenuController;

public class PongRequest extends Request{
    public PongRequest(int id) {
        super(id);
    }

    @Override
    public void execute(Controller controller) {
        if(controller instanceof MenuController){
            MenuController.getInstance().pongRequestHandler(this);
        }
    }
}
