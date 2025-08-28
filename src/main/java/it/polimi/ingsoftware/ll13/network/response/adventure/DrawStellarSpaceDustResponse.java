package it.polimi.ingsoftware.ll13.network.response.adventure;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.response.Response;

/**
 * Represents a response that encapsulates information about drawing a
 * Stellar Space Dust card during gameplay. This response is sent to
 * indicate the specific card that has been drawn and is handled by
 * the client logic accordingly.
 *
 * This class extends the {@code Response} class and implements the
 * required behavior to process the response using a {@code Handler}.
 */
public class DrawStellarSpaceDustResponse extends Response {

    private final Card card;

    /**
     * Creates a new {@code DrawStellarSpaceDustResponse} with the specified card.
     * This response is sent when a "Stellar Space Dust" card is drawn in the game.
     * It contains the card's details and allows it to be processed by the appropriate
     * handler on the client side.
     *
     * @param card The {@code Card} instance that represents the drawn "Stellar Space Dust" card.
     */
    public DrawStellarSpaceDustResponse(Card card) {
        this.card = card;
    }

    // --> Getters <--

    /**
     * Retrieves the card associated with this response.
     *
     * @return The {@code Card} instance encapsulated within the response.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Executes the handling of this {@code DrawStellarSpaceDustResponse} instance
     * using the provided {@code Handler}.
     *
     * @param handler The {@code Handler} instance responsible for processing
     *                this {@code DrawStellarSpaceDustResponse}.
     */
    @Override
    public void execute(Handler handler) {
        handler.handleDrawStellarSpaceDustResponse(this);
    }
}
