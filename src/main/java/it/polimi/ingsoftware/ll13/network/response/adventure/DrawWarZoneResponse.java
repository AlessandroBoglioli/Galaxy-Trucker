package it.polimi.ingsoftware.ll13.network.response.adventure;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.response.Response;

/**
 * Represents a response for drawing a "War Zone" card during the game.
 * This response contains a card of type {@code Card} and provides functionality
 * to handle this specific response using a handler.
 *
 * This class extends the {@code Response} abstract class, inheriting its structure
 * and behavior, and is designed to be executed by a {@code Handler} implementation.
 */
public class DrawWarZoneResponse extends Response {

    private final Card card;

    /**
     * Constructs a {@code DrawWarZoneResponse} object, which represents a response
     * for drawing a "War Zone" card in the game. This response contains the associated
     * {@code Card} instance and is designed to be handled by specific logic on the client side.
     *
     * @param card The {@code Card} instance associated with the "War Zone" card drawn.
     */
    public DrawWarZoneResponse(Card card) {
        this.card = card;
    }

    // --> Getters <--

    /**
     * Retrieves the card associated with this response.
     *
     * @return The {@code Card}*/
    public Card getCard() {
        return card;
    }

    /**
     * Executes the logic associated with the {@code DrawWarZoneResponse} by delegating
     * the processing to the provided {@code Handler} implementation. This method acts
     * as the entry point for handling the response, ensuring specific behaviors are triggered
     * based on the response type.
     *
     * @param handler The {@code Handler} instance responsible for processing the
     *                {@code DrawWarZoneResponse}. The handler's implementation
     *                should define the logic for managing the effects of drawing
     *                a "War Zone" card in the game.
     */
    @Override
    public void execute(Handler handler) {
        handler.handleDrawWarZoneResponse(this);
    }
}
