package it.polimi.ingsoftware.ll13.network.requests.game_requests.adventure;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.requests.Request;
import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.GameController;

/**
 * Represents a request to apply the effect of an abandoned station card in the game.
 * This request is sent by a specific sender and contains the card whose effect needs to be applied.
 * The execution of this request delegates the handling of the abandoned station effect
 * to an appropriate method within a GameController instance.
 */
public class ApplyAbandonedStationEffectRequest extends Request {

    private final Card card;

    /**
     * Constructs a request to apply the effect of an abandoned station card in the game.
     *
     * @param senderId The unique identifier of the sender creating this request.
     * @param card The card representing the abandoned station whose effect needs to be applied.
     */
    public ApplyAbandonedStationEffectRequest(int senderId, Card card) {
        super(senderId);
        this.card = card;
    }

    // --> Getters <--

    /**
     * Retrieves*/
    public Card getCard() {
        return card;
    }

    /**
     * Executes the request to apply the effect of an abandoned station card.
     * Delegates the handling of the effect to the appropriate method in a GameController instance.
     *
     * @param controller The controller responsible for managing the game state, expected to be an instance of GameController.
     */
    @Override
    public void execute(Controller controller) {
        if(controller instanceof GameController){
            ((GameController) controller).handleAbandonedStationEffect(this);
        }
    }
}
