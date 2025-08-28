package it.polimi.ingsoftware.ll13.network.requests.game_requests.adventure;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.requests.Request;
import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.GameController;

/**
 * Represents a request to apply the effects of an epidemic card within the game.
 * This request is specifically designed to interact with the game's controller
 * and trigger the handling of an epidemic effect.
 *
 * This class extends the {@code Request} class, inheriting its properties
 * and functionality, while adding an additional behavior tied to a specific
 * {@code Card} instance.
 *
 * The request is sent by a specific sender, identified by their unique sender ID,
 * and it includes the {@code Card} whose epidemic effects need to be applied.
 */
public class ApplyEpidemicEffectRequest extends Request {

    private final Card card;

    /**
     * Constructs a new {@code ApplyEpidemicEffectRequest} to request the application
     * of an epidemic effect triggered by a specific card.
     *
     * @param senderId The unique identifier of the sender making the request.
     * @param card     The {@code Card} instance that triggers the epidemic effect.
     */
    public ApplyEpidemicEffectRequest(int senderId, Card card) {
        super(senderId);
        this.card = card;
    }

    // --> Getters <--

    /**
     * Retrieves the card associated with this request.
     *
     * @return The {@code Card} instance that is related to the effect or action
     *         triggered by this*/
    public Card getCard() {
        return card;
    }

    /**
     * Executes the request to apply the effects of the epidemic card by interacting
     * with the provided game controller. If the supplied controller is an instance
     * of {@code GameController}, it invokes the {@code handleEpidemicEffect} method
     * to process the epidemic effect tied to this request.
     *
     * @param controller The {@code Controller} instance responsible for handling
     *                    the request. Specifically, it is expected to be of type
     *                    {@code GameController} to properly process the epidemic effect.
     */
    @Override
    public void execute(Controller controller) {
        if(controller instanceof GameController){
            ((GameController) controller).handleEpidemicEffect(this);
        }
    }
}
