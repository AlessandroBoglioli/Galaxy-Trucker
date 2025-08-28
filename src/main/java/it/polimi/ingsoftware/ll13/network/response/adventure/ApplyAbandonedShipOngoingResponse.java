package it.polimi.ingsoftware.ll13.network.response.adventure;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.response.Response;

/**
 * Represents a response for applying the effects of an ongoing "Abandoned Ship" card in the game.
 * This class extends the {@code Response} class and is used to handle*/
public class ApplyAbandonedShipOngoingResponse extends Response {

    private final Card card;

    /**
     * Constructs an {@code ApplyAbandonedShipOngoingResponse} object with the specified card.
     * This response handles the application of the effects of an ongoing "Abandoned Ship" card.
     *
     * @param card The {@code Card} object representing the ongoing "Abandoned Ship" card to be applied.
     */
    public ApplyAbandonedShipOngoingResponse(Card card) {
        this.card = card;
    }

    // --> Getters <--

    /**
     * Retrieves the {@code Card} associated with this response.
     *
     * @return The {@code Card} object representing the relevant card in the response.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Executes the logic associated with handling the ongoing "Abandoned Ship" effect.
     * Delegates the processing of this response to the specified {@code Handler}.
     *
     * @param handler The {@code Handler} responsible for processing this response.
     */
    @Override
    public void execute(Handler handler) {
        handler.handleAbandonedShipOngoing(this);
    }
}
