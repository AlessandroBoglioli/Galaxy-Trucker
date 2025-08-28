package it.polimi.ingsoftware.ll13.network.requests.game_requests.adventure;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.requests.Request;
import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.GameController;

/**
 * Represents a request to apply the effect of a meteor shower card in the game.
 * This request extends the {@code Request} class and is specifically designed to handle
 * the effect of a meteor shower card when executed by the appropriate game controller.
 */
public class ApplyMeteorShowerEffectRequest extends Request {

    private final Card card;

    /**
     * Constructs a new {@code ApplyMeteorShowerEffectRequest} to apply the effect of a Meteor Shower card in the game.
     *
     * @param senderId The unique identifier of the player sending this request.
     * @param card     The {@code Card} object representing the Meteor Shower card associated with the request.
     */
    public ApplyMeteorShowerEffectRequest(int senderId, Card card) {
        super(senderId);
        this.card = card;
    }

    // --> Getters <--

    /**
     * Retrieves the {@code Card} object associated with this request.
     *
     * @return The {@code Card} object representing the card that is being applied or managed as part*/
    public Card getCard() {
        return card;
    }

    /**
     * Executes the specific behavior associated with the {@code ApplyMeteorShowerEffectRequest}.
     * If the provided {@code controller} is an instance of {@code GameController},
     * it triggers the handling of the meteor shower effect using this request.
     *
     * @param controller The {@code Controller} instance responsible for managing the execution
     *                   of the request. This should typically be a {@code GameController} instance.
     */
    @Override
    public void execute(Controller controller) {
        if(controller instanceof GameController){
            ((GameController) controller).handleMeteorShowerEffect(this);
        }
    }
}
