package it.polimi.ingsoftware.ll13.network.requests.game_requests.adventure;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.requests.Request;
import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.GameController;

/**
 * The SendFirePowerRequest class represents a request sent by a player to calculate
 * the lowest fire power based on a specific card. This request encapsulates the card
 * being used and the sender's ID, and is processed by the game controller.
 * This class extends the abstract Request class and provides a concrete implementation
 * for its execute method.
 *
 * Responsibilities:
 * - Encapsulates the card details to be used in the fire power calculation.
 * - Executes the logic to handle the request through the GameController, if applicable.
 */
public class SendFirePowerRequest extends Request {

    private final Card card;

    /**
     * Constructs a {@code SendFirePowerRequest} object, representing a request to calculate
     * the lowest fire power in the game based on the provided*/
    public SendFirePowerRequest(int senderId, Card card) {
        super(senderId);
        this.card = card;
    }

    // --> Getters <--

    /**
     * Retrieves the card associated with this request.
     *
     * @return The {@code Card} object tied to this request.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Executes the logic for processing the fire power request, specifically delegating
     * the calculation of the lowest fire power to the GameController if the given controller
     * is an instance of GameController.
     *
     * @param controller The controller responsible for handling the request. This is expected
     *                   to be an instance of GameController to properly process the request.
     */
    @Override
    public void execute(Controller controller) {
        if(controller instanceof GameController){
            ((GameController) controller).handleCalculationOfLowestFirePower(this);
        }
    }
}
