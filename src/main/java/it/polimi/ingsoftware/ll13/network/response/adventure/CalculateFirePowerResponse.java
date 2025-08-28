package it.polimi.ingsoftware.ll13.network.response.adventure;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.response.Response;
import it.polimi.ingsoftware.ll13.server.controller.GameController;

/**
 * Represents a response sent during the adventure phase of the game to calculate the firepower
 * in a war zone scenario. This response provides a specific card associated with the calculation,
 * which will define the firepower applied in*/
public class CalculateFirePowerResponse extends Response {

    private final Card card;

    /**
     * Constructs a response for calculating the firepower in war zone scenarios
     * during the game's adventure phase. This response encapsulates a specific card
     * that determines the firepower calculation.
     *
     * @param card The card associated with the calculation of firepower in the war zone.
     */
    public CalculateFirePowerResponse(Card card) {
        this.card = card;
    }

    // --> Getters <--

    /**
     * Retrieves the card associated with this response.
     *
     * @return The {@code Card} instance encapsulated in this response.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Executes the handling of a war zone firepower calculation response by delegating
     * the processing to the provided handler. This method ensures the specific logic
     * related to the war zone firepower calculation is executed within the context of the handler.
     *
     * @param handler The handler responsible for managing and processing the war zone
     *                firepower calculation logic associated with this response.
     */
    @Override
    public void execute(Handler handler) {
        handler.handleWarZoneFirePower(this);
    }
}
