package it.polimi.ingsoftware.ll13.network.response.adventure;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.response.Response;

/**
 * Represents a response that applies the ongoing effects of the Smugglers card in the game.
 * This response is part of the game's network protocol and is handled by the client to
 * update the state relevant to the ongoing effects of the Smugglers card.
 *
 * The response encapsulates a single {@link Card} object, which represents the Smugglers card
 * whose ongoing effects need to be applied.
 *
 * The class extends the {@code Response} class, which provides the base functionality for
 * handling various types of responses in the game's network communication framework.
 *
 * The {@code execute} method is overridden to handle this specific type of response by invoking
 * the {@code handleOngoingSmugglersResponse} method on the provided {@link Handler} object.
 */
public class ApplySmugglersOngoingResponse extends Response {

    private final Card card;

    /**
     * Constructs a response to apply the ongoing effects of a Smugglers card in the game.
     *
     * @param card The Smugglers card whose ongoing effects are being applied.
     */
    public ApplySmugglersOngoingResponse(Card card) {
        this.card = card;
    }

    // --> Getters <--

    /**
     * Retrieves the card associated with this response.
     *
     * @return The {@code Card} object encapsulated in this response.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Executes the handling of this specific response by delegating it to the provided handler.
     *
     * @param handler The handler responsible for processing the ongoing Smugglers response.
     */
    @Override
    public void execute(Handler handler) {
        handler.handleOngoingSmugglersResponse(this);
    }
}
