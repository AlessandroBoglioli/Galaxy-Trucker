package it.polimi.ingsoftware.ll13.network.response.adventure;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.response.Response;

/**
 * Represents a response sent by the server to indicate that a "Draw Abandoned Ship" action has occurred.
 * This response contains details about the card*/
public class DrawAbandonedShipResponse extends Response {

    private final Card card;

    /**
     * Constructs a new {@code DrawAbandonedShipResponse} object that contains information
     * about a "Draw Abandoned Ship" action, including the associated card.
     *
     * @param card The card associated with the "Draw Abandoned Ship" action.
     */
    public DrawAbandonedShipResponse(Card card) {
        this.card = card;
    }

    // --> Getters <--

    /**
     *
     */
    public Card getCard() {
        return card;
    }

    /**
     * Executes the logic associated with processing a "Draw Abandoned Ship" response.
     * Delegates the handling of this response to the appropriate method in the {@link Handler}.
     *
     * @param handler The handler responsible for processing the "Draw Abandoned Ship" response.
     */
    @Override
    public void execute(Handler handler) {
        handler.handleDrawAbandonedShipResponse(this);
    }
}
