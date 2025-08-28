package it.polimi.ingsoftware.ll13.network.requests.game_requests.adventure;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.requests.Request;
import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.GameController;

/**
 * Represents a specific type of request to apply the "Slavers Effect" in a game.
 * This request contains a reference to a {@link Card} and the ID of the sender.
 * It is designed to be executed within a {@link GameController}, where the effect
 * is handled and applied to the game.
 *
 * The {@code execute} method delegates the handling of this request to the
 * {@code handleSlaversEffect} method in the {@code GameController}.
 */
public class ApplySlaversEffectRequest extends Request {

    private final Card card;

    /**
     * Constructs an {@code ApplySlaversEffectRequest} object. This request is used to apply the
     * "Slavers Effect" in the game. It is tied to a specific card and originates from a sender
     * identified by their unique ID.
     *
     * @param senderId The unique identifier of the player sending the request.
     * @param card     The {@code Card} object associated with the "Slavers Effect" being applied.
     */
    public ApplySlaversEffectRequest(int senderId, Card card) {
        super(senderId);
        this.card = card;
    }

    // --> Getters <--

    /**
     *
     * @return
     */
    public Card getCard() {
        return card;
    }

    /**
     * Executes the process of handling the "Slavers Effect" request within the game.
     * This method delegates the handling of the effect to the {@code handleSlaversEffect}
     * method of the {@code GameController}, if the provided {@code controller} is an
     * instance of {@code GameController}.
     *
     * @param controller The {@code Controller} instance responsible for handling the request.
     *                    Must be an instance of {@code GameController} for the "Slavers Effect"
     *                    to be processed.
     */
    @Override
    public void execute(Controller controller) {
        if(controller instanceof GameController){
            ((GameController) controller).handleSlaversEffect(this);
        }
    }
}
