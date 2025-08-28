package it.polimi.ingsoftware.ll13.network.response.adventure;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.response.Response;

/**
 *
 */
public class DrawOpenSpaceResponse extends Response {

    private final Card card;

    /**
     * Creates a new DrawOpenSpaceResponse instance to handle the event of drawing an open space card.
     *
     * @param card The card drawn from the deck, representing the open space.
     */
    public DrawOpenSpaceResponse(Card card) {
        this.card = card;
    }

    // --> Getters <--

    /**
     * Retrieves the card associated with this response.
     *
     * @return The {@code Card} instance representing the card drawn in this response.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Executes the appropriate handling logic for a "Draw Open Space" response by invoking
     * the corresponding method on the provided handler.
     *
     * @param handler The handler responsible for processing this "Draw Open Space" response.
     */
    @Override
    public void execute(Handler handler) {
        handler.handleDrawOpenSpaceResponse(this);
    }
}
