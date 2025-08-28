package it.polimi.ingsoftware.ll13.network.response.adventure;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.response.Response;

/**
 * Represents a response that applies the ongoing*/
public class ApplyPirateOngoingResponse extends Response {

    private final Card card;

    /**
     * Constructs an ApplyPirateOngoingResponse with the specified card.
     *
     * @param card The {@code Card} object representing the pirate card whose ongoing effect is to be applied.
     */
    public ApplyPirateOngoingResponse(Card card) {
        this.card = card;
    }

    // --> Getters <--

    /**
     * Retrieves the card associated with this response.
     *
     * @return The {@code Card} object associated with this response.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Executes the handling of the pirate's ongoing effect response using the provided {@code Handler}.
     *
     * @param handler The {@code Handler} instance responsible for processing this response.
     */
    @Override
    public void execute(Handler handler) {
        handler.handlePiratesOngoingResponse(this);
    }
}
