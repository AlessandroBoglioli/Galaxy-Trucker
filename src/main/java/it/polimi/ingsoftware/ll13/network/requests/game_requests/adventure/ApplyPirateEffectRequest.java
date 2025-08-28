package it.polimi.ingsoftware.ll13.network.requests.game_requests.adventure;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.requests.Request;
import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.GameController;

/**
 * Represents a request to apply a "Pirate Effect" on the given {@code Card}.
 * This request is intended to be handled by the {@code GameController}, where the effect of the specific
 * card is processed and executed. The request identifies the sender by an ID
 * and encapsulates the card on which the effect is to be applied.
 *
 * Upon invocation of the {@code execute} method, this request forwards itself
 * to the appropriate handling logic within the controller (if it is of type {@code GameController}).
 */
public class ApplyPirateEffectRequest extends Request {

    private final Card card;

    /**
     * Constructs an {@code ApplyPirateEffectRequest} object. This request is used to apply
     * the effect of a pirate card within the game. It is tied to the specific card provided
     * and originates from a sender identified by their unique ID.
     *
     * @param senderId The unique identifier of the player sending the request.
     * @param card     The {@code Card}*/
    public ApplyPirateEffectRequest(int senderId, Card card) {
        super(senderId);
        this.card = card;
    }

    // --> Getters <--

    /**
     * Retrieves the card associated with this request.
     *
     * @return The {@code Card} object associated with this request.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Executes the request to apply the "Pirate Effect" on the provided card.
     * This method delegates the handling of the effect to the appropriate game logic within the {@code GameController}.
     * If the provided controller is an instance of {@code GameController}, it processes this request using its
     * specialized handling method.
     *
     * @param controller The controller responsible for managing the execution of the request.
     *                   Must be an instance of {@code GameController} for this method to handle the request.
     */
    @Override
    public void execute(Controller controller) {
        if(controller instanceof GameController){
            ((GameController) controller).handlePirateEffect(this);
        }
    }
}
