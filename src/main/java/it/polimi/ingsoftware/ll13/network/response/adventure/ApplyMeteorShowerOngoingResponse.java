package it.polimi.ingsoftware.ll13.network.response.adventure;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.response.Response;

/**
 * Represents a response that applies the ongoing effect of a meteor shower
 * in the game context. This class is used to handle the continuation of
 * the meteor shower effect on the relevant game components.
 *
 * The response contains a reference to the specific card that triggers or
 * represents the meteor shower effect. The card encapsulates the properties
 * and behavior associated with this effect.
 *
 * This response is executed by invoking the {@code handleOnGoingMeteorShowerResponse}
 * method of the provided {@code Handler} implementation.
 *
 * Inherits from the abstract {@code Response} class, which provides a contract
 * for executing the response using the specified {@code Handler}.
 */
public class ApplyMeteorShowerOngoingResponse extends Response {

    private final Card card;

    /**
     * Constructs a new ApplyMeteorShowerOngoingResponse with the specified card.
     *
     * @param card The {@code Card} associated with the ongoing meteor shower response.
     */
    public ApplyMeteorShowerOngoingResponse(Card card) {
        this.card = card;
    }

    // --> Getters <--
    /**
     * Retrieves the card associated with this response.
     *
     * @return The {@code Card} object linked to the response.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Executes the logic associated with applying the ongoing meteor shower effect
     * in the context of the game. This method delegates the handling of the
     * {@code ApplyMeteorShowerOngoingResponse} to the specified {@code Handler}.
     *
     * @param handler The {@code Handler} responsible for processing this ongoing
     *                meteor shower response and implementing the corresponding behavior.
     */
    @Override
    public void execute(Handler handler) {
        handler.handleOnGoingMeteorShowerResponse(this);
    }
}
