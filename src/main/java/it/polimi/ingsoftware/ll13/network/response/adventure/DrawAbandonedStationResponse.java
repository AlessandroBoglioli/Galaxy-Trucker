package it.polimi.ingsoftware.ll13.network.response.adventure;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.response.Response;

/**
 * Represents a response received when a card is drawn from an abandoned station.
 * This response contains a specific {@code Card} instance, which represents
 * the drawn card. It extends the abstract {@code Response} class.
 *
 * Upon execution, delegates handling of the response to a {@code Handler},
 * which processes it using the {@code handleDrawAbandonedStationResponse} method.
 */
public class DrawAbandonedStationResponse extends Response {

    private final Card card;

    /**
     * Constructs a new {@code DrawAbandonedStationResponse}, representing the response
     * after drawing a card from an abandoned station. This response contains the
     * drawn {@code Card} instance.
     *
     * @param card The {@code Card} object that was drawn from the abandoned station.
     */
    public DrawAbandonedStationResponse(Card card) {
        this.card = card;
    }

    // --> Getters <--

    /**
     * Retrieves the card associated with this response.
     *
     * @return The {@code Card} that was drawn in the context of this response.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Executes the logic associated with the {@code DrawAbandonedStationResponse} in the given
     * {@code Handler}. Specifically delegates the processing of this response to the
     * {@code handleDrawAbandonedStationResponse} method of the supplied {@code Handler}.
     *
     * @param handler The {@code Handler} instance responsible for handling this response.
     */
    @Override
    public void execute(Handler handler) {
        handler.handleDrawAbandonedStationResponse(this);
    }
}
