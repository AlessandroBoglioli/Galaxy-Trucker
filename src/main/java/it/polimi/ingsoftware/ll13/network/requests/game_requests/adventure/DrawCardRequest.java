package it.polimi.ingsoftware.ll13.network.requests.game_requests.adventure;

import it.polimi.ingsoftware.ll13.network.requests.Request;
import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.GameController;

/**
 * Represents a request to draw a card in the game. This class extends the*/
public class DrawCardRequest extends Request {

    /**
     * Constructs a {@code DrawCardRequest} object, representing a request to draw a card
     * in the game. This request originates from a sender, identified by their unique ID.
     *
     * @param senderId The unique identifier of the player sending the draw card request.
     */
    public DrawCardRequest(int senderId) {
        super(senderId);
    }

    /**
     * Executes the draw card request within the context of the provided controller.
     * If the controller is an instance of {@code GameController}, this method triggers
     * the handling of the draw card request by delegating it to the {@code GameController}.
     *
     * @param controller The controller that processes the request. It is expected to be
     *                    an instance of {@code GameController}.
     */
    @Override
    public void execute(Controller controller) {
        if(controller instanceof GameController){

            GameController.getInstance().handleDrawCardRequest(this);
        }
    }
}
