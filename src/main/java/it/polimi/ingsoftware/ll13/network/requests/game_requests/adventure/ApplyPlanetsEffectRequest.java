package it.polimi.ingsoftware.ll13.network.requests.game_requests.adventure;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.requests.Request;
import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.GameController;

/**
 * Represents a request to apply the effect of a specific card related to the "Planets Effect" in the game.
 * This class extends the {@code Request} class and is tailored for handling the application of a planet-related
 * card effect by a game controller.
 *
 **/
public class ApplyPlanetsEffectRequest extends Request {

    private final Card card;

    /**
     * Constructs an {@code ApplyPlanetsEffectRequest} object. This request is issued to apply the effect
     * of a specific planet-related card within the game. It is associated with a particular card and originates
     * from a sender identified by their unique ID.
     *
     * @param senderId The unique identifier of the player sending the request.
     * @param card     The {@code Card} object associated with the planet effect being applied.
     */
    public ApplyPlanetsEffectRequest(int senderId, Card card) {
        super(senderId);
        this.card = card;
    }

    // --> Getters <--

    /**
     * Retrieves the card associated with this request*/
    public Card getCard() {
        return card;
    }

    /**
     * Executes the request by invoking the appropriate method to handle a planet-related card effect
     * if the provided controller is an instance of {@code GameController}.
     *
     * @param controller The controller responsible for handling the request. Expected to be an instance of
     *                    {@code GameController} to process the effect of the planet-related card.
     */
    @Override
    public void execute(Controller controller) {
        if(controller instanceof GameController){
            ((GameController) controller).handlePlanetsEffect(this);
        }
    }
}
