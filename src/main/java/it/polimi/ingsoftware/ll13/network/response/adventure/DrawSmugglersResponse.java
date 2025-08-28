package it.polimi.ingsoftware.ll13.network.response.adventure;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.response.Response;

/**
 * Represents a response for drawing a smugglers card in the game.
 * This class encapsulates the specific smugglers card that has been drawn
 * and is used to communicate this information from the server to a client.
 *
 * The {@code DrawSmugglersResponse} is a subclass of {@code Response},
 * which mandates the implementation of the {@code execute} method for
 * processing this response through a handler.
 */
public class DrawSmugglersResponse extends Response {

    private final Card card;

    /**
     * Represents a response for drawing a smugglers card in the game.
     * This class is used to encapsulate and communicate information about
     * the specific smugglers card that has been drawn from the deck.
     *
     * The card is provided to the client for processing, which may involve
     * displaying the card details or applying its effects to the game.
     *
     * @param card The card drawn from the deck, representing the smugglers.
     */
    public DrawSmugglersResponse(Card card) {
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
     * Executes the draw smugglers response by delegating the processing
     * of this response to the specified handler.
     *
     * @param handler the handler responsible for processing this draw smugglers response
     */
    @Override
    public void execute(Handler handler) {
        handler.handleDrawSmugglersResponse(this);
    }
}
