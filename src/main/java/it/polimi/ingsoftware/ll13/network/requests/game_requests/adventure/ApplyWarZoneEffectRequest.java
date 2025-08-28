package it.polimi.ingsoftware.ll13.network.requests.game_requests.adventure;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.requests.Request;
import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.GameController;

/**
 * Represents a request to apply a War Zone effect using a specified card.
 * This request is sent by a specific sender and is intended to be handled by a controller.
 * The main responsibility of this class is to encapsulate the logic for
 * applying the*/
public class ApplyWarZoneEffectRequest extends Request {

    private final Card card;

    /**
     * Constructs a request to apply a War Zone effect using the specified card.
     * This request originates from a specific sender identified by their unique ID.
     *
     * @param senderId The unique identifier of the player sending the request.
     * @param card     The {@code Card} object representing the War Zone effect to be applied.
     */
    public ApplyWarZoneEffectRequest(int senderId, Card card) {
        super(senderId);
        this.card = card;
    }

    // --> Getters <--

    /**
     * Retrieves the card associated with this request.
     *
     * @return The {@code Card} object linked to this request.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Executes the logic to handle a War Zone effect request by interacting with the provided controller.
     * If the controller is an instance of {@code GameController}, this method delegates the handling
     * of the War Zone effect request to the {@code handleWarZoneEffect} method of the {@code GameController}.
     *
     * @param controller The {@code Controller} responsible for processing the War Zone effect request.
     *                    Must be an instance of {@code GameController} to properly handle the request.
     */
    @Override
    public void execute(Controller controller) {
        if(controller instanceof GameController){
            ((GameController) controller).handleWarZoneEffect(this);
        }
    }
}
