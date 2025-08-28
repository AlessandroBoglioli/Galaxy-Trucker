package it.polimi.ingsoftware.ll13.network.requests.game_requests.adventure;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.requests.Request;
import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.GameController;

/**
 * Represents a request to apply the effect of an "Abandoned Ship" card within the game.
 * This request encapsulates the card whose effect is to be applied and is sent by a specific sender.
 * The request, when executed, delegates the handling of the card's effect to the appropriate controller logic.
 */
public class ApplyAbandonedShipEffectRequest extends Request {

    private final Card card;

    /**
     * Constructs a request to apply the effect of an "Abandoned Ship" card.
     *
     * @param senderId The unique identifier of the sender initiating the request.
     * @param card The card whose effect is to be applied.
     */
    public ApplyAbandonedShipEffectRequest(int senderId, Card card) {
        super(senderId);
        this.card = card;
    }

    // --> Getters <--

    /**
     * Retrieves the card associated with this request.
     *
     * @return The {@code Card} whose effect is to be applied.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Executes the request to apply the effect of an "Abandoned Ship" card
     * by delegating the processing to the appropriate controller logic.
     *
     * @param controller The controller responsible for handling the execution of the request.
     *                   If the controller is an instance of {@code GameController},
     *                   the card effect is processed by invoking {@code handleAbandonedShipEffect}.
     */
    @Override
    public void execute(Controller controller) {
        if(controller instanceof GameController){
            ((GameController) controller).handleAbandonedShipEffect(this);
        }
    }
}
