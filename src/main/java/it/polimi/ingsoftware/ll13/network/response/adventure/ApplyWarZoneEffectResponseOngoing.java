package it.polimi.ingsoftware.ll13.network.response.adventure;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.response.Response;

/**
 * Represents a response that notifies the client of an ongoing War Zone effect
 * during the game. This response includes the specific card that triggers the effect.
 *
 * This class extends the abstract {@code Response} class and implements its
 * behavior by defining the {@code execute} method, which invokes the
 * {@code handleWarZoneEffectOnGoing} method on the provided {@code Handler}.
 */
public class ApplyWarZoneEffectResponseOngoing extends Response {

    private final Card card;

    /**
     * Constructs an {@code ApplyWarZoneEffectResponseOngoing} object with the specified {@code Card}.
     * This response indicates the occurrence of an ongoing War Zone effect during the game.
     *
     * @param card The {@code Card} associated with the ongoing War Zone effect.
     */
    public ApplyWarZoneEffectResponseOngoing(Card card) {
        this.card = card;
    }

    // --> Getters <--

    /**
     * Retrieves the card associated with this response.
     *
     * @return The {@code Card} instance that is linked to the ongoing War Zone effect.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Executes the behavior defined for handling an ongoing War Zone effect.
     * This method is specifically tailored to invoke the appropriate handler
     * method for processing the {@code ApplyWarZoneEffectResponseOngoing} instance.
     *
     * @param handler The {@code Handler} responsible for managing game responses.
     *                The handler processes the ongoing War Zone effect described
     *                by this response.
     */
    @Override
    public void execute(Handler handler) {
        handler.handleWarZoneEffectOnGoing(this);
    }
}
