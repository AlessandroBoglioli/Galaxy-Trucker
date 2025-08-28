package it.polimi.ingsoftware.ll13.network.requests;

import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.GameController;

public class QuitRequest extends Request{

    public QuitRequest(int senderId) {
        super(senderId);
    }

    @Override
    public void execute(Controller controller) {
        if(controller instanceof GameController){
            GameController.getInstance().disconnectClient(this.getSenderId());
        }
    }
}
