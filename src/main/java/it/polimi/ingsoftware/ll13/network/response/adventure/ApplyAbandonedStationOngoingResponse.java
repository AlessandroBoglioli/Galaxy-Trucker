package it.polimi.ingsoftware.ll13.network.response.adventure;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.response.Response;

/**
 * Represents a response that applies the ongoing effects of an abandoned station card in the game.
 * This class contains the specific card responsible for the effect and provides functionality
 * to propagate the response handling to the appropriate handler.
 *
 * The abandoned station card represents a unique game scenario, and this response is used
 * to process the ongoing effects triggered by it during the game.
 * It extends the abstract {@code Response} class, ensuring it follows the structure
 * and behavior of all responses within the game network protocol.
 */
public class ApplyAbandonedStationOngoingResponse extends Response {

    private final Card card;

    /**
     * Constructs an instance of ApplyAbandonedStationOngoingResponse with the specified card.
     *
     * @param card The {@code Card} representing the abandoned station ongoing response.
     */
    public ApplyAbandonedStationOngoingResponse(Card card) {
        this.card = card;
    }

    // --> Getters <--
    /**
     * Retrieves the card associated with this response.
     *
     * @return The {@code Card} representing the specific ongoing effect or event
     *         related to this response.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Executes the logic associated with applying the ongoing effects of the
     * abandoned station card. This method delegates the handling of this logic
     * to the specified {@code Handler} instance.
     *
     * @param handler The {@code Handler} responsible for processing the
     *                ongoing effects of the abandoned station card.
     */
    @Override
    public void execute(Handler handler) {
        handler.handleAbandonedStationOngoing(this);
    }
}
