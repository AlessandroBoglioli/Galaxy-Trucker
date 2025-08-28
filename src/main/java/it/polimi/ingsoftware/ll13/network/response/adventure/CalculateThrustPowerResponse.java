package it.polimi.ingsoftware.ll13.network.response.adventure;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.response.Response;

/**
 * Represents a response received during the calculation of the thrust power
 * in the game. This response contains a specific {@code Card} object whose
 * attributes and effects are evaluated to calculate the thrust power.
 *
 * The {@code CalculateThrustPowerResponse} is designed to be processed by a
 * {@code Handler} object, which defines the logic for interpreting and
 * applying this response to the client-side game state or logic.
 */
public class CalculateThrustPowerResponse extends Response {

    private final Card card;

    /**
     * Constructs a new {@code CalculateThrustPowerResponse} object with the specified {@code Card}.
     * This class represents the response containing the information needed to calculate thrust power
     * based on the provided card data.
     *
     * @param card The {@code Card} object associated with the thrust power calculation.
     */
    public CalculateThrustPowerResponse(Card card) {
        this.card = card;
    }

    // --> Getters <--

    /**
     * Retrieves the {@code Card} object associated with this response.
     *
     * @return The {@code Card} object contained in this response.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Executes the action associated with the {@code CalculateThrustPowerResponse}.
     * Delegates the handling of the calculation of thrust power to the provided {@code Handler}.
     *
     * @param handler The {@code Handler} responsible for processing the
     *                thrust power calculation logic for this response.
     */
    @Override
    public void execute(Handler handler) {
        handler.handleCalculateThrustPower(this);
    }
}
