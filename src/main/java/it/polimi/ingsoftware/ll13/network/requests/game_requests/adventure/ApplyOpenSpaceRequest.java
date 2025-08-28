package it.polimi.ingsoftware.ll13.network.requests.game_requests.adventure;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.requests.Request;
import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.GameController;

/**
 * Represents a request to apply an open space effect in the game. This request is associated
 * with a specific card and originates from a particular sender identified by their ID.
 *
 * The {@code ApplyOpenSpaceRequest} class extends the abstract {@code Request} class
 * and provides the implementation of the {@code execute} method, which handles the specific
 * behavior of this request within the game controller.
 */
public class ApplyOpenSpaceRequest extends Request {

    private final Card card;

    /**
     * Constructs an {@code ApplyOpenSpaceRequest} object. This request is used to apply the
     * effect of an open space card within the game. It is tied to the specific card passed as a parameter
     * and originates from a sender identified by their unique ID.
     *
     * @param senderId The unique identifier of the player sending the request.
     * @param card     The {@code Card} object associated with the open space effect being applied.
     */
    public ApplyOpenSpaceRequest(int senderId, Card card) {
        super(senderId);
        this.card = card;
    }

    // --> Getters <--

    /**
     * Retrieves the card associated with this request.
     *
     * @return The {@code Card} object tied to this request.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Executes the specific behavior associated with the request using the provided controller.
     * If the controller is an instance of {@code GameController}, it invokes the method
     * to handle the open space effect tied to this request.
     *
     * @param controller The controller responsible for processing this request. Expected to be an instance of
     *                   {@code GameController} for proper handling of the open space effect.
     */
    @Override
    public void execute(Controller controller) {
        if(controller instanceof GameController){
            ((GameController) controller).handleOpenSpaceEffect(this);
        }
    }
}
