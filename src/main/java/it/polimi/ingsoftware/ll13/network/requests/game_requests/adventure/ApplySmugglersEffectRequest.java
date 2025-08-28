package it.polimi.ingsoftware.ll13.network.requests.game_requests.adventure;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.requests.Request;
import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.GameController;

/**
 * This class represents a request to apply the effect of a smuggler's card within the game.
 * It extends the {@code Request} class and provides functionality specific to handling
 * smuggler's effect requests.
 *
 * The {@code ApplySmugglersEffectRequest} encapsulates the sender's ID and the {@code Card}
 * to be acted upon. It delegates the handling of the request to the appropriate {@code Controller},
 * specifically the {@code GameController}, to process the smuggler's effect.
 */
public class ApplySmugglersEffectRequest extends Request {

    private final Card card;

    /**
     * Constructs an {@code ApplySmugglersEffectRequest} object. This request is used to apply the
     * effect of a smuggler's card within the game. It originates from the sender identified by their unique ID
     * and is associated with a specific card.
     *
     * @param senderId The unique identifier of the player sending the request.
     * @param card     The {@code Card} object associated with the smuggler's effect being applied.
     */
    public ApplySmugglersEffectRequest(int senderId, Card card) {
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
     * Executes the request to apply the smuggler's card effect by delegating the handling
     * to the provided {@code Controller}. Specifically, if the controller is an instance of
     * {@code GameController}, it invokes the {@code handleSmugglersEffect} method with this request.
     *
     * @param controller The controller responsible for processing the smuggler's effect request.
     *                   Expected to be an instance of {@code GameController}.
     */
    @Override
    public void execute(Controller controller) {
        if(controller instanceof GameController){
            ((GameController) controller).handleSmugglersEffect(this);
        }
    }
}
