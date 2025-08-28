package it.polimi.ingsoftware.ll13.network.requests.game_requests.adventure;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.requests.Request;
import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.GameController;

/**
 * Represents a request to transmit thrust power within the game.
 * This request is sent by a specific*/
public class SendThrustPowerRequest extends Request {

    private final Card card;

    /**
     * Constructs a {@code SendThrustPowerRequest} object, representing a request to calculate
     * the lowest thrust power in the game based on the provided card. This request originates
     * from a specific sender identified by their unique ID.
     *
     * @param senderId The unique identifier of the player sending the request.
     * @param card     The {@code Card} object associated with the thrust power calculation.
     */
    public SendThrustPowerRequest(int senderId, Card card) {
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
     * Executes the processing of the request based on the provided controller.
     * If the controller is an instance of {@code GameController}, it delegates the
     * calculation of the lowest thrust power using the current instance of
     * {@code SendThrustPowerRequest}.
     *
     * @param controller The {@code Controller} that processes this request. This
     *                    may include specific implementations such as
     *                    {@code GameController} to handle the request logic.
     */
    @Override
    public void execute(Controller controller) {
        if(controller instanceof GameController){
            ((GameController) controller).handleCalculationOfLowestThrustPower(this);
        }
    }
}
