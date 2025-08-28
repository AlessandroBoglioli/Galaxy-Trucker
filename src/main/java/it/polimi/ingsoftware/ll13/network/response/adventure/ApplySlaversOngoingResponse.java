package it.polimi.ingsoftware.ll13.network.response.adventure;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.response.Response;

/**
 * Represents a response indicating the ongoing effect of a Slavers card within the game.
 * This response is typically sent from the server to notify the client of the ongoing impact
 * of a Slavers card drawn during the Adventure phase of gameplay.
 *
 * This class encapsulates information about the Slavers card associated with the effect
 * and provides functionality for executing the appropriate client-side handling.
 *
 * It extends the {@code Response} class, inheriting its core behavior for executing
 * various server-client interaction commands, and adds specific functionality tied to
 * the Slavers card's ongoing effects.
 */
public class ApplySlaversOngoingResponse extends Response {

    private final Card card;

    /**
     *
     * @param card
     */
    public ApplySlaversOngoingResponse(Card card) {
        this.card = card;
    }

    // --> Getters <--

    /**
     * Retrieves the card associated with this response.
     *
     * @return The {@code Card} object representing the card associated with this response.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Executes the handling of the ongoing Slavers card response.
     *
     * This method invokes the appropriate processing logic in the provided {@code handler}
     * to manage the current ongoing effects of a Slavers card on the client side.
     *
     * @param handler The {@code Handler} instance responsible for managing the game state
     *                and client behavior in response to the ongoing Slavers card effect.
     */
    @Override
    public void execute(Handler handler) {
        handler.handleOngoingSlaversResponse(this);
    }
}
