package it.polimi.ingsoftware.ll13.network.requests.game_requests.construction;

import it.polimi.ingsoftware.ll13.network.requests.Request;
import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.GameController;

/**
 * this class is used by the host player to send a request to start the construction phase, after this
 * every phase change will be handled by the controller and the model
 */
public class StartConstruction extends Request {
    public StartConstruction(int senderId) {
        super(senderId);
    }

    @Override
    public void execute(Controller controller) {
        try {
            GameController.getInstance().startConstruction();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
