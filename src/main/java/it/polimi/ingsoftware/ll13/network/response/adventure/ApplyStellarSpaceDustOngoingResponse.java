package it.polimi.ingsoftware.ll13.network.response.adventure;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.response.Response;

/**
 * Represents a response that carries the application of ongoing effects caused by the
 * Stellar Space Dust event in the game. This response encapsulates the specific card
 * tied to this effect and provides execution logic to notify the corresponding handler.
 *
 * <p>The ongoing effect is typically processed by calling the appropriate handler method
 * within the client-side system to react accordingly within the gameplay context.</p>
 */
public class ApplyStellarSpaceDustOngoingResponse extends Response {

    private final Card card;

    /**
     * Creates a new response to apply the ongoing effects induced by the Stellar Space Dust event.
     * The response is associated with a specific card, which represents the entity affected
     * by the ongoing effects in the game.
     *
     * @param card The card associated with the ongoing effects caused by the Stellar Space Dust event.
     */
    public ApplyStellarSpaceDustOngoingResponse(Card card) {
        this.card = card;
    }

    // --> Getters <--

    /**
     * Retrieves the card associated with this response.
     *
     * @return The {@code Card} tied to this response, representing the specific game element
     *         affected or used in conjunction with this response.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Executes the logic associated with handling the ongoing effects of a Stellar Space Dust event.
     * This method delegates the responsibility to the provided handler by invoking its
     * {@code handleStellarSpaceDustOnGoing} method with the current response.
     *
     * @param handler The handler responsible for processing this response to apply the
     *                ongoing effects of the Stellar Space Dust event during the game.
     */
    @Override
    public void execute(Handler handler) {
        handler.handleStellarSpaceDustOnGoing(this);
    }
}
