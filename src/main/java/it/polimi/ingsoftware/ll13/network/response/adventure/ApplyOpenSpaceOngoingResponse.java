package it.polimi.ingsoftware.ll13.network.response.adventure;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.response.Response;

/**
 * Represents the response indicating the ongoing effect of an Open Space card
 * within the game. This response is typically processed when the Open Space
 * card's effect needs to be applied or updated during gameplay.
 *
 * This class extends the {@code Response} class, which acts as a base for
 * various response types in the game, and it is designed to encapsulate the
 * Open Space card effect as part of the ongoing gameplay mechanics.
 *
 * The primary responsibility of this class is to hold the {@code Card}
 * object representing the Open Space effect and to delegate its processing
 * to the appropriate handler via the {@code execute} method.
 */
public class ApplyOpenSpaceOngoingResponse extends Response {

    private final Card card;

    /**
     * Constructs an {@code ApplyOpenSpaceOngoingResponse} object, which represents
     * an ongoing response for the effects of an Open Space card during gameplay.
     *
     * @param card The {@code Card} representing the Open Space card being applied in the response.
     */
    public ApplyOpenSpaceOngoingResponse(Card card) {
        this.card = card;
    }

    // --> Getters <--

    /**
     * Retrieves the card associated with this response.
     *
     * @return The {@code Card} object representing the current state or effect
     */
    public Card getCard() {
        return card;
    }

    /**
     * Executes the logic associated with processing the current response object.
     * This involves delegating the handling of the ongoing Open Space card effect
     * to the provided {@code Handler}.
     *
     * @param handler The {@code Handler} instance responsible for managing this
     *                response and applying its logic within the game context.
     */
    @Override
    public void execute(Handler handler) {
        handler.handleOnGoingOpenSpaceResponse(this);
    }
}
