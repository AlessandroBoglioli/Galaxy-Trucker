package it.polimi.ingsoftware.ll13.network.response.adventure;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.response.Response;

/**
 *
 */
public class DrawPirateResponse extends Response {

    private final Card card;

    /**
     * Represents a response for drawing a pirate card in the game. This response is
     * sent from the server side and is handled to apply the effects of a pirate card on the game.
     * It contains detailed information about the drawn card and provides functionality
     * to process the response on the client side.
     *
     * @param card The card drawn from the deck, representing the pirate.
     */
    public DrawPirateResponse(Card card) {
        this.card = card;
    }

    // --> Getters <--

    /**
     * Retrieves the card associated with this response.
     *
     * @return The {@code Card} instance that represents the card drawn in this response.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Executes the logic associated with the current response by delegating
     * the handling to the provided {@code Handler} instance. Specifically, it
     * invokes the {@code handleDrawPirateResponse} method on the handler,
     * passing this response instance as a parameter.
     *
     * @param handler The {@code Handler} instance responsible for processing
     *                this specific type of response. It implements the handling
     *                logic for the pirate card draw event.
     */
    @Override
    public void execute(Handler handler){
        handler.handleDrawPirateResponse(this);
    }
}
