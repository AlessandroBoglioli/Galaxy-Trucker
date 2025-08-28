package it.polimi.ingsoftware.ll13.network.requests.game_requests.adventure;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.requests.Request;
import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.GameController;

/**
 * Represents a request to apply the Stellar Space Dust effect to a card in the game.
 * This*/
public class ApplyStellarSpaceDustEffectRequest extends Request {

    private final Card card;

    /**
     * Constructs an {@code ApplyStellarSpaceDustEffectRequest} object. This request is used to apply
     * the Stellar Space Dust effect to a specific card within the game. It originates from a sender
     * identified by their unique identifier.
     *
     * @param senderId The unique identifier of the player sending the request.
     * @param card     The {@code Card} object associated with the Stellar Space Dust effect to be applied.
     */
    public ApplyStellarSpaceDustEffectRequest(int senderId, Card card) {
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
     * Executes the {@code ApplyStellarSpaceDustEffectRequest} by invoking the appropriate
     * handler in the provided controller. This implementation specifically checks if the
     * controller is an instance of {@code GameController} and applies the Stellar Space
     * Dust effect through the respective handler method.
     *
     * @param controller The controller responsible for managing this request.
     *                   It must be an instance of {@code GameController} to handle
     *                   the Stellar Space Dust effect.
     */
    @Override
    public void execute(Controller controller) {
        if(controller instanceof GameController){
            ((GameController) controller).handleStellarSpaceDustEffect(this);
        }
    }
}
