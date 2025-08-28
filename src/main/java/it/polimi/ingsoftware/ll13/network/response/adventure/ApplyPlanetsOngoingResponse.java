package it.polimi.ingsoftware.ll13.network.response.adventure;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.response.Response;

/**
 * Represents a response indicating the ongoing application of a planetary effect in the game.
 * This class extends the {@code Response} class, inheriting its functionality
 * to be executed on a {@code Handler}.
 *
 * The response contains a reference to a {@code Card}, representing the specific card
 * associated with the planetary effect being applied.
 */
public class ApplyPlanetsOngoingResponse extends Response {

    private final Card card;

    /**
     * Constructs an ApplyPlanetsOngoingResponse instance with the specified card.
     * This response is used to indicate the ongoing application of a planetary effect.
     *
     * @param card The {@code Card} associated with this response, representing the effect being applied.
     */
    public ApplyPlanetsOngoingResponse(Card card) {
        this.card = card;
    }

    // --> Getters <--
    /**
     * Retrieves the card associated with the ongoing planetary effect in this response.
     *
     * @return The {@code Card} instance representing the specific card related
     *         to the planetary effect being applied.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Executes the response by invoking the appropriate handler method.
     * Delegates the ongoing planetary effect response handling to the provided handler.
     *
     * @param handler The handler responsible for processing this response.
     */
    @Override
    public void execute(Handler handler) {
        handler.handlePlanetsOngoingResponse(this);
    }
}
