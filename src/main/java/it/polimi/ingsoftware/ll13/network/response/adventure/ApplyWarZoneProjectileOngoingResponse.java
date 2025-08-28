package it.polimi.ingsoftware.ll13.network.response.adventure;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.response.Response;

/**
 * Represents a response triggering the ongoing effect of a War Zone projectile in the game.
 * This response contains information about a specific card that is involved
 * in the application of the War Zone projectile effect.
 *
 * This class extends the {@code Response} class and is primarily used
 * to notify the system to execute the corresponding effect defined by
 * a {@code Handler}.
 */
public class ApplyWarZoneProjectileOngoingResponse extends Response {

    private final Card card;

    /**
     * Constructs a new {@code ApplyWarZoneProjectileOngoingResponse} object with a specific card.
     * This response is used to trigger the ongoing effect of a War Zone projectile in the game,
     * and it encapsulates the card that is involved in this effect.
     *
     * @param card The {@code Card} object that is central to the War Zone projectile's ongoing effect.
     */
    public ApplyWarZoneProjectileOngoingResponse(Card card) {
        this.card = card;
    }

    // --> Getters <--

    /**
     * Retrieves the card associated with this response.
     *
     * @return The {@code Card} object involved in the War Zone projectile's ongoing effect.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Executes the specific behavior for handling an ongoing War Zone projectile effect.
     * This method delegates the processing of this response to the provided {@code Handler} instance.
     *
     * @param handler The {@code Handler} instance responsible for managing the ongoing effect
     *                of a War Zone projectile in the game. This instance should implement
     *                the {@code handleWarZoneProjectileOnGoing} method to define the logic
     *                for processing this response.
     */
    @Override
    public void execute(Handler handler) {
        handler.handleWarZoneProjectileOnGoing(this);
    }
}
