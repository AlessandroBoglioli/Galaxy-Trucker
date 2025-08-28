package it.polimi.ingsoftware.ll13.network.response.adventure;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.response.Response;

/**
 * Represents a response for drawing an epidemic card in the game. This response is
 * sent from the server side and is handled to apply the effects of an epidemic card on the game.
 * It contains detailed information about the drawn card and provides functionality
 * to process the response on the client side.
 */
public class DrawEpidemicResponse extends Response {

    private final Card card;

    /**
     * Creates a new DrawEpidemicResponse instance to handle the event of drawing an epidemic card.
     *
     * @param card The card representing the epidemic drawn from the deck.
     */
    public DrawEpidemicResponse(Card card) {
        this.card = card;
    }

    // --> Getters <--

    /**
     * Retrieves the card associated with this response.
     *
     * @return The {@code Card} instance representing the card drawn as part of this response.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Executes the handling of a draw epidemic response by delegating to the provided handler.
     *
     * @param handler The {@code Handler} instance responsible for processing this draw epidemic response.
     */
    @Override
    public void execute(Handler handler) {
        handler.handleDrawEpidemicResponse(this);
    }
}
