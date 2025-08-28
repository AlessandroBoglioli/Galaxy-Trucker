package it.polimi.ingsoftware.ll13.network.response.adventure;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.response.Response;

/**
 * Represents a response sent by the server when a Slavers card is drawn
 * during the adventure phase of the game.
 *
 * This response encapsulates the Slavers card that was drawn and provides
 * the mechanism to handle this event appropriately through the associated
 * handler's method.
 */
public class DrawSlaversResponse extends Response {

    private final Card card;

    /**
     * Represents a response triggered when a Slavers card is drawn during the game.
     * This response contains the card information and facilitates the handling of
     * this event through a specific method in the associated handler.
     *
     * @param card The {@code Card} instance representing the Slavers card that was drawn.
     */
    public DrawSlaversResponse(Card card) {
        this.card = card;
    }

    // --> Getters <--

    /**
     * Retrieves the {@code Card} instance associated with*/
    public Card getCard() {
        return card;
    }

    /**
     * Executes the logic for processing this response by delegating it
     * to the appropriate method in the provided {@code Handler} instance.
     * Specifically, it invokes the {@code handleDrawSlaversResponse} method
     * on the handler, passing this response as an argument.
     *
     * @param handler The {@code Handler} instance responsible for handling
     *                this specific type of response, allowing the effects
     *                of the Slavers card draw event to be processed.
     */
    @Override
    public void execute(Handler handler) {
        handler.handleDrawSlaversResponse(this);
    }
}
